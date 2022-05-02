import java.io.IOException;
import java.nio.file.*;

public class Main {

    public static void main(String[] args) {
        try {
            // Start of the lexer
            Lexer lexer = new Lexer(Files.readString(Path.of("input.txt")));
            lexer.process();

            // Check if lexer has errors. If not, get the tokens
            if (!lexer.isHasErrors())
                for (Token t : lexer.tokens)
                    System.out.println(t.tokenFormat());
        }
        catch (IOException e) {
            System.out.println("Failed to read file to string.");
        }
    }
}
