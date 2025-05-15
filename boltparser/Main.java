package boltparser;

import AbstractSyntax.Statements.Stmt;

public class Main {
    public static void main(String[] args) {
        System.out.println("Working Directory: " + System.getProperty("user.dir"));

        if (args.length < 1) {
            System.out.println("Usage: java boltparser.Main <filename>");
            return;
        }

        String fileName = args[0];
        System.out.println("Attempting to parse file: " + fileName);

        try {
            // Create a scanner for the input file
            Scanner scanner = new Scanner(fileName);
            System.out.println("Scanner created successfully");

            // Create a parser using the scanner
            Parser parser = new Parser(scanner);
            System.out.println("Parser created successfully");

            // Parse the input
            System.out.println("Starting parsing...");
            parser.Parse();
            System.out.println("Parsing completed");

            if (parser.hasErrors()) {
                System.out.println("Errors occurred during parsing!");
            } else {
                Stmt program = parser.mainNode;
                if (program != null) {
                    System.out.println("Parsing successful!");
                    System.out.println("AST root: " + program.getClass().getSimpleName());

                    // Use the PrettyPrinter to print the AST as code
                    PrettyPrinter prettyPrinter = new PrettyPrinter();
                    String prettyPrinted = prettyPrinter.print(program);

                    System.out.println("\nPretty printed code:");
                    System.out.println("-------------------");
                    System.out.println(prettyPrinted);
                    System.out.println("-------------------");
                } else {
                    System.out.println("No program was parsed. mainNode is null.");
                }
            }
        } catch (Exception e) {
            System.out.println("Exception during parsing: " + e.getMessage());
            e.printStackTrace();
        }
    }
}