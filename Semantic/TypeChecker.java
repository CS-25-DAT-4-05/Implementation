package Semantic;
//Libraries used
import AbstractSyntax.Program.Prog;
import AbstractSyntax.Definitions.FuncDef;
import AbstractSyntax.Statements.*;
import AbstractSyntax.Expressions.*;
import AbstractSyntax.Types.*;
import Lib.Pair;

import java.util.*;

//import javax.management.openmbean.SimpleType;

public class TypeChecker {
    
    private final Map<String, Type> typeEnv = new HashMap<>(); //"typeEnv" is a symbol table mapping variables,     
    private final List<String> errors = new ArrayList<>(); //A list to collect redeable error messages. After checking; if empty, the program is valid. If not empty the program is invalid 
    
    //This is the main function our parser/compiler will call. Input: Takes a single Stmt. Output: if errors, print and throw errors, if no errors, nothing (void)
    public void check(Stmt stmt) {
        checkStmt(stmt);
        if (!errors.isEmpty()) {
            for (String err : errors) {
                System.err.println(err);
            }
            throw new RuntimeException("Type checking failed with " + errors.size() + " error(s)."); //Prints number of errors
        }
    }

    private void checkStmt(Stmt stmt) { //Recursively check any Stmt type; Assign, Declaration, If, While etc...
        if (stmt instanceof Declaration decl) { //Checks if a variable is already declared, if it has; error
            if (typeEnv.containsKey(decl.ident)) {
                errors.add("Variable '" + decl.ident + "' already declared.");
            } else {
                Type exprType = checkExpr(decl.expr);
                if (!sameType(decl.t, exprType)) {
                    errors.add("Type mismatch in declaration of '" + decl.ident + "'");
                }
                typeEnv.put(decl.ident, decl.t);
                checkStmt(decl.stmt); //Recursively typechecks the next statement (decl.stmt)
            }
        } else if (stmt instanceof Assign assign) { //Verifies the variable was declared
            Type varType = typeEnv.get(assign.ident); 
            if (varType == null) {
                errors.add("Assignment to undeclared variable '" + assign.ident + "'");
                return;
            }
            Type exprType = checkExpr(assign.expr); //Checks that the assigned expression matches the variables type
            if (!sameType(varType, exprType)) {
                errors.add("Type mismatch in assignment to '" + assign.ident + "'");
            }
        } else if (stmt instanceof Comp comp) { //Recursively checks both statements in order
            checkStmt(comp.stmt1);
            checkStmt(comp.stmt2);
        } else if (stmt instanceof If ifstmt) { //Checks that the condition is a bool, recursively typechecks the then, else and/or loop body
            Type condType = checkExpr(ifstmt.cond);
            if (!(condType instanceof SimpleType st && st.type == SimpleTypesEnum.BOOL)) {
                errors.add("Condition in 'if' must be boolean.");
            }
            checkStmt(ifstmt.then);
            if (ifstmt.els != null) checkStmt(ifstmt.els);
        } else if (stmt instanceof While whilestmt) { //Checks that the condition is a bool, recursively typechecks the then, else and/or loop body
            Type condType = checkExpr(whilestmt.cond);
            if (!(condType instanceof SimpleType st && st.type == SimpleTypesEnum.BOOL)) {
                errors.add("Condition in 'while' must be boolean.");
            }
            checkStmt(whilestmt.stmt);
        } else if (stmt instanceof Defer deferstmt) { //Only typechecks inner statement right now, can add GPU-related checks later
            checkStmt(deferstmt.stmt);
        } else {
            errors.add("Unknown statement type: " + stmt.getClass().getSimpleName()); //Fallback, if invalid AST node type (Statement not defined), error
        }
    }


    private Type checkExpr(Expr expr) { //Checks expression, 
        if (expr instanceof IntLiteral) {
            return new SimpleType(SimpleTypesEnum.INT);
        } else if (expr instanceof BoolLiteral) {
            return new SimpleType(SimpleTypesEnum.BOOL);
        } else if (expr instanceof VarExpr var) {
            if (!typeEnv.containsKey(var.name)) {
                errors.add("Use of undeclared variable '" + var.name + "'");
                return null;
            }
            return typeEnv.get(var.name);
        } else if (expr instanceof BinaryExpr bin) { //Checks if expression is a binary operator (+,<,&&,^,=,)
            Type left = checkExpr(bin.left);
            Type right = checkExpr(bin.right);
            if (!sameType(left, right)) {
                errors.add("Type mismatch in binary expression: " + pretty(left) + " " + bin.op + " " + pretty(right));
                return null;
            }
            return switch (bin.op) {
                case ADD, SUB, MUL -> left;
                case LT, EQ -> new SimpleType(SimpleTypesEnum.BOOL);
                default -> null;
            };
        } else if (expr instanceof UnaryExpr un) { //Checks if expression is a unary(++,--)
            Type inner = checkExpr(un.expr);
            return switch (un.op) {
                case NEG -> {
                    if (!(inner instanceof SimpleType st) || st.type != SimpleTypesEnum.INT)
                        errors.add("NEG expects an int."); //If Unary Expression is not a int; error
                    yield inner;
                }
                case NOT -> {
                    if (!(inner instanceof SimpleType st) || st.type != SimpleTypesEnum.BOOL)
                        errors.add("NOT expects a bool."); //If Unary expression
                    yield new SimpleType(SimpleTypesEnum.BOOL);
                }
            };
        } else {
            errors.add("Unknown expression type: " + expr.getClass().getSimpleName());
            return null;
        }
    }
    //Beep boop, I dont know why this doesnt work????
    private boolean sameType(Type t1, Type t2) {
    if (t1 == null || t2 == null) return false; //Null safety

    if (t1.getClass() != t2.getClass()) return false; // Same class

    //Simple types
    if (t1 instanceof SimpleType && t2 instanceof SimpleType) {
        SimpleType st1 = (SimpleType) t1;
        SimpleType st2 = (SimpleType) t2;
        return st1.type == st2.type;
    }

    //Tensor types
    if (t1 instanceof TensorType && t2 instanceof TensorType) {
        TensorType tt1 = (TensorType) t1;
        TensorType tt2 = (TensorType) t2;
        return sameType(tt1.componentType, tt2.componentType) &&
               tt1.dimensions.size() == tt2.dimensions.size();



        //IMPROVEMENT: shape matching can be added here
        
        //Compare actual, SizeParam values (ID vs INTVAL) for stricter tensor shape matching.
    }
    //return false;

    }

    // Entry point: checks a full program consisting of multiple function definitions
    public void check(Prog prog) {
        FuncDef current = prog.func;
        while (current != null) {
            checkFunc(current);
            current = current.nextFunc;
        }

        if (!errors.isEmpty()) {
            for (String err : errors) {
                System.err.println(err);
             }
            throw new RuntimeException("Type checking failed with " + errors.size() + " error(s).");
        }
    }

    // Checks a function: its parameter declarations, body, and return expression
    private void checkFunc(FuncDef func) {
        typeEnv.clear(); // Reset environment for each function

        for (Pair<Type, String> param : func.formalParams) {
            String name = param.snd;
            if (typeEnv.containsKey(name)) {
                errors.add("Duplicate parameter name: " + name + " in function " + func.procname);
            } else {
                typeEnv.put(name, param.fst);
            }
    }

        checkStmt(func.funcBody);

        Type returnType = checkExpr(func.returnExpr);
        if (!sameType(func.returnType, returnType)) {
            errors.add("Function '" + func.procname + "' has return type " + pretty(func.returnType) +
                       ", but returns expression of type " + pretty(returnType));
        }
    }

    private String pretty(Type type) {
    if (type instanceof SimpleType st) {
        return st.type.name().toLowerCase();
    } else if (type instanceof TensorType tt) {
        return "tensor[" + pretty(tt.componentType) + ", " + tt.dimensions.size() + "D]";

    } else {
        return "unknown";
    }
}

}
