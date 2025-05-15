package AbstractSyntax.Program;

import AbstractSyntax.Definitions.FuncDef;

/* Created 09-05-2025 by Nikolaj
 * 
 * 
 * 
 */

public class Prog {
    FuncDef func;
    public Prog(FuncDef func){
        this.func = func;
    }

    public FuncDef getFuncDef(){
        return func;
    }
}
