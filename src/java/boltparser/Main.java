package boltparser;

import AbstractSyntax.Program.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Main <input-file>");
            return;
        }

        String filename = args[0];
        System.out.println("Working Directory: " + System.getProperty("user.dir"));
        System.out.println("Attempting to parse file: " + filename);

        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.err.println("Error: File does not exist: " + filename);
                return;
            }

            // Create scanner
            Scanner scanner = new Scanner(filename);
            System.out.println("Scanner created successfully");

            // Create parser
            Parser parser = new Parser(scanner);
            System.out.println("Parser created successfully");

            // Parse the input
            System.out.println("Starting parsing...");
            parser.Parse();
            System.out.println("Parsing completed");

            if (parser.hasErrors()) {
                System.out.println("Errors occurred during parsing!");
                return;
            }

            // Print the AST
            System.out.println("\n=== Abstract Syntax Tree - Program Structure ===\n");
            Prog ast = parser.mainNode;  // Changed from Stmt to Prog
            if (ast == null) {
                System.out.println("No AST generated (empty program)");
            } else {
                AstPrinter printer = new AstPrinter();
                System.out.println(printer.printProgram(ast));  // Use new method for Prog
            }

        } catch (Exception e) {
            System.err.println("Error during parsing: " + e.getMessage());
            e.printStackTrace();
        }
    }
}