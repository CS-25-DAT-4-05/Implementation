package AbstractSyntax.Expressions;

/* Created 09-05-2025 by Nikolaj
 * 
 * 
 * 
 */
public class IntVal implements Expr {
    int value;
    public IntVal(int value){
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}
