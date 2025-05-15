package AbstractSyntax.Types;
import java.util.ArrayList;
import AbstractSyntax.SizeParams.*;

/* Created 09-05-2025 by Nikolaj
 * 
 * 
 * 
 */

public class TensorType implements Type {
    SimpleType componentType;
    ArrayList<SizeParam> dimensions = new ArrayList<SizeParam>();
    public TensorType(SimpleType compT, ArrayList<SizeParam> dim){
        for (SizeParam sizeParam : dim) {
            dimensions.add(sizeParam);
        }
        componentType = compT;
    }
}
