package AbstractSyntax.Statements;
import AbstractSyntax.Expressions.*;

/* Created 09-05-2025 by Nikolaj
 * 
 * 
 * 
 */

public class Assign implements Stmt{
    public String ident;
    public Expr expr;
    public Assign(String ident, Expr expr){
        this.ident = ident;
        this.expr = expr;
    }
}
