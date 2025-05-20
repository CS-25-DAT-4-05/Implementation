package boltparser;

//package boltparser; //If you move this file to boltparser, un-comment package boltparser;
import java.io.FileReader;
import java.io.Reader;

import boltparser.Parser;
import boltparser.Scanner;
import AbstractSyntax.Statements.Stmt;

public class ParserWrapper {
    public static Stmt parse(String path) throws Exception {
        Scanner scanner = new Scanner(path); //pass path directly
        Parser parser = new Parser(scanner);
        return parser.mainNode(); //Return the root AST Node
    }
}


