package AbstractSyntax.Types;

//import java.lang.reflect.Type;
import AbstractSyntax.SizeParams.SizeParam;
import java.util.List;
    
    
public class TensorType implements Type {
    public Type componentType;
    public List<SizeParam> dimensions; 
    //public List<SizeParam> dimensions;
    //private final Type base;
    //private final ArrayList<Integer> shape;

    public TensorType(Type componentType, List<SizeParam> dimensions) {
        this.componentType = componentType;
        this.dimensions = dimensions;
    }

    public List<SizeParam> getDimensions() {
    return dimensions;
    }

    // Vector type check
    public boolean isVector() {
        return dimensions != null && dimensions.size() == 1;
    }

   // Matrix type check
    public boolean isMatrix() {
        return dimensions != null && dimensions.size() == 2;
    }

    public List<SizeParam> shape() {
        return dimensions;
    }

    public Type base() {
        return componentType;
    }
    
    //public String toString() {
    //    return "tensor<" + componentType.toString() + ", " + dimensions.size() + "D>" 8=====D;


    @Override
    public String toString() {
        return "TensorType(" + componentType + ", " + dimensions + ")";
    }
}

    /* 
    public TensorType(SimpleType compT, ArrayList<SizeParam> dim) {
        this.componentType = compT;
        this.dimensions = new ArrayList<>();
        if (dim != null) {
            this.dimensions.addAll(dim);
        }
    }
    
   // Vector type check
    public boolean isVector() {
        return dimensions != null && dimensions.size() == 1;
    }

   // Matrix type check
    public boolean isMatrix() {
        return dimensions != null && dimensions.size() == 2;
    }

    // Tensor type check
    public boolean isHigherDimensionalTensor() {
        return dimensions != null && dimensions.size() >= 3;
    }

    //private ArrayList<Integer> shape;
    //public ArrayList<Integer> shape() { return shape; }
    */

