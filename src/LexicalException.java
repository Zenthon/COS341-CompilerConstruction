public class LexicalException extends Exception {

    private final String message;

    public LexicalException(String msg) {
        message = msg;
    }

    public String getMessage() {
        return "Lexical Error: " + message;
    }
}