public class Token {
    public String input;
    public String tType;
    public int id;

    public Token(int id, String tType, String input) {
        this.id = id;
        this.tType = tType;
        this.input = input;
    }

    public String tokenFormat() { return id + ", " + tType + ", " + input; }
}


enum TokenType {
    //  Non-terminals
    tok_main("main"),
    tok_halt("halt"),
    tok_proc("proc"),
    tok_return("return"),
    token_if("if"),
    token_then("then"),
    token_else("else"),
    token_do("do"),
    token_until("until"),
    token_while("while"),
    token_output("output"),
    token_call("call"),
    token_true("true"),
    token_false("false"),
    token_input("input"),
    token_not("not"),
    token_and("and"),
    token_or("or"),
    token_eq("eq"),
    token_larger("larger"),
    token_add("add"),
    token_sub("sub"),
    token_mult("mult"),
    token_arr("arr"),
    token_num("num"),
    token_bool("bool"),
    token_string("string"),
    token_leftbrace("{"),
    token_rightbrace("}"),
    token_semicolon(";"),
    token_comma(","),
    token_assign(":="),
    token_leftparen("("),
    token_rightparen(")"),
    token_leftbracket("["),
    token_rightbracket("]"),
    token_var("userDefinedName"),
    token_number("number"),
    token_shortstring("shortString"),
    token_eof("$");

    final String token_name;

    TokenType(String name) { token_name = name;  }

    public String getTokenName () { return token_name; }
}
