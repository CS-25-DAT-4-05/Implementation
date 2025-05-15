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
            addPrototype(fWriter, root.getFuncDef());
            fWriter.write("\n");
            transpileDef(fWriter, root.getFuncDef());
        } catch(Exception e){

        }

    }
    static void transpileStmt(FileWriter fWriter,Stmt s) throws Exception{
        String ident;
        String expr;
        switch (s) {
            case null:
                fWriter.append("");
                break;
            case Assign a:
                ident = a.getIdent();
                expr = transpileExpr( a.getExpr());
                fWriter.append(ident + " = " + expr + ";\n");
                break;
            case Declaration d:
                String type = boltToCudaTypeConverter(d.getT());
                ident = d.getIdent();
                expr = transpileExpr(d.getExpr());
                fWriter.append(type + " " + ident + " = " + expr + ";\n");
                transpileStmt(fWriter, d.getStmt());
                break;
            default:  
                break;
        }
    } 

    static String transpileExpr(Expr e){
        switch (e) {
            case BinExpr be:
                String e1 = transpileExpr(be.getLeft());
                String e2 = transpileExpr(be.getRight());
                return e1 +  getBinOp(be.getOp()) + e2;
            case IntVal iv:
                return "" + iv.getValue();    
            default:
                return "";
        }
    }

    static void transpileDef(FileWriter fileWriter,FuncDef f) throws Exception{
        printFunctionHeader(fileWriter, f);
        fileWriter.append("{\n");
        transpileStmt(fileWriter, f.getBody());
        String returnExpr = transpileExpr(f.getReturnExpr());

        fileWriter.append("return "+returnExpr + ";\n");
        fileWriter.append("}\n");
        transpileDef(fileWriter, f.getNexFuncDef());
    }

    
    //#### Auxiliary functions ########################################
    static void printFunctionHeader(FileWriter fWriter, FuncDef f) throws Exception{
        String rtype = boltToCudaTypeConverter(f.getReturnType());
        String procName = f.getProcName();
        String paramters = "";
        StringBuilder sb = new StringBuilder();
        if(f.getFormalParams() != null){
            for (Pair<Type,String> p : f.getFormalParams()) {
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

        if(f.getProcName().equals("main")){
            hasMain = true;
            addPrototype(fWriter,f.getNexFuncDef());
        }
        else{
            printFunctionHeader(fWriter, f);
            fWriter.append(";\n");
            addPrototype(fWriter, f.getNexFuncDef());
        }
    }

    static String boltToCudaTypeConverter(Type t) throws Exception{
        switch (t) {
            case SimpleType st:
                switch (st.getType()) {
                    case INT:
                        return "int";
                    case BOOL:
                        return "bool";
                    case CHAR:
                        return "char";
                    case DOUBLE:
                        return "double";
                    default:
                        return "void";  
                }
            case TensorType ct:
                return "tensor"; //Needs to be changed
            default:
                throw new Exception("Unrecognized type");
        }

    }

    static char getBinOp(Binoperator bin){
        switch (bin) {
            case Binoperator.ADD:
                return '+';    
            default:
                return '?';
        }
    }
    
}