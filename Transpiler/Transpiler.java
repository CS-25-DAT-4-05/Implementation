package Transpiler;

//Abstract syntax
import AbstractSyntax.Expressions.*;
import AbstractSyntax.Definitions.*;
import AbstractSyntax.SizeParams.*;
import AbstractSyntax.Types.*;
import AbstractSyntax.Program.*;
import AbstractSyntax.Statements.*;

//Helper libraries
import Lib.*;

//Java libraries
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Transpiler {
    static boolean hasMain = false;

    static HashMap<String,Defer> deferMap;

    static boolean isRoot = false;

    public static void TranspileProg(String fileName, Prog root){
        //Initialization
        Ftable ftable = new Ftable();
        File outputFile;

        if(fileName == null){
            fileName = "a.cu";
        }else{
            fileName = fileName + ".cu";
        }
        outputFile = new File(fileName);

        try(FileWriter fWriter = new FileWriter(outputFile)){
            if(!(root instanceof Prog)){
                throw new Exception("Incorrect root for abstract syntax tree");
            }
            addPrototype(fWriter, root.func);
            fWriter.write("\n");
            transpileDef(fWriter, root.func);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    static void transpileStmt(FileWriter fWriter,Stmt s) throws Exception{
        String ident;
        String expr;
        String cond;
        switch (s) {
            case null:
                fWriter.append("");
                break;
            case Assign asgn:
                ident = asgn.ident;
                expr = transpileExpr( asgn.expr);
                fWriter.append(ident + " = " + expr + ";\n");
                break;
            case Declaration dec:
                String type = boltToCudaTypeConverter(dec.t);
                ident = dec.ident;
                expr = transpileExpr(dec.expr);
                fWriter.append(type + " " + ident + " = " + expr + ";\n");
                transpileStmt(fWriter, dec.stmt);
                break;
            case Comp cmp:
                transpileStmt(fWriter, cmp.stmt1);
                transpileStmt(fWriter, cmp.stmt2);
                break;
            case If ife:
                cond = transpileExpr(ife.cond);
                fWriter.append("if(" + cond + "){\n");
                transpileStmt(fWriter, ife.then);
                fWriter.append("}\n");
                if(ife.els != null){
                    fWriter.append("else{\n");
                    transpileStmt(fWriter, ife.els);
                    fWriter.append("}\n");
                }
                break;
            case While wh:
                cond = transpileExpr(wh.cond);
                fWriter.append("while()" + cond + "){\n");
                transpileStmt(fWriter, wh.stmt);
                fWriter.append("}\n");
                break;
            case Defer df:
                
                break;
            default:  
                break;
        }
    } 

    static String transpileExpr(Expr e) throws Exception{
        StringBuilder sb = new StringBuilder();
        switch (e) {
            case BinExpr be:
                String e1 = transpileExpr(be.left);
                String e2 = transpileExpr(be.left);
                return e1 +  getBinOp(be.op) + e2;
            case IntVal iv:
                return "" + iv.value;
            case BoolVal bv:
                return "" + bv.value;
            case CharVal cv:
                return "" + cv.val;
            case DoubleVal dv:
                return "" + dv.val;
            case Ident id:
                return id.name;
            case ParenExpr pe:
                return "("+ transpileExpr(pe.expr)+")";
            case UnExpr ue:
                return getUnOp(ue.op) + transpileExpr(ue.expr);
            case FuncCallExpr func:
                String params = "";
                if(func.ActualParameters != null){
                    for (Expr exp : func.ActualParameters) {
                        if(sb.length() != 0){
                            sb.append(",");
                        }
                        sb.append(transpileExpr(exp));
                    }
                    params = sb.toString();
                }
                return func.name + "(" + params + ")";
            case TensorAccessExpr tae:
                for(Expr exp: tae.indices){
                    if(sb.length() != 0){
                        sb.append(',');
                    }
                    sb.append(transpileExpr(exp));
                }
                String indices = sb.toString();
                return transpileExpr(tae.listExpr) + ".access({" + indices + "})";
            case TensorDefExpr tde:
                ArrayList<Integer> dim = new ArrayList<>();
                getDim(tde, dim);
                StringBuilder sbDim = new StringBuilder("{");
                for (int integer : dim.reversed()) {
                    if(sbDim.length()!=1){
                        sbDim.append(",");
                    }
                    sbDim.append(integer);
                }
                sbDim.append("}");
                
                return "";
            default:
                return "";
        }
    }

    static void getDim(Expr e,ArrayList<Integer> dim){
        switch (e) {
            case TensorDefExpr tde:
                dim.add(tde.exprs.size());
                getDim(tde.exprs.get(0), dim);
                break;
            default:
                return;
        }
    }    


    static void transpileDef(FileWriter fileWriter,FuncDef f) throws Exception{
        printFunctionHeader(fileWriter, f);
        fileWriter.append("{\n");
        transpileStmt(fileWriter, f.funcBody);
        String returnExpr = transpileExpr(f.returnExpr);

        fileWriter.append("return "+returnExpr + ";\n");
        fileWriter.append("}\n");
        transpileDef(fileWriter, f.nextFunc);
    }

    
    //#### Auxiliary functions ########################################
    static void printFunctionHeader(FileWriter fWriter, FuncDef f) throws Exception{
        String rtype = boltToCudaTypeConverter(f.returnType);
        String procName = f.procname;
        String paramters = "";
        StringBuilder sb = new StringBuilder();
        if(f.formalParams != null){
            for (Pair<Type,String> p : f.formalParams) {
                if(!(sb.length() == 0)){
                    sb.append(',');
                }
                sb.append(boltToCudaTypeConverter(p.getElem1()));
                sb.append(" ");
                sb.append(p.getElem2());
            }
            paramters = sb.toString();
        }
        String writeString = rtype + " " + procName + "(" + paramters +  ")";
        fWriter.append(writeString);
    }


    static void addPrototype(FileWriter fWriter,FuncDef f) throws Exception{
        if(f == null){
            return;
        }

        if(f.procname.equals("main")){
            hasMain = true;
            addPrototype(fWriter,f.nextFunc);
        }
        else{
            printFunctionHeader(fWriter, f);
            fWriter.append(";\n");
            addPrototype(fWriter, f.nextFunc);
        }
    }

    static String boltToCudaTypeConverter(Type t) throws Exception{
        switch (t) {
            case SimpleType st:
                switch (st.type) {
                    case INT:
                        return "int";
                    case BOOL:
                        return "bool";
                    case CHAR:
                        return "char";
                    case DOUBLE:
                        return "double";
                    default:
                        throw new Exception("Unrecognized type");
                }
            case TensorType ct:
                switch (ct.componentType.type) {
                    case INT:
                        return "IntTensor";
                    case DOUBLE:
                        return "DoubleTensor";
                    default:
                        throw new Exception("Unrecognized type");
                }
            default:
                throw new Exception("Unrecognized type");
        }

    }

    static String getBinOp(Binoperator bin) throws Exception{
        switch (bin) {
            case ADD:
                return "+";
            case MINUS:
                return "-";
            case TIMES:
                return "*";
            case MODULO:
                return "%";
            case EQUAL:
                return "==";
            case NEQUAL:
                return "!=";
            case DIV:
                return "/";
            case LEQ:
                return "<=";
            case LT:
                return "<";
            case GT:
                return ">";
            case GEQ:
                return ">=";
            case OR:
                return "||";
            case AND:
                return "&&";
            case ELMULT:
                return "<<";    
            default:
                throw new Exception("invalid operator");
        }
    }

    static char getUnOp(Unaryoperator op) throws Exception{
        switch (op) {
            case NOT:
                return '!';
            case NEG:
                return '-';
            default:
                throw new Exception("invalid operator");
        }
    }
    
}