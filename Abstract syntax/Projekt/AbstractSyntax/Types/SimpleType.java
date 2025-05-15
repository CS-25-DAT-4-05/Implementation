package AbstractSyntax.Types;

/* Created 09-05-2025 by Nikolaj
 * 
 * 
 * 
 */

public class SimpleType implements Type {
    SimpleTypesEnum type;
    public SimpleType(SimpleTypesEnum T){
        type = T;
    }
    public SimpleTypesEnum getType(){
        return type;
    }
}
