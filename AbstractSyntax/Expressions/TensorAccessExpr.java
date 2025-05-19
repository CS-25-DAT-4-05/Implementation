package AbstractSyntax.Expressions;
/* Created 09-05-2025 by Nikolaj
 * 
 * 
 * 
 */
import java.util.ArrayList;
public class TensorAccessExpr implements Expr {
    public Expr listExpr;
    public ArrayList<Expr> indices = new ArrayList<Expr>();
    public TensorAccessExpr(Expr listExpr,ArrayList<Expr> indices){
        for (Expr expr : indices) {
            this.indices.add(expr);
        }
        this.listExpr = listExpr;
    }
}
