package AbstractSyntax.Statements;
import AbstractSyntax.Expressions.*;

/* Created 09-05-2025 by Nikolaj
 * 
 * 
 * 
 */

public class Assign implements Stmt{
    String ident;
    Expr expr;
    public Assign(String ident, Expr expr){
        this.ident = ident;
        this.expr = expr;
    }

    public String getIdent() {
        return ident;
    }
    
    public Expr getExpr() {
        return expr;
    }
}
