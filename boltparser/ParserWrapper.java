//package boltparser; //If you move this file to boltparser, un-comment package boltparser;
//java.util.Scanner; //Messes with boltparser.Scanner. Use 1 or the other.
package boltparser;

import java.io.IOException;
import boltparser.Scanner;
//import java.nio.file.Files;
//import java.nio.file.Paths;
import AbstractSyntax.Program.Prog;

public class ParserWrapper {
    public static Prog parse(String path) throws IOException {
        // Use the file path directly — boltparser.Scanner only accepts a String
        // Do NOT declare 'Scanner' twice
        boltparser.Scanner boltscanner = new boltparser.Scanner(path);

        Parser parser = new Parser(boltscanner);

        parser.Program();

        return parser.programAST; // Assuming programAST is a valid field in your parser



        // Return the root AST node
        //return parser.Program();  // This is good if and only if your Program() method returns a Stmt. If no
    }
}



/* 
public class ParserWrapper {
    public static Stmt parse(String path) throws Exception {
        Scanner scanner = new Scanner(path); //pass path directly
        Parser parser = new Parser(scanner);
        return parser.Program(); //Return the root AST Node
    }
}
*/

