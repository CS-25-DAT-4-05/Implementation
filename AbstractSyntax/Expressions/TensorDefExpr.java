package AbstractSyntax.Expressions;

import java.util.ArrayList;

/* Created 09-05-2025 by Nikolaj
 * 
 * 
 * 
 */

public class TensorDefExpr implements Expr {
    ArrayList<Expr> exprs = new ArrayList<Expr>();

    public TensorDefExpr(ArrayList<Expr> exprs){
        for (Expr expr : exprs) {
            this.exprs.add(expr);
        }
    }
}
