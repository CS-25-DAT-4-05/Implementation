//package Implementation;

//port AbstractSyntax.Statements.Stmt;
import AbstractSyntax.Program.Prog;
import Semantic.TypeChecker;
import boltparser.ParserWrapper;
//import java.nio.file.Files;
//import java.nio.file.Paths;

public class TestTypeChecker {
    public static void  check(Prog prog) {
        try {
            Prog stmt = ParserWrapper.parse("Implementation/CocoR/test.bolt");
            TypeChecker checker = new TypeChecker();
            checker.check(prog);
            System.out.println("Type checking passed.");
        } catch (Exception e) {
            System.err.println("Type checking failed:");
            e.printStackTrace();
        }
    }
}

/* 
import AbstractSyntax.Statements.Stmt;
import Semantic.TypeChecker;
import boltparser.ParserWrapper;

public class TestTypeChecker {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java TestTypeChecker <inputfile>");
            return;
        }

        String path = args[0];
        try {
            Stmt prog = ParserWrapper.parse(path);   // Step 1 — parse into AST
            TypeChecker checker = new TypeChecker(); // Step 2 — create checker
            checker.check(prog);                     // Step 3 — run checker

            // Print errors
            if (checker.errors.isEmpty()) {
                System.out.println("No type errors!");
            } else {
                System.out.println("Type errors found:");
                for (String error : checker.errors) {
                    System.out.println(error);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/