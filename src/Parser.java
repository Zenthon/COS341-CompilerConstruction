import java.util.List;

public class Parser {

    private List<Token> tokens;
    private Token next;
    private String xmlString;
    private boolean hasErrors = false;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.tokens.add(new Token(this.tokens.size(), TokenType.token_eof.name().toUpperCase(), "$"));
        xmlString = null;
    }

    public boolean getHasErrors() {
        return hasErrors;
    }

    public String getXmlString() {
        return xmlString;
    }

    public String match(String s) throws ParserException {
        Token lookAhead = tokens.remove(0);
        if (lookAhead.tType.equals(s))
            return s;
        else if (lookAhead.input.equals(s))
            return s;
        throw new ParserException("Token #" + lookAhead.id + ", Token name: " + lookAhead.input + " is invalid. Expected " + s);
    }

    public void parseGrammar() throws ParserException {
        xmlString = "<SPLProgr>" + parseS() + "</SPLProgr>";
    }

    private String parseS() throws ParserException {
        next = tokens.get(0);
        if (next.input.equals("main") || next.input.equals("proc"))
            return "<ProcDefs>" + parseA() + "</ProcDefs>" + match("main") + " " + match("{") + "<Algorithm>" + parseB() + "</Algorithm>" + match("halt") + match(";") + "<VarDecl>" + parseC() + "</VarDecl>" + match("}");
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid");
    }

    private String parseA() throws ParserException {
        next = tokens.get(0);
        if (next.input.equals("main") || next.input.equals("return") || next.input.equals("output") || next.tType.equals("TOKEN_VAR") || next.input.equals("if") || next.input.equals("do") || next.input.equals("while") || next.input.equals("call"))
            return "epsilon";
        else if (next.input.equals("proc"))
            return "<PD>" + parseD() + "</PD>" + match(",") + "<ProcDefs>" + parseA() + "</ProcDefs>";
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid");
    }

    private String parseD() throws ParserException {
        next = tokens.get(0);
        if (next.input.equals("proc"))
            return match("proc") + " " + match("TOKEN_VAR") + " " + match("{") + "<ProcDefs>" + parseA() + "</ProcDefs>" + "<Algorithm>" + parseB() + "</Algorithm>" + match("return") + " " + match(";") + "<VarDecl>" + parseC() + "</VarDecl>" + match("}");
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid. Expected proc");
    }

    private String parseB() throws ParserException {
        next = tokens.get(0);
        if (next.input.equals("halt") || next.input.equals("return") || next.input.equals("}"))
            return "epsilon";
        else if (next.input.equals("output") || next.tType.equals("TOKEN_VAR") || next.input.equals("if") || next.input.equals("do") || next.input.equals("while") || next.input.equals("call"))
            return "<Instr>" + parseE() + "</Instr>" + match(";") + "<Algorithm>" + parseB() + "</Algorithm>";
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid.");
    }

    private String parseE() throws ParserException {
        next = tokens.get(0);
        if (next.input.equals("output") || next.tType.equals("TOKEN_VAR"))
            return "<Assign>" + parseF() + "</Assign>";
        else if (next.input.equals("if"))
            return "<Branch>" + parseG() + "</Branch>";
        else if (next.input.equals("do") || next.input.equals("while"))
            return "<Loop>" + parseI() + "</Loop>";
        else if (next.input.equals("call"))
            return "<PCall>" + parseL() + "</PCall>";
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid.");
    }

    private String parseF() {
        next = tokens.get(0);
        return "";
    }

    private String parseG() {
        next = tokens.get(0);
        return "";
    }

    private String parseH() {
        next = tokens.get(0);
        return "";
    }

    private String parseI() {
        next = tokens.get(0);
        return "";
    }

    private String parseJ() {
        next = tokens.get(0);
        return "";
    }

    private String parseK() {
        next = tokens.get(0);
        return "";
    }

    private String parseL() {
        next = tokens.get(0);
        return "";
    }

    private String parseM() {
        next = tokens.get(0);
        return "";
    }

    private String parseN() {
        next = tokens.get(0);
        return "";
    }

    private String parseZ() {
        next = tokens.get(0);
        return "";
    }

    private String parseO() {
        next = tokens.get(0);
        return "";
    }

    private String parseP() {
        next = tokens.get(0);
        return "";
    }

    private String parseQ() {
        next = tokens.get(0);
        return "";
    }

    private String parseC() {
        next = tokens.get(0);
        return "";
    }

    private String parseR() {
        next = tokens.get(0);
        return "";
    }

    private String parseT() {
        next = tokens.get(0);
        return "";
    }


}
