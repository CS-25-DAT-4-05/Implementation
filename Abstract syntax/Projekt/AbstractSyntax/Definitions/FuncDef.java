package AbstractSyntax.Definitions;
import  java.util.ArrayList;

import AbstractSyntax.Statements.Stmt;
import AbstractSyntax.Types.Type;
import AbstractSyntax.Expressions.Expr;
import Lib.Pair;

/* Created 09-05-2025 by Nikolaj
 * 
 * 
 * 
 */

public class FuncDef {
    Type returnType;
    String procname;
    ArrayList<Pair<Type,String>> formalParams = new ArrayList<Pair<Type,String>>();
    Stmt funcBody;
    Expr returnExpr;
    FuncDef nextFunc;

    public FuncDef(Type returnType,String procname,ArrayList<Pair<Type,String>> formalParams,Stmt funcBody,Expr returnExpr,FuncDef nextFunc){
        this.returnExpr = returnExpr;
        this.returnType = returnType;
        this.procname = procname;
        if(!(formalParams == null)){
            for (Pair<Type,String> param : formalParams) {
                this.formalParams.add(param);
            }
        }
        this.funcBody = funcBody;
        this.nextFunc = nextFunc;
    }
    public Type getReturnType(){
        return returnType;
    }
    public String getProcName(){
        return procname;
    }

    public FuncDef getNexFuncDef(){
        return nextFunc;
    }

    public Stmt getBody(){
        return funcBody;
    }

    public Expr getReturnExpr(){
        return returnExpr;
    }

    public ArrayList<Pair<Type,String>> getFormalParams(){
        return this.formalParams;
    }
}
