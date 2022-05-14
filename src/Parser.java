import java.util.List;

public class Parser {

    private List<Token> tokens;
    private final String xmlString;
    private boolean hasErrors = false;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        xmlString = null;
    }

    public boolean getHasErrors() {
        return hasErrors;
    }

}
