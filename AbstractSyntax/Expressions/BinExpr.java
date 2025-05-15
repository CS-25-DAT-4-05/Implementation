package AbstractSyntax.Expressions;
/* Created 09-05-2025 by Nikolaj
 * 
 * 
 * 
 */

public class BinExpr implements Expr{
    Expr left,right;
    Binoperator op;

    public BinExpr(Expr left, Expr right,Binoperator op){
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public Expr getLeft() {
        return left;
    }

    public Expr getRight(){
        return right;
    }

    public Binoperator getOp() {
        return op;
    }
}