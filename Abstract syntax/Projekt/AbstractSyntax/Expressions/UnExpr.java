package AbstractSyntax.Expressions;
public class UnExpr implements Expr{
    Unaryoperator op;
    Expr expr;

    public UnExpr(Expr expr, Unaryoperator op){
        this.op = op;
        this.expr = expr;
    }
}
