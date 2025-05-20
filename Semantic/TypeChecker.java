package Semantic;

import java.util.*;
import Lib.Pair;

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

//From AbstractSyntax.Types
import AbstractSyntax.Types.SimpleType;
import AbstractSyntax.Types.SimpleTypesEnum;
import AbstractSyntax.Types.TensorType;
import AbstractSyntax.Types.Type;

//import javax.management.openmbean.SimpleType;

public class TypeChecker {

    // Maps variable names to their declared types (e.g., "x" -> INT)
    private final Map<String, Type> typeEnv = new HashMap<>();

    // Keeps a list of error messages to report after checking
    private final List<String> errors = new ArrayList<>();

    // === MAIN ENTRY POINT ===
    public void check(Stmt stmt) {
        checkStmt(stmt);
        if (!errors.isEmpty()) {
            for (String err : errors) {
                System.err.println(err);
            }
            throw new RuntimeException("Type checking failed with " + errors.size() + " error(s).");
        }
    }

    // === Handles all Statement types ===
    private void checkStmt(Stmt stmt) {
        if (stmt instanceof Declaration decl) {
            // Declares a new variable with its type
            if (typeEnv.containsKey(decl.ident)) {
                errors.add("Variable '" + decl.ident + "' already declared.");
            } else {
                Type exprType = checkExpr(decl.expr);
                typeEnv.put(decl.ident, exprType);
            }

        } else if (stmt instanceof Assign assign) {
            // Checks type match between variable and assigned expression
            Type varType = typeEnv.get(assign.ident);
            Type exprType = checkExpr(assign.expr);
            if (!sameType(varType, exprType)) {
                errors.add("Type mismatch in assignment to '" + assign.ident + "'");
            }

        } else if (stmt instanceof Comp comp) {
            // Compound statement: check both in sequence
            Comp compStmt = (Comp) stmt;
            checkStmt(compStmt.first);
            checkStmt(compStmt.second);

        } else if (stmt instanceof If ifstmt) {
            // If statement: check condition and branches
            Type condType = checkExpr(ifstmt.cond);
            checkStmt(ifstmt.thenstmt);
            if (ifstmt.elsestmt != null) checkStmt(ifstmt.elsestmt);

        } else if (stmt instanceof While whilestmt) {
            // While loop: check condition and body
            Type condType = checkExpr(whilestmt.cond);
            checkStmt(whilestmt.body);

        } else if (stmt instanceof Defer deferstmt) {
            // Defer: check inner statement
            checkStmt(deferstmt.inner);
        }
    }

    // === Handles Expressions and infers their types ===
    private Type checkExpr(Expr expr) {
        if (expr instanceof IntVal) {
            return new SimpleType(SimpleTypesEnum.INT);

        } else if (expr instanceof BoolVal) {
            return new SimpleType(SimpleTypesEnum.BOOL);

        } else if (expr instanceof DoubleVal) {
            return new SimpleType(SimpleTypesEnum.DOUBLE);

        } else if (expr instanceof Ident var) {
            // Lookup variable type from the symbol table
            if (!typeEnv.containsKey(var.name)) {
                errors.add("Undeclared variable '" + var.name + "'");
                return new SimpleType(SimpleTypesEnum.INT); // Fallback type
            }
            return typeEnv.get(var.name);

        } else if (expr instanceof BinExpr bin) {
            Type left = checkExpr(bin.left);
            Type right = checkExpr(bin.right);
            // Basic type compatibility check for binary operations
            if (!sameType(left, right)) {
                errors.add("Type mismatch in binary operation.");
            }
            return left; // Simplified — might depend on operator in future

        } else if (expr instanceof UnExpr un) {
            // Unary expression: return type of inner expression
            return checkExpr(un.expr);
        }

        // Default fallback — should not reach here ideally
        return new SimpleType(SimpleTypesEnum.INT);
    }

    // === Checks whether two types are the same ===
    private boolean sameType(Type t1, Type t2) {
        if (t1 == null || t2 == null) return false;
        return t1.getClass() == t2.getClass(); // Compares actual class types
    }

    // === Handles function declarations ===
    /*
    public void check(Prog prog) {
        for (FuncDef func : prog.funcs) {
            checkFunc(func);
        }
    }
    */

    // === Type checks a single function ===
    private void checkFunc(FuncDef func) {
        // Add parameters to symbol table
        for (Pair<Type, String> param : func.formalParams) {
            //typeEnv.put(param.snd, param.fst);
        }

        // Check return expression type
        Type returnType = checkExpr(func.returnExpr);
        if (!sameType(returnType, func.returnType)) {
            errors.add("Return type mismatch in function '" + func.name + "'");
        }
    }

    // === Converts a type to its name for printing (used in debugging) ===
    private String pretty(Type type) {
        return type.getClass().getSimpleName();
    }
}