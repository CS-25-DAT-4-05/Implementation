package AbstractSyntax.Statements;

import AbstractSyntax.Expressions.Expr;

public class ReturnStmt implements Stmt {
    public final Expr expr;
    public ReturnStmt(Expr expr) {
        this.expr = expr;
    }
}
