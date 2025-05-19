package AbstractSyntax.Expressions;
import java.util.ArrayList;
import java.util.HashMap;

/* Created 09-05-2025 by Nikolaj
 * 
 * 
 * 
 */

public class FuncCallExpr implements Expr{
    public ArrayList<Expr> ActualParameters = new ArrayList<Expr>();
    public String name;
    public HashMap<String, String> gamma;

    public FuncCallExpr(ArrayList<Expr> ActualParameters,String name){
        if(!(ActualParameters == null)){
            for (Expr expr : ActualParameters) {
                this.ActualParameters.add(expr);
            }
        }
        this.name = name;
        this.gamma = new HashMap<String,String>();
    }
}
