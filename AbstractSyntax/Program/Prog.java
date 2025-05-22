package AbstractSyntax.Program;

import AbstractSyntax.Definitions.FuncDef;
import AbstractSyntax.Statements.Stmt;


/* Created 09-05-2025 by Nikolaj
 * Program representation for BOLT language
 * Represents: P ∈ Prog ::= Df
 * A program consists of a chain of function definitions
 */

public class Prog implements Stmt{
    public FuncDef func;  // First function in the chain

    public Prog(FuncDef func) {
        this.func = func;
    }
}
