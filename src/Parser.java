import java.util.List;

public class Parser {

    private List<Token> tokens;
    private Token next;
    private String xmlString;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        xmlString = null;
    }

    public String getXmlString() {
        return xmlString;
    }

    public String match(String s) throws ParserException {
        Token lookAhead = tokens.remove(0);
        if (lookAhead.tType.equals(s))
            return lookAhead.input;
        else if (lookAhead.input.equals(s))
            return s;
        throw new ParserException("Token #" + lookAhead.id + ", Token name: " + lookAhead.input + " is invalid. Expected " + s);
    }

    public void parseGrammar() throws ParserException {
        xmlString = "<SPLProgr>" + parseS() + "</SPLProgr>";
        if (!tokens.isEmpty())
            throw new ParserException("Not all the tokens were processed");
    }

    private String parseS() throws ParserException {
        next = tokens.get(0);
        if (next.input.equals("main") || next.input.equals("proc"))
            return "<ProcDefs>" + parseA() + "</ProcDefs>" + match("main") + " " + match("{") + "<Algorithm>" + parseB() + "</Algorithm>" + match("halt") + " " + match(";") + "<VarDecl>" + parseC() + "</VarDecl>" + match("}");
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

    private String parseF() throws ParserException {
        next = tokens.get(0);
        if (next.input.equals("output") ||next.tType.equals("TOKEN_VAR"))
            return "<LHS>" + parseJ() + "</LHS>" + match(":=") + "<Expr>" + parseK() + "</Expr>";
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid.");
    }

    private String parseG() throws ParserException {
        next = tokens.get(0);
        if (next.input.equals("if"))
            return match("if") + " " + match("(") + "<Expr>" + parseK() + "</Expr>" + match(")") + " " + match("then") + " " + match("{") + "<Algorithm>" + parseB() + "</Algorithm>" + match("}") + "<Alternat>" + parseH() + "</Alternat>";
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid.");
    }

    private String parseH() throws ParserException {
        next = tokens.get(0);
        if (next.input.equals(";"))
            return "epsilon";
        else if (next.input.equals("else"))
            return match("else") + " " + match("{") + " " + "<Algorithm>" + parseB() + "</Algorithm>" + match("}");
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid.");
    }

    private String parseI() throws ParserException {
        next = tokens.get(0);
        if (next.input.equals("do"))
            return match("do") + " " + match("{") + "<Algorithm>" + parseB() + "</Algorithm>" + match("}") + " " + match("until") + " " + match("(") + "<Expr>" + parseK() + "</Expr>" + match(")");
        else if (next.input.equals("while"))
            return match("while") + " " + match("(") + "<Expr>" + parseK() + "</Expr>" + match(")") + " " + match("do") + " " + match("{") + "<Algorithm>" + parseB() + "</Algorithm>" + match("}");
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid.");
    }

    private String parseJ() throws ParserException {
        next = tokens.get(0);
        if (next.input.equals("output"))
            return match("output");
        else if (next.tType.equals("TOKEN_VAR") && tokens.get(1).input.equals("["))
            return "<Field>" + parseN() + "</Field>";
        if (next.tType.equals("TOKEN_VAR"))
            return "<Var>" + parseM() + "</Var>";
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid. Expected output or a userDefinedName");
    }

    private String parseK() throws ParserException {
        next = tokens.get(0);
        if (next.tType.equals("TOKEN_SHORTSTRING") || next.tType.equals("TOKEN_NUMBER") || next.input.equals("true") || next.input.equals("false"))
            return "<Const>" + parseO() + "</Const>";
        if (next.tType.equals("TOKEN_VAR") && tokens.get(1).input.equals("["))
            return "<Field>" + parseN() + "</Field>";
        if (next.tType.equals("TOKEN_VAR"))
            return "<Var>" + parseM() + "</Var>";
        if (next.input.equals("input") || next.input.equals("not"))
            return "<UnOp>" + parseP() + "</UnOp>";
        if (next.input.equals("and") || next.input.equals("or") || next.input.equals("eq") || next.input.equals("larger") || next.input.equals("add") || next.input.equals("sub") || next.input.equals("mult"))
            return "<BinOp>" + parseQ() + "</BinOp>";
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid.");
    }

    private String parseL() throws ParserException {
        next = tokens.get(0);
        if (next.input.equals("call"))
            return match("call") + " " + match("TOKEN_VAR");
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid.");
    }

    private String parseM() throws ParserException {
        next = tokens.get(0);
        if(next.tType.equals("TOKEN_VAR"))
            return match("TOKEN_VAR");
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid.");
    }

    private String parseN() throws ParserException {
        next = tokens.get(0);
        if (next.tType.equals("TOKEN_VAR"))
            return match("TOKEN_VAR") + " " + match("[") + parseZ();
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid.");
    }

    private String parseZ() throws ParserException {
        next = tokens.get(0);
        if (next.tType.equals("TOKEN_VAR"))
            return "<Var>" + parseM() + "</Var>" + match("]");
        if (next.tType.equals("TOKEN_SHORTSTRING") || next.tType.equals("TOKEN_NUMBER") || next.input.equals("true") || next.input.equals("false"))
            return "<Const>" + parseO() + "</Const>" + match("]");
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid.");
    }

    private String parseO() throws ParserException {
        next = tokens.get(0);
        if (next.tType.equals("TOKEN_SHORTSTRING"))
            return match("TOKEN_SHORTSTRING");
        if (next.tType.equals("TOKEN_NUMBER"))
            return match("TOKEN_NUMBER");
        if (next.input.equals("true") || next.input.equals("false"))
            return match(next.input);
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid.");
    }

    private String parseP() throws ParserException {
        next = tokens.get(0);
        if (next.input.equals("input"))
            return match("input") + " " + match("(") + "<Var>" + parseM() + "</Var>" + match(")");
        if (next.input.equals("not"))
            return match("not") + " " + match("(") + "<Expr>" + parseK() + "</Expr>" + match(")");
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid.");
    }

    private String parseQ() throws ParserException {
        next = tokens.get(0);
        if (next.input.equals("and") || next.input.equals("or") || next.input.equals("eq") || next.input.equals("larger") || next.input.equals("add") || next.input.equals("sub") || next.input.equals("mult"))
            return match(next.input) + " " + match("(") + "<Expr>" + parseK() + "</Expr>" + match(",") + "<Expr>" + parseK() + "</Expr>" + match(")");
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid.");
    }

    private String parseC() throws ParserException {
        next = tokens.get(0);
        if (next.input.equals("}"))
            return "epsilon";
        if (next.input.equals("bool") || next.input.equals("num") || next.input.equals("string") || next.input.equals("arr"))
            return "<Dec>" + parseR() + "</Dec>" + match(";") + "<VarDecl>" + parseC() + "</VarDecl>";
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid.");
    }

    private String parseR() throws ParserException {
        next = tokens.get(0);
        if (next.input.equals("bool") || next.input.equals("num") || next.input.equals("string"))
            return "<TYP>" + parseT() + "</TYP>" + "<Var>" + parseM() + "</Var>";
        if (next.input.equals("arr"))
            return match("arr") + "<TYP>" + parseT() + "</TYP>" + match("[") + "<Const>" + parseO() + "</Const>" + match("]") + "<Var>" + parseM() + "</Var>";
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid.");
    }

    private String parseT() throws ParserException {
        next = tokens.get(0);
        if (next.input.equals("string"))
            return match("string");
        if (next.input.equals("bool"))
            return match("bool");
        if (next.input.equals("num"))
            return match("num");
        throw new ParserException("Token #" + next.id + ", Token name: " + next.input + " is invalid.");
    }
}
