import AbstractSyntax.Expressions.*;

import java.util.ArrayList;

import AbstractSyntax.Definitions.*;
import AbstractSyntax.SizeParams.*;
import AbstractSyntax.Types.*;
import Lib.Pair;
import Transpiler.Transpiler;
import AbstractSyntax.Program.*;
import AbstractSyntax.Statements.*;

public class Test {


    public static void main(){
        Prog root;
        FuncDef func1;
        Assign assign1;
        BinExpr bexp;
        Ident returnExp;
        FuncDef mainFunc;
        Ident ident;
        Assign assign2;
        IntVal zero;
        Declaration dec1;

        bexp = new BinExpr(new IntVal(1), new IntVal(2), Binoperator.ADD);
        zero = new IntVal(0);
        
        
        assign2 = new Assign("x", bexp);
        dec1 = new Declaration(new SimpleType(SimpleTypesEnum.INT), "x", new IntVal(3),assign2 );


        ArrayList<Pair<Type,String>> params = new ArrayList<Pair<Type,String>>();
        params.add(new Pair<Type,String>(new SimpleType(SimpleTypesEnum.INT),"n"));

        func1 = new FuncDef(new SimpleType(SimpleTypesEnum.DOUBLE), "foo", params, dec1, zero,null);

        mainFunc = new FuncDef(new SimpleType(SimpleTypesEnum.INT), "main",null, null, zero, func1);



        root = new Prog(mainFunc);
        Transpiler.TranspileProg(null, root);
    }
}
