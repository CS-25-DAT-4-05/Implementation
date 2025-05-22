package AbstractSyntax.Statements;
import AbstractSyntax.Expressions.Expr;
import AbstractSyntax.Types.Type;

/* Created 09-05-2025 by Nikolaj
 * 
 * 
 * 
 */

public class Declaration implements Stmt {
    public Type type;
    public String ident;
    public Expr expr;
    public Stmt stmt;
    
    public Declaration(Type type, String ident, Expr expr, Stmt stmt){
        this.type = type;
        this.ident = ident;
        this.expr = expr;
        this.stmt = stmt;
    }
}
