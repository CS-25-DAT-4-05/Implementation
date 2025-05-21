package Semantic;

import java.util.*;

import javax.management.openmbean.SimpleType;

//import javax.management.openmbean.SimpleType;

//AST Interfaces and Classes
import AbstractSyntax.Expressions.*;
import AbstractSyntax.Statements.*;
import AbstractSyntax.Types.*;
import AbstractSyntax.Definitions.*;
import AbstractSyntax.Programs.*;
import AbstractSyntax.SizeParams.*;

import Lib.Pair;


//Core type interfaces
//import AbstractSyntax.Types.SimpleTypesEnum;


/* 
//From AbstractSyntax.Definitions
import AbstractSyntax.Definitions.FuncDef;

//From AbstractSyntax.Programs
import AbstractSyntax.Program.Prog;

//From AbstractSyntax.SizeParams
import AbstractSyntax.SizeParams.SizeParam;
import AbstractSyntax.SizeParams.SPIdent;
import AbstractSyntax.SizeParams.SPInt;

//From AbstractSyntax.Statements 
import AbstractSyntax.Statements.Assign;
import AbstractSyntax.Statements.Comp;
import AbstractSyntax.Statements.Declaration;
import AbstractSyntax.Statements.Defer;
import AbstractSyntax.Statements.If;
import AbstractSyntax.Statements.Stmt;
import AbstractSyntax.Statements.While;

//From AbstractSyntax.Expressions
import AbstractSyntax.Expressions.BinExpr;
import AbstractSyntax.Expressions.BinOperator;
import AbstractSyntax.Expressions.BoolVal;
import AbstractSyntax.Expressions.CharVal;
import AbstractSyntax.Expressions.DoubleVal;
import AbstractSyntax.Expressions.Expr;
import AbstractSyntax.Expressions.FuncCallExpr;
import AbstractSyntax.Expressions.Ident;
import AbstractSyntax.Expressions.IntVal;
import AbstractSyntax.Expressions.ParenExpr;
import AbstractSyntax.Expressions.TensorAccessExpr;
import AbstractSyntax.Expressions.TensorDefExpr;
import AbstractSyntax.Expressions.UnaryOperator;
import AbstractSyntax.Expressions.UnExpr;
*/


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

        if (expr instanceof Var var) {
            if (!typeEnv.containsKey(var.ident)) {
                errors.add("Undeclared variable '" + var.ident + "'");
                return new SimpleType(SimpleTypesEnum.INT);
            }
            return typeEnv.get(var.ident);
        }

        if (expr instanceof BinOpExpr bin) {
            Type left = checkExpr(bin.left);
            Type right = checkExpr(bin.right);
            if (!left.equals(right)) errors.add("Binary op mismatch: " + left + " vs " + right);
            return left;
        }

        if (expr instanceof UnaryOpExpr unary) {
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
                if (!(indexType instanceof SimpleType st) || st.type != SimpleTypesEnum.INT) {
                    errors.add("Tensor index must be INT");
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

        // Extend with If, While, Assignment, Return, etc. if needed
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
