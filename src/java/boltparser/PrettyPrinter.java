package boltparser;

import AbstractSyntax.Expressions.*;
import AbstractSyntax.Statements.*;
import AbstractSyntax.Types.*;

/**
 * Pretty printer for BOLT AST nodes. This class traverses the AST and
 * outputs the code in a formatted way.
 */
public class PrettyPrinter {
    private int indentLevel = 0;
    private final StringBuilder output = new StringBuilder();

    /**
     * Pretty prints a statement
     *
     * @param stmt The statement to pretty print
     * @return The pretty printed string
     */
    public String print(Stmt stmt) {
        output.setLength(0); // Clear the buffer
        visitStmt(stmt);
        return output.toString();
    }

    /**
     * Increase the indentation level
     */
    private void indent() {
        indentLevel += 2;
    }

    /**
     * Decrease the indentation level
     */
    private void dedent() {
        indentLevel = Math.max(0, indentLevel - 2); // Ensure we don't go negative
    }

    /**
     * Add indentation to the current line
     */
    private void appendIndent() {
        output.append(" ".repeat(indentLevel));
    }

    /**
     * Add a line of text with indentation
     */
    private void appendLine(String text) {
        appendIndent();
        output.append(text).append("\n");
    }

    /**
     * Add text without a newline
     */
    private void append(String text) {
        output.append(text);
    }

    /**
     * Visit a statement node
     */
    private void visitStmt(Stmt stmt) {
        if (stmt == null) {
            return;
        }

        if (stmt instanceof Comp) {
            visitComp((Comp) stmt);
        } else if (stmt instanceof Declaration) {
            visitDeclaration((Declaration) stmt);
        } else if (stmt instanceof Assign) {
            visitAssign((Assign) stmt);
        } else if (stmt instanceof If) {
            visitIf((If) stmt);
        } else if (stmt instanceof While) {
            visitWhile((While) stmt);
        } else {
            appendLine("// Unknown statement: " + stmt.getClass().getSimpleName());
        }
    }

    /**
     * Visit a compound statement
     */
    private void visitComp(Comp comp) {
        visitStmt(comp.stmt1);
        visitStmt(comp.stmt2);
    }

    /**
     * Visit a declaration statement
     */
    private void visitDeclaration(Declaration decl) {
        appendIndent();
        append(printType(decl.t));
        append(" ");
        append(decl.ident);

        if (decl.expr != null) {
            append(" := ");
            visitExpr(decl.expr);
        }

        append(";\n");

        // Handle any subsequent statement in the chain
        if (decl.stmt != null) {
            visitStmt(decl.stmt);
        }
    }

    /**
     * Visit an assignment statement
     */
    private void visitAssign(Assign assign) {
        appendIndent();
        append(assign.ident);
        append(" := ");
        visitExpr(assign.expr);
        append(";\n");
    }

    /**
     * Visit an if statement
     */
    private void visitIf(If ifStmt) {
        appendIndent();
        append("if (");
        visitExpr(ifStmt.cond);
        append(") then {\n");

        indent();
        visitStmt(ifStmt.then);
        dedent();

        appendLine("}");

        if (ifStmt.els != null) {
            appendLine("else {");
            indent();
            visitStmt(ifStmt.els);
            dedent();
            appendLine("}");
        }
    }

    /**
     * Visit a while statement
     */
    private void visitWhile(While whileStmt) {
        appendIndent();
        append("while (");
        visitExpr(whileStmt.cond);
        append(") do {\n");

        indent();
        visitStmt(whileStmt.stmt);
        dedent();

        appendLine("}");
    }

    /**
     * Visit an expression
     */
    private void visitExpr(Expr expr) {
        if (expr == null) {
            append("null");
            return;
        }

        if (expr instanceof BinExpr) {
            visitBinExpr((BinExpr) expr);
        } else if (expr instanceof UnExpr) {
            visitUnExpr((UnExpr) expr);
        } else if (expr instanceof IntVal) {
            append(String.valueOf(((IntVal) expr).value));
        } else if (expr instanceof DoubleVal) {
            append(String.valueOf(((DoubleVal) expr).val));
        } else if (expr instanceof BoolVal) {
            append(String.valueOf(((BoolVal) expr).value));
        } else if (expr instanceof CharVal) {
            append("'" + ((CharVal) expr).val + "'");
        } else if (expr instanceof Ident) {
            append(((Ident) expr).name);
        } else if (expr instanceof ParenExpr) {
            append("(");
            visitExpr(((ParenExpr) expr).expr);
            append(")");
        } else {
            append("/* Unknown expression */");
        }
    }

    /**
     * Visit a binary expression
     */
    private void visitBinExpr(BinExpr binExpr) {
        // Add parentheses around nested binary expressions
        boolean needsParens = binExpr.left instanceof BinExpr &&
                getPrecedence(((BinExpr) binExpr.left).op) < getPrecedence(binExpr.op);

        if (needsParens) append("(");
        visitExpr(binExpr.left);
        if (needsParens) append(")");

        append(" " + binOpToString(binExpr.op) + " ");

        needsParens = binExpr.right instanceof BinExpr &&
                getPrecedence(((BinExpr) binExpr.right).op) <= getPrecedence(binExpr.op);

        if (needsParens) append("(");
        visitExpr(binExpr.right);
        if (needsParens) append(")");
    }

    /**
     * Visit a unary expression
     */
    private void visitUnExpr(UnExpr unExpr) {
        append(unOpToString(unExpr.op));

        // Add parentheses if the operand is a binary expression
        boolean needsParens = unExpr.expr instanceof BinExpr;

        if (needsParens) append("(");
        visitExpr(unExpr.expr);
        if (needsParens) append(")");
    }

    /**
     * Convert a binary operator to its string representation
     */
    private String binOpToString(Binoperator op) {
        switch (op) {
            case ADD: return "+";
            case MINUS: return "-";
            case TIMES: return "*";
            case DIV: return "/";
            case MODULO: return "%";
            case EQUAL: return "==";
            case NEQUAL: return "!=";
            case LT: return "<";
            case LEQ: return "<=";
            case GT: return ">";
            case GEQ: return ">=";
            case AND: return "&&";
            case OR: return "||";
            case ELMULT: return ".*"; // Element-wise multiplication
            default: return "?";
        }
    }

    /**
     * Convert a unary operator to its string representation
     */
    private String unOpToString(Unaryoperator op) {
        switch (op) {
            case NOT: return "!";
            case NEG: return "-";
            default: return "?";
        }
    }

    /**
     * Get the precedence of a binary operator
     * Higher values mean higher precedence
     */
    private int getPrecedence(Binoperator op) {
        switch (op) {
            case OR: return 1;
            case AND: return 2;
            case EQUAL:
            case NEQUAL: return 3;
            case LT:
            case LEQ:
            case GT:
            case GEQ: return 4;
            case ADD:
            case MINUS: return 5;
            case TIMES:
            case DIV:
            case MODULO:
            case ELMULT: return 6;
            default: return 0;
        }
    }

    /**
     * Convert a type to its string representation
     */
    private String printType(Type type) {
        if (type == null) {
            return "unknown_type";
        }

        if (type instanceof SimpleType) {
            SimpleTypesEnum simpleType = ((SimpleType) type).type;

            switch (simpleType) {
                case INT: return "int";
                case BOOL: return "bool";
                case CHAR: return "char";
                case DOUBLE: return "double";
                default: return "unknown_simple_type";
            }
        } else if (type instanceof TensorType) {
            // We're not handling tensor types for now
            return "tensor_type";
        } else {
            return "unknown_type";
        }
    }
}