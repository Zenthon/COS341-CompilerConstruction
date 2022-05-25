import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Please enter the the input file name with the extension (.txt): ");
            String inputFile = sc.nextLine();

            // Start the lexer
            Lexer lexer = new Lexer(Files.readString(Path.of(inputFile)));
            lexer.process();

            // Start the parser
            Parser parser = new Parser(lexer.tokens);
            parser.parseGrammar();

            // Start the scope analyser
            ScopeAnalyzer scopeAnalyzer = new ScopeAnalyzer(lexer.tokens);
            scopeAnalyzer.startScopeAnalysis();
            scopeAnalyzer.print();

        }
        catch (IOException e) {
            System.out.println("Failed to read file to string.");
        } catch (ParserException  | LexicalException | SemanticException e) {
            System.out.println(e.getMessage());
        }
    }
}