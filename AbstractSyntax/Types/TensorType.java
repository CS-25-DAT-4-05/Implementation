package AbstractSyntax.Types;
import AbstractSyntax.SizeParams.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import javax.management.openmbean.SimpleType;

public class TensorType implements Type {
    public SimpleType componentType;
    public ArrayList<SizeParam> dimensions;

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

    private ArrayList<Integer> shape;
    public ArrayList<Integer> shape() { return shape; }

}

