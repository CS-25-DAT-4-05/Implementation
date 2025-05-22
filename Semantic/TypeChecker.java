package Semantic;

import AbstractSyntax.Expressions.*;
import AbstractSyntax.Statements.*;
import AbstractSyntax.Types.*;
import java.util.*;

public class TypeChecker {
    private final Map<String, Type> typeEnv = new HashMap<>();
    private final List<String> errors = new ArrayList<>();

    public void check(Stmt stmt) {
        checkStmt(stmt);
        if (!errors.isEmpty()) {
            for (String err : errors) System.err.println(err);
            throw new RuntimeException("Type checking failed.");
        }
    }

    // ================================
    // Expression type checking
    // ================================
    private Type checkExpr(Expr expr) {
        if (expr instanceof IntVal) return new SimpleType(SimpleTypesEnum.INT);
        if (expr instanceof DoubleVal) return new SimpleType(SimpleTypesEnum.DOUBLE);
        if (expr instanceof CharVal) return new SimpleType(SimpleTypesEnum.CHAR);
        if (expr instanceof BoolVal) return new SimpleType(SimpleTypesEnum.BOOL);

        if (expr instanceof Variable var) {
            if (!typeEnv.containsKey(var.ident)) {
                errors.add("Undeclared variable '" + var.ident + "'");
                return new SimpleType(SimpleTypesEnum.INT);
            }
            return typeEnv.get(var.ident);
        }

        if (expr instanceof BinExpr bin) {
            Type left = checkExpr(bin.left);
            Type right = checkExpr(bin.right);
            if (!left.equals(right)) errors.add("Binary op mismatch: " + left + " vs " + right);
            return left;
        }

        if (expr instanceof UnExpr unary) {
            return checkExpr(unary.expr);
        }

        if (expr instanceof TensorLiteral tensor) {
            return tensor.type;  // Assumed inferred during AST construction
        }

        if (expr instanceof TensorAccessExpr access) {
            Type baseType = checkExpr(access.listExpr);

            if (!(baseType instanceof TensorType tensorType)) {
                errors.add("Tensor access on non-tensor type");
                return new SimpleType(SimpleTypesEnum.INT);
            }

            if (access.indices.size() != tensorType.shape().size()) {
                errors.add("Tensor index count mismatch");
            }

            for (Expr index : access.indices) {
                Type indexType = checkExpr(index);
                if (!(indexType instanceof SimpleType)) {
                    errors.add("Tensor index must be of type INT");
            } else {
                SimpleType st = (SimpleType) indexType;
                if (st.type != SimpleTypesEnum.INT) {
                errors.add("Tensor index must be of type INT");
    }
}

            }

            return tensorType.base();
        }

        errors.add("Unknown expression type: " + expr.getClass());
        return new SimpleType(SimpleTypesEnum.INT);
    }

    // ================================
    // Statement type checking
    // ================================
    private void checkStmt(Stmt stmt) {
        if (stmt instanceof Declaration decl) {
            Type declaredType = decl.type;
            if (typeEnv.containsKey(decl.ident)) {
                errors.add("Variable '" + decl.ident + "' already declared");
            } else {
                typeEnv.put(decl.ident, declaredType);
            }

            if (decl.expr != null) {
                Type exprType = checkExpr(decl.expr);
                if (!declaredType.equals(exprType)) {
                    errors.add("Type mismatch in declaration: " + declaredType + " vs " + exprType);
                }
            }
        }

        
        else if (stmt instanceof If ifstmt) {
            Type condType = checkExpr(ifstmt.cond);
            if (condType != new SimpleType(SimpleTypesEnum.BOOL)) {
                errors.add("If condition must be of type BOOL");
            }
            checkStmt(ifstmt.then);
            if (ifstmt.els != null) {
                    checkStmt(ifstmt.els);
            }
            }

        else if (stmt instanceof While whilestmt) {
            Type condType = checkExpr(whilestmt.cond);
            if (condType != new SimpleType(SimpleTypesEnum.BOOL)) {
            errors.add("While condition must be of type BOOL");
            }
            checkStmt(whilestmt.body);
}

    }

    // ================================
    // Helper: Convert string → type
    // ================================
    private Type convertStringToType(String typeStr) {
        return switch (typeStr) {
            case "int" -> new SimpleType(SimpleTypesEnum.INT);
            case "double" -> new SimpleType(SimpleTypesEnum.DOUBLE);
            case "char" -> new SimpleType(SimpleTypesEnum.CHAR);
            case "bool" -> new SimpleType(SimpleTypesEnum.BOOL);
            default -> {
                errors.add("Unknown type: " + typeStr);
                yield new SimpleType(SimpleTypesEnum.INT);
            }
        };
    }
}
