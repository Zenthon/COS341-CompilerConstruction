public class SemanticException extends Exception {

    private final String message;

    public SemanticException(String msg) {
        message = msg;
    }

    public String getMessage() {
        return "Semantic Error: " + message;
    }
}