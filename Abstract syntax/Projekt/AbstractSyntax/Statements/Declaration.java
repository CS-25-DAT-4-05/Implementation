package AbstractSyntax.Statements;
import AbstractSyntax.Types.Type;
import AbstractSyntax.Expressions.Expr;

/* Created 09-05-2025 by Nikolaj
 * 
 * 
 * 
 */

public class Declaration implements Stmt {
    Type t;
    String ident;
    Expr expr;
    Stmt stmt;
    public Declaration(Type t, String ident, Expr expr, Stmt stmt){
        this.t = t;
        this.ident = ident;
        this.expr = expr;
        this.stmt = stmt;
    }

    public Expr getExpr() {
        return expr;
    }
    public String getIdent() {
        return ident;
    }
    public Stmt getStmt() {
        return stmt;
    }
    public Type getT() {
        return t;
    }
}
