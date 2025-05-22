package AbstractSyntax.Expressions;

public class Variable implements Expr {
    public final String ident;
    public Variable(String ident) {
        this.ident = ident;
    }
}
