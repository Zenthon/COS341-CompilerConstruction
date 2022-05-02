import java.util.LinkedList;
import java.util.List;


public class Lexer {
    private final String input;
    private String str = "";

    private int state = 0;
    private int currentPosition = 0;
    private static int lineNumber = 1;
    private int numQuoteMarks = 0;

    private boolean hasErrors = false;

    public List<Token> tokens  = new LinkedList<>();;
    public int tokenId = 1;
    private final char[] alphabet = "\"-{}()[],:;=".toCharArray();

    public Lexer(String inputText) {
        inputText = inputText.replaceAll("[}]", " } ");
        inputText = inputText.replaceAll("[{]", " { ");
        inputText = inputText.replaceAll("[(]", " ( ");
        inputText = inputText.replaceAll("[)]", " ) ");
        inputText = inputText.replaceAll("[\\[]", " [ ");
        inputText = inputText.replaceAll("[\\]]", " ] ");
        inputText = inputText.replaceAll("[,]", " , ");
        inputText = inputText.replaceAll("[;]", " ; ");
        inputText = inputText.replaceAll("[:]", " :");
        inputText = inputText.replaceAll("[=]", "= ");
        this.input = inputText;
    }

    public boolean isHasErrors() { return hasErrors; }

    public void process() {
        for (int i=0;   i<input.length() && !hasErrors;   i++) {
            if (input.charAt(i) == '\r' && input.charAt(i+1) == '\n') {
                lineNumber++;
                i++;
                try {   checkToken();  }
                catch (Exception e) {
                    hasErrors = true;
                    System.out.println(e.getMessage());
                }
                currentPosition = state = 0;
            }
            else if (Character.isWhitespace(input.charAt(i)) && (numQuoteMarks == 0 || numQuoteMarks == 2)) {
                try {  checkToken();   }
                catch (Exception e) {
                    hasErrors = true;
                    System.out.println(e.getMessage());
                }
                state = 0;
            }
            else {
                try {
                    currentPosition ++;
                    state = delta(input.charAt(i));
                }
                catch (Exception e) {
                    hasErrors = true;
                    System.out.println(e.getMessage());
                    tokens.clear();
                }
            }
        }

        if (!hasErrors) {
            try {
                checkToken();
            } catch (Exception e) {
                hasErrors = true;
                System.out.println(e.getMessage());
                tokens.clear();
            }
        }
    }

    public int delta(char c) throws Exception{
        if (Character.isLetterOrDigit(c) || isInAlphabet(c, alphabet) || Character.isWhitespace(c)) {
            str += c;
            switch (state) {
                case 0:
                    if (Character.isLowerCase(c))  return 3;
                    if (c == ':') return 4;
                    if (c == '0' || c == '{' || c == '}' || c == '(' || c == ')' || c == '[' || c == ']' || c == ',' || c == ';') return 2;
                    if (c == '\"') {
                        numQuoteMarks = 1;
                        return 7;
                    }
                    if (c == '-') return 5;
                    if (Character.isDigit(c)) return 1;
                    throw new LexicalException("No token starts with the character '" + c + "' on line " + lineNumber + " position " + currentPosition);
                case 1:
                    if (Character.isDigit(c)) return 1;
                    throw new LexicalException("The character '" + c + "' on line " + lineNumber + " position " + currentPosition + " is not a digit");
                case 3:
                    if (Character.isDigit(c) || Character.isLowerCase(c)) return 3;
                    throw new LexicalException("User defined name cannot contain the character '" + c + "' on line " + lineNumber + " position " + currentPosition);
                case 4:
                    if (c == '=') return 2;
                    throw new LexicalException("The character '" + c + "\' is invalid after ':' on line " + lineNumber + " position " + currentPosition);
                case 5:
                    if (c != '0' && Character.isDigit(c)) return 1;
                    throw new LexicalException("The character '" + c + "' on line " + lineNumber + " position " + currentPosition + " is not a positive digit");
                case 7:
                    if (c == '\"') {
                        numQuoteMarks = 2;
                        return 2;
                    }
                    if (Character.isWhitespace(c) || Character.isUpperCase(c) || Character.isDigit(c)) return 7;
                    throw new LexicalException("The character '" + c + "' on line " + lineNumber + " position " + currentPosition + " is invalid for ShortStrings");
            }
            throw new LexicalException("The character '" + c + "' on line " + lineNumber + " position " + currentPosition + " has no transition");

        }
        throw new LexicalException("The character '" + c + "' on line " + lineNumber + " position " + currentPosition + " is not part of the alphabet");
    }

    public boolean isInAlphabet(char c, char[] array) {
        for (char x : array)
            if (x == c)
                return true;
        return false;
    }


    public void state2() throws LexicalException {
        if (str.contains("\"")) {
            if (str.length() > 17)
                throw  new LexicalException("Short string must be a maximum of 15 characters long on line " + lineNumber + " position " + currentPosition);
            numQuoteMarks = 0;
            tokens.add(new Token(tokenId++, TokenType.token_shortstring.name().toUpperCase(), str));
        }
        else if (str.equals("0"))
            tokens.add(new Token(tokenId++, TokenType.token_number.name().toUpperCase(), str));
        else {
            for (TokenType t : TokenType.values())
                if (t.getTokenName().equals(str))
                    tokens.add(new Token(tokenId++, t.name().toUpperCase(), str));
        }
    }

    public void state3() {
        boolean added_token = false;
        for (TokenType t : TokenType.values()) {
            if (t.getTokenName().equals(str)) {
                tokens.add(new Token(tokenId++, t.name().toUpperCase(), str));
                added_token = true;
            }
        }
        if (!added_token)  tokens.add(new Token(tokenId++, TokenType.token_var.name().toUpperCase(), str));
    }

    public void checkToken() throws Exception{
        if (str.length() > 0) {
            if (state == 1) tokens.add(new Token(tokenId++, TokenType.token_number.name().toUpperCase(), str));
            else if (state == 2) {
                try {  state2(); }
                catch (Exception e) {
                    hasErrors = true;
                    System.out.println(e.getMessage());
                }
            }
            else if (state == 3) state3();
            else if (state == 4) throw new LexicalException("The character ':' on line " + lineNumber + " position " + currentPosition + " is missing a '= after it");
            else if (state == 5) throw new LexicalException("The character '-' on line " + lineNumber + " position " + currentPosition + " is missing positive digit(s) in front of it");
            else if (state == 7)
                throw  new LexicalException("Short string missing closing quotation marks on line " + lineNumber + " position " + currentPosition);
            str ="";
        }
    }
}