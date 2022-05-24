import java.util.*;

public class ScopeAnalyzer {

    private List<Token> tokens;
    private Token next;
    private Node tree;

    public ScopeAnalyzer(List<Token> tokens) {
        this.tokens = new ArrayList<>(tokens);
    }

    // Generate the tree
    public Node generateAST() {
        return parseS();
    }

    public String match(String s) {
        Token lookAhead = tokens.remove(0);
        if (lookAhead.tType.equals(s))
            return lookAhead.input;
        else if (lookAhead.input.equals(s))
            return s;
        return "main";
    }

    public Node parseS() {
        next = tokens.get(0);
        Node SPLProgr = new Node("SPLProgr", "NON-TERMINAL");
        if (next.input.equals("main") || next.input.equals("proc")) {
            SPLProgr.addChildren(parseA());
            SPLProgr.addChildren(new Node(match("main"), "TERMINAL"));
            SPLProgr.addChildren(new Node(match("{"), "TERMINAL"));
            SPLProgr.addChildren(parseB());
            SPLProgr.addChildren(new Node(match("halt"), "TERMINAL"));
            SPLProgr.addChildren(new Node(match(";"), "TERMINAL"));
            SPLProgr.addChildren(parseC());
            SPLProgr.addChildren((new Node(match("}"), "TERMINAL")));
        }
        return SPLProgr;
    }

    public Node parseA() {
        next = tokens.get(0);
        if (next.input.equals("main") || next.input.equals("return") || next.input.equals("output") || next.tType.equals("TOKEN_VAR") || next.input.equals("if") || next.input.equals("do") || next.input.equals("while") || next.input.equals("call"))
            return null;
        Node ProcDefs = new Node("ProcDefs", "NON-TERMINAL");
        if (next.input.equals("proc")) {
            ProcDefs.addChildren(parseD());
            ProcDefs.addChildren(new Node(match(","), "TERMINAL"));
            ProcDefs.addChildren(parseA());
        }
        return ProcDefs;
    }

    public Node parseD() {
        next = tokens.get(0);
        Node PD = new Node("PD", "NON-TERMINAL");
        if (next.input.equals("proc")) {
            PD.addChildren(new Node(match("proc"), "TERMINAL"));
            PD.addChildren(new Node(match("TOKEN_VAR"), "procUserDefinedName"));
            PD.addChildren(new Node(match("{"), "TERMINAL"));
            PD.addChildren(parseA());
            PD.addChildren(parseB());
            PD.addChildren(new Node(match("return"), "TERMINAL"));
            PD.addChildren(new Node(match(";"), "TERMINAL"));
            PD.addChildren(parseC());
            PD.addChildren(new Node(match("}"), "TERMINAL"));
        }
        return PD;
    }

    Node parseB() {
        next = tokens.get(0);
        if (next.input.equals("halt") || next.input.equals("return") || next.input.equals("}"))
            return null;
        Node Algorithm = new Node("Algorithm", "NON-TERMINAL");
        if (next.input.equals("output") || next.tType.equals("TOKEN_VAR") || next.input.equals("if") || next.input.equals("do") || next.input.equals("while") || next.input.equals("call")) {
            Algorithm.addChildren(parseE());
            Algorithm.addChildren(new Node(match(";"), "TERMINAL"));
            Algorithm.addChildren(parseB());
        }
        return Algorithm;
    }

    public Node parseE() {
        next = tokens.get(0);
        Node Instr = new Node("Instr", "NON-TERMINAL");
        if (next.input.equals("output") || next.tType.equals("TOKEN_VAR"))
            Instr.addChildren(parseF());
        else if (next.input.equals("if"))
            Instr.addChildren(parseG());
        else if (next.input.equals("do") || next.input.equals("while"))
            Instr.addChildren(parseI());
        else if (next.input.equals("call"))
            Instr.addChildren(parseL());
        return Instr;
    }

    public Node parseF() {
        next = tokens.get(0);
        Node ASSIGN = new Node("ASSIGN", "NON-TERMINAL");
        if (next.input.equals("output") ||next.tType.equals("TOKEN_VAR")) {
            ASSIGN.addChildren(parseJ());
            ASSIGN.addChildren(new Node(match(":="),"TERMINAL"));
            ASSIGN.addChildren(parseK());
        }
        return ASSIGN;
    }

    public Node parseG() {
        next = tokens.get(0);
        Node Branch = new Node("Branch", "NON-TERMINAL");
        if (next.input.equals("if")) {
            Branch.addChildren(new Node(match("if"), "TERMINAL"));
            Branch.addChildren(new Node(match("("), "TERMINAL"));
            Branch.addChildren(parseK());
            Branch.addChildren(new Node(match(")"), "TERMINAL"));
            Branch.addChildren(new Node(match("then"), "TERMINAL"));
            Branch.addChildren(new Node(match("{"), "TERMINAL"));
            Branch.addChildren(parseB());
            Branch.addChildren(new Node(match("}"), "TERMINAL"));
            Branch.addChildren(parseH());
        }
        return Branch;
    }

    public Node parseH() {
        next = tokens.get(0);
        if (next.input.equals(";"))
            return null;
        Node Alternat = new Node("Alternat", "NON-TERMINAL");
        if (next.input.equals("else")) {
            Alternat.addChildren(new Node(match("else"), "TERMINAL"));
            Alternat.addChildren(new Node(match("{"), "TERMINAL"));
            Alternat.addChildren(parseB());
            Alternat.addChildren(new Node(match("}"), "TERMINAL"));
        }
        return Alternat;
    }

    public Node parseI() {
        next = tokens.get(0);
        Node Loop = new Node("Loop", "NON-TERMINAL");
        if (next.input.equals("do")) {
            Loop.addChildren(new Node(match("do"), "TERMINAL"));
            Loop.addChildren(new Node(match("{"), "TERMINAL"));
            Loop.addChildren(parseB());
            Loop.addChildren(new Node(match("}"), "TERMINAL"));
            Loop.addChildren(new Node(match("until"), "TERMINAL"));
            Loop.addChildren(new Node(match("("), "TERMINAL"));
            Loop.addChildren(parseK());
            Loop.addChildren(new Node(match(")"), "TERMINAL"));
        }
        else if (next.input.equals("while")) {
            Loop.addChildren(new Node(match("while"), "TERMINAL"));
            Loop.addChildren(new Node(match("("), "TERMINAL"));
            Loop.addChildren(parseK());
            Loop.addChildren(new Node(match(")"), "TERMINAL"));
            Loop.addChildren(new Node(match("do"), "TERMINAL"));
            Loop.addChildren(new Node(match("{"), "TERMINAL"));
            Loop.addChildren(parseB());
            Loop.addChildren(new Node(match("}"), "TERMINAL"));
        }
        return Loop;
    }

    public Node parseJ() {
        next = tokens.get(0);
        Node LHS = new Node("LHS", "NON-TERMINAL");
        if (next.input.equals("output"))
            LHS.addChildren(new Node(match("output"), "TERMINAL"));
        else if (next.tType.equals("TOKEN_VAR") && tokens.get(1).input.equals("["))
            LHS.addChildren(parseN());
        if (next.tType.equals("TOKEN_VAR"))
            LHS.addChildren(parseM());
        return LHS;
    }

    public Node parseK() {
        next = tokens.get(0);
        Node Expr = new Node("Expr", "NON-TERMINAL");
        if (next.tType.equals("TOKEN_SHORTSTRING") || next.tType.equals("TOKEN_NUMBER") || next.input.equals("true") || next.input.equals("false"))
            Expr.addChildren(parseO());
        if (next.tType.equals("TOKEN_VAR") && tokens.get(1).input.equals("["))
            Expr.addChildren(parseN());
        if (next.tType.equals("TOKEN_VAR"))
            Expr.addChildren(parseM());
        if (next.input.equals("input") || next.input.equals("not"))
            Expr.addChildren(parseP());
        if (next.input.equals("and") || next.input.equals("or") || next.input.equals("eq") || next.input.equals("larger") || next.input.equals("add") || next.input.equals("sub") || next.input.equals("mult"))
            Expr.addChildren(parseQ());
        return Expr;
    }

    public Node parseL() {
        next = tokens.get(0);
        Node PCall = new Node("PCall", "NON-TERMINAL");
        if (next.input.equals("call")) {
            PCall.addChildren(new Node(match("call"), "TERMINAL"));
            PCall.addChildren(new Node(match("TOKEN_VAR"), "TERMINAL"));
        }
        return PCall;
    }

    public Node parseM() {
        next = tokens.get(0);
        Node Var = new Node("Var", "NON-TERMINAL");
        if(next.tType.equals("TOKEN_VAR"))
            Var.addChildren(new Node(match("TOKEN_VAR"), "TERMINAL"));
        return Var;
    }

    public Node parseN() {
        next = tokens.get(0);
        Node Field = new Node("Field", "NON-TERMINAL");
        if (next.tType.equals("TOKEN_VAR")) {
            Field.addChildren(new Node(match("TOKEN_VAR"), "TERMINAL"));
            Field.addChildren(new Node(match("["), "TERMINAL"));
            Field.addChildren(parseZ());
        }
        return Field;
    }

    public Node parseZ() {
        next = tokens.get(0);
        Node Z = new Node("Z", "NON-TERMINAL");
        if (next.tType.equals("TOKEN_VAR")) {
            Z.addChildren(parseM());
            Z.addChildren(new Node(match("]"), "TERMINAL"));
        }
        if (next.tType.equals("TOKEN_SHORTSTRING") || next.tType.equals("TOKEN_NUMBER") || next.input.equals("true") || next.input.equals("false")) {
            Z.addChildren(parseO());
            Z.addChildren(new Node(match("]"), "TERMINAL"));
        }
        return Z;
    }

    public Node parseO() {
        next = tokens.get(0);
        Node Const = new Node("Const", "TERMINAL");
        if (next.tType.equals("TOKEN_SHORTSTRING"))
            Const.addChildren(new Node(match("TOKEN_SHORTSTRING"), "TERMINAL"));
        if (next.tType.equals("TOKEN_NUMBER"))
            Const.addChildren(new Node(match("TOKEN_NUMBER"), "TERMINAL"));
        if (next.input.equals("true") || next.input.equals("false"))
            Const.addChildren(new Node(match(next.input), "TERMINAL"));
        return Const;
    }

    public Node parseP() {
        next = tokens.get(0);
        Node UnOp = new Node("UnOp", "NON-TERMINAL");
        if (next.input.equals("input")) {
            UnOp.addChildren(new Node(match("input"), "TERMINAL"));
            UnOp.addChildren(new Node(match("("), "TERMINAL"));
            UnOp.addChildren(parseM());
            UnOp.addChildren(new Node(match(")"), "TERMINAL"));
        }
        if (next.input.equals("not")) {
            UnOp.addChildren(new Node(match("not"), "TERMINAL"));
            UnOp.addChildren(new Node(match("("), "TERMINAL"));
            UnOp.addChildren(parseK());
            UnOp.addChildren(new Node(match(")"), "TERMINAL"));
        }
        return UnOp;
    }

    public Node parseQ() {
        next = tokens.get(0);
        Node BinOp = new Node("BinOp", "NON-TERMINAL");
        if (next.input.equals("and") || next.input.equals("or") || next.input.equals("eq") || next.input.equals("larger") || next.input.equals("add") || next.input.equals("sub") || next.input.equals("mult")) {
            BinOp.addChildren(new Node(match(next.input), "TERMINAL"));
            BinOp.addChildren(new Node(match("("), "TERMINAL"));
            BinOp.addChildren(parseK());
            BinOp.addChildren(new Node(match(","), "TERMINAL"));
            BinOp.addChildren(parseK());
            BinOp.addChildren(new Node(match(")"), "TERMINAL"));
        }
        return BinOp;
    }

    public Node parseC() {
        next = tokens.get(0);
        if (next.input.equals("}"))
            return null;
        Node VarDecl = new Node("VarDecl", "NON-TERMINAL");
        if (next.input.equals("bool") || next.input.equals("num") || next.input.equals("string") || next.input.equals("arr")) {
            VarDecl.addChildren(parseR());
            VarDecl.addChildren(new Node(match(";"), "TERMINAL"));
            VarDecl.addChildren(parseC());
        }
        return VarDecl;
    }

    public Node parseR() {
        next = tokens.get(0);
        Node DEC = new Node("DEC", "NON-TERMINAL");
        if (next.input.equals("bool") || next.input.equals("num") || next.input.equals("string")) {
            DEC.addChildren(parseT());
            DEC.addChildren(parseM());
        }
        else if (next.input.equals("arr")) {
            DEC.addChildren(new Node(match("arr"), "TERMINAL"));
            DEC.addChildren(parseT());
            DEC.addChildren(new Node(match("["), "TERMINAL"));
            DEC.addChildren(parseO());
            DEC.addChildren(new Node(match("]"), "TERMINAL"));
            DEC.addChildren(parseM());
        }
        return DEC;
    }

    public Node parseT() {
        next = tokens.get(0);
        Node TYP = new Node("TYP", "NON-TERMINAL");
        if (next.input.equals("string"))
            TYP.addChildren(new Node(match("string"), "TERMINAL"));
        if (next.input.equals("bool"))
            TYP.addChildren(new Node(match("bool"), "TERMINAL"));
        if (next.input.equals("num"))
            TYP.addChildren(new Node(match("num"), "TERMINAL"));
        return TYP;
    }


    // Start the scope analysis
    public void startScopeAnalysis() throws SemanticException {
        checkMainProcedure();
        tree = generateAST();
        setScopes();
        System.out.println("Hello");
    }

    // Set the scopes
    public void setScopes() {
        LinkedList<Node> open = new LinkedList<>();
        open.add(tree);
        Node temp;
        int scope = 0;

        while (!open.isEmpty()) {
            temp = open.remove(open.size()-1);
            if (temp.name.equals("proc"))
                scope++;
            else if (temp.name.equals("main"))
                scope = 0;
            temp.scopeID = scope;

            if (temp.name.equals("proc") || temp.classifier.equals("procUserDefinedName"))
                temp.setCorrectScope();

            for (int i=temp.children.size()-1;  i>=0;   i--)
                open.add(temp.children.get(i));
        }
    }


    // Semantic Checking for Procedures
    public void checkMainProcedure() throws SemanticException {
        for (int i=0;   i<tokens.size()-1;    i++)
            if (tokens.get(i).input.equals("proc") && tokens.get(i+1).input.equals("main"))
                throw new SemanticException("Token #" + i+1 + ", Token name: main is not allowed to be used as a procedure name.");
    }
}