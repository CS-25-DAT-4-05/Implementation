package Implementation;

import AbstractSyntax.Program.Prog;
import AbstractSyntax.Statements.Stmt;

import Semantic.TypeChecker; 
import boltparser.ParserWrapper; //correct if ParserWrapper.java is in boltparser folder. If in main folder: "Implementation.ParserWrapper;""

public class TestTypeChecker {
    public static void main(String[] args) {
        try {
            String path = "CocoR/test.bolt";
            Stmt prog = ParserWrapper.parse(path);
            runTypeCheckerTest(prog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runTypeCheckerTest(Prog prog) {
        TypeChecker checker = new TypeChecker();
        checker.check(prog); //Stmt is the correct root..
        System.out.println("Type checking passed!");
    }
}
