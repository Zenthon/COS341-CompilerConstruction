public class ParserException extends Exception {

    private final String message;

    public ParserException(String msg) {
        message = msg;
    }

    public String getMessage() {
        return "Syntax Error: " + message;
    }
}