package AbstractSyntax.Expressions;

import AbstractSyntax.Types.Type;


public class TensorLiteral implements Expr {
    public final Type type; // inferred during parsing
    public TensorLiteral(Type type) {
        this.type = type;
    }
}
