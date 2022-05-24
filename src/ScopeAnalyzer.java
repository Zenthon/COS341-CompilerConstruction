import java.util.*;

public class ScopeAnalyzer {

    private final List<Token> tokens;
    private Token next;
    private Node tree;
    private Node symbolTable;

    public ScopeAnalyzer(List<Token> tokens) {
        this.tokens = new ArrayList<>(tokens);
    }

    // Generate the tree
    public void generateAST() {
        tree=  parseS();
    }

    private String match(String s) {
        Token lookAhead = tokens.remove(0);
        if (lookAhead.tType.equals(s)) return lookAhead.input;
        else if (lookAhead.input.equals(s)) return s;
        return "main";
    }

    private Node parseS() {
        next = tokens.get(0);
        Node SPLProgr = new Node("SPLProgr", "NON-TERMINAL");
        if (next.input.equals("main") || next.input.equals("proc")) {
            SPLProgr.addChildren(parseA());
            SPLProgr.addChildren(new Node(match("main"), "MAIN"));
            SPLProgr.addChildren(new Node(match("{"), "TERMINAL"));
            SPLProgr.addChildren(parseB());
            SPLProgr.addChildren(new Node(match("halt"), "TERMINAL"));
            SPLProgr.addChildren(new Node(match(";"), "TERMINAL"));
            SPLProgr.addChildren(parseC());
            SPLProgr.addChildren((new Node(match("}"), "TERMINAL")));
        }
        return SPLProgr;
    }

    private Node parseA() {
        next = tokens.get(0);
        if (next.input.equals("main") || next.input.equals("return") || next.input.equals("output") || next.tType.equals("TOKEN_VAR") || next.input.equals("if") || next.input.equals("do") || next.input.equals("while") || next.input.equals("call")) return null;
        Node ProcDefs = new Node("ProcDefs", "NON-TERMINAL");
        if (next.input.equals("proc")) {
            ProcDefs.addChildren(parseD());
            ProcDefs.addChildren(new Node(match(","), "TERMINAL"));
            ProcDefs.addChildren(parseA());
        }
        return ProcDefs;
    }

    private Node parseD() {
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

    private Node parseB() {
        next = tokens.get(0);
        if (next.input.equals("halt") || next.input.equals("return") || next.input.equals("}")) return null;
        Node Algorithm = new Node("Algorithm", "NON-TERMINAL");
        if (next.input.equals("output") || next.tType.equals("TOKEN_VAR") || next.input.equals("if") || next.input.equals("do") || next.input.equals("while") || next.input.equals("call")) {
            Algorithm.addChildren(parseE());
            Algorithm.addChildren(new Node(match(";"), "TERMINAL"));
            Algorithm.addChildren(parseB());
        }
        return Algorithm;
    }

    private Node parseE() {
        next = tokens.get(0);
        Node Instr = new Node("Instr", "NON-TERMINAL");
        if (next.input.equals("output") || next.tType.equals("TOKEN_VAR"))  Instr.addChildren(parseF());
        else if (next.input.equals("if"))  Instr.addChildren(parseG());
        else if (next.input.equals("do") || next.input.equals("while"))  Instr.addChildren(parseI());
        else if (next.input.equals("call"))  Instr.addChildren(parseL());
        return Instr;
    }

    private Node parseF() {
        next = tokens.get(0);
        Node ASSIGN = new Node("ASSIGN", "NON-TERMINAL");
        if (next.input.equals("output") ||next.tType.equals("TOKEN_VAR")) {
            ASSIGN.addChildren(parseJ());
            ASSIGN.addChildren(new Node(match(":="),"TERMINAL"));
            ASSIGN.addChildren(parseK());
        }
        return ASSIGN;
    }

    private Node parseG() {
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

    private Node parseH() {
        next = tokens.get(0);
        if (next.input.equals(";")) return null;
        Node Alternat = new Node("Alternat", "NON-TERMINAL");
        if (next.input.equals("else")) {
            Alternat.addChildren(new Node(match("else"), "TERMINAL"));
            Alternat.addChildren(new Node(match("{"), "TERMINAL"));
            Alternat.addChildren(parseB());
            Alternat.addChildren(new Node(match("}"), "TERMINAL"));
        }
        return Alternat;
    }

    private Node parseI() {
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

    private Node parseJ() {
        next = tokens.get(0);
        Node LHS = new Node("LHS", "NON-TERMINAL");
        if (next.input.equals("output"))   LHS.addChildren(new Node(match("output"), "TERMINAL"));
        else if (next.tType.equals("TOKEN_VAR") && tokens.get(1).input.equals("["))  LHS.addChildren(parseN());
        if (next.tType.equals("TOKEN_VAR")) LHS.addChildren(parseM());
        return LHS;
    }

    private Node parseK() {
        next = tokens.get(0);
        Node Expr = new Node("Expr", "NON-TERMINAL");
        if (next.tType.equals("TOKEN_SHORTSTRING") || next.tType.equals("TOKEN_NUMBER") || next.input.equals("true") || next.input.equals("false")) Expr.addChildren(parseO());
        if (next.tType.equals("TOKEN_VAR") && tokens.get(1).input.equals("[")) Expr.addChildren(parseN());
        if (next.tType.equals("TOKEN_VAR"))  Expr.addChildren(parseM());
        if (next.input.equals("input") || next.input.equals("not")) Expr.addChildren(parseP());
        if (next.input.equals("and") || next.input.equals("or") || next.input.equals("eq") || next.input.equals("larger") || next.input.equals("add") || next.input.equals("sub") || next.input.equals("mult")) Expr.addChildren(parseQ());
        return Expr;
    }

    private Node parseL() {
        next = tokens.get(0);
        Node PCall = new Node("PCall", "NON-TERMINAL");
        if (next.input.equals("call")) {
            PCall.addChildren(new Node(match("call"), "TERMINAL"));
            PCall.addChildren(new Node(match("TOKEN_VAR"), "PCall"));
        }
        return PCall;
    }

    private Node parseM() {
        next = tokens.get(0);
        Node Var = new Node("Var", "NON-TERMINAL");
        if(next.tType.equals("TOKEN_VAR")) Var.addChildren(new Node(match("TOKEN_VAR"), "TERMINAL"));
        return Var;
    }

    private Node parseN() {
        next = tokens.get(0);
        Node Field = new Node("Field", "NON-TERMINAL");
        if (next.tType.equals("TOKEN_VAR")) {
            Field.addChildren(new Node(match("TOKEN_VAR"), "TERMINAL"));
            Field.addChildren(new Node(match("["), "TERMINAL"));
            Field.addChildren(parseZ());
        }
        return Field;
    }

    private Node parseZ() {
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

    private Node parseO() {
        next = tokens.get(0);
        Node Const = new Node("Const", "TERMINAL");
        if (next.tType.equals("TOKEN_SHORTSTRING")) Const.addChildren(new Node(match("TOKEN_SHORTSTRING"), "TERMINAL"));
        if (next.tType.equals("TOKEN_NUMBER")) Const.addChildren(new Node(match("TOKEN_NUMBER"), "TERMINAL"));
        if (next.input.equals("true") || next.input.equals("false")) Const.addChildren(new Node(match(next.input), "TERMINAL"));
        return Const;
    }

    private Node parseP() {
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

    private Node parseQ() {
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

    private Node parseC() {
        next = tokens.get(0);
        if (next.input.equals("}")) return null;
        Node VarDecl = new Node("VarDecl", "NON-TERMINAL");
        if (next.input.equals("bool") || next.input.equals("num") || next.input.equals("string") || next.input.equals("arr")) {
            VarDecl.addChildren(parseR());
            VarDecl.addChildren(new Node(match(";"), "TERMINAL"));
            VarDecl.addChildren(parseC());
        }
        return VarDecl;
    }

    private Node parseR() {
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

    private Node parseT() {
        next = tokens.get(0);
        Node TYP = new Node("TYP", "NON-TERMINAL");
        if (next.input.equals("string")) TYP.addChildren(new Node(match("string"), "TYP_string"));
        if (next.input.equals("bool")) TYP.addChildren(new Node(match("bool"), "TYP_bool"));
        if (next.input.equals("num")) TYP.addChildren(new Node(match("num"), "TYP_num"));
        return TYP;
    }

    // Start the scope analysis
    public void startScopeAnalysis() throws SemanticException {
        // Check if main is used as a procedure name
        checkMainProcedure();

        // Generate the Abstract Syntax Tree from tokens
        generateAST();

        // Set the scopes of the tokens in the tree
        setScopes();

        // Convert tree into list of terminal tokens
        List<Node> nodes = new LinkedList<>();
        populateList(nodes, tree);
        for (int i=0;   i<nodes.size(); i++) {
            Node temp = nodes.get(i);
            if (temp.name.equals("proc"))
                nodes.get(i+1).childScope = temp.childScope;
        }

        // Create Global Symbol table
        symbolTable = getMainNode(nodes);
        symbolTable.parent = null;
        symbolTable.scopeID = -1;

        // Populate global symbol table and generate inner scope tables
        populateTable(nodes, symbolTable);

        checkProcedureCalls(symbolTable);
    }

    // Check procedure calls
    private void checkProcedureCalls(Node symbolTable) throws SemanticException {
        boolean isValid = false;
        for (Node child : symbolTable.children) {
            if (child.classifier.equals("PCall")) {
                if (child.name.equals(symbolTable.name))
                    isValid = true;

                if (!isValid) {
                    for (Node x : symbolTable.children) {
                        if (x.classifier.equals("procUserDefinedName") && x.name.equals(child.name)) {
                            isValid = true;
                            break;
                        }
                    }
                }
                if (!isValid)
                    throw new SemanticException("Procedure " + child.name + " is not declared (APPL-DECL error)");
            }
        }

        for (Node child : symbolTable.children)
            checkProcedureCalls(child);
    }

    // populate table
    void populateTable(List<Node> nodes, Node table) throws SemanticException {
        boolean deleted = false;
        for (int i=0;   i<nodes.size();     i++) {
            if (deleted) i = 0;
            Node temp = nodes.get(i);
            if (temp.scopeID == table.childScope) {
                if (temp.name.equals(table.name) && temp.classifier.equals("procUserDefinedName"))
                    throw new SemanticException("Procedure name " + temp.name + ", Scope #" + temp.scopeID + " is already used by the parent");
                if (temp.classifier.equals("procUserDefinedName") )
                    checkSiblings(temp, table.children);
                table.children.add(nodes.remove(i));
                if (table.children.get(table.children.size()-1).classifier.equals("procUserDefinedName"))
                    populateTable(nodes, table.children.get(table.children.size()-1));
                deleted = true;
            } else deleted = false;
        }
    }

    // get main node
    private Node getMainNode(List<Node> nodes) {
        for (int i=0;   i<nodes.size();     i++)
            if (nodes.get(i).name.equals("main"))
                return nodes.remove(i);
        return null;
    }

    // check sibling
    private void checkSiblings(Node child, List<Node> siblings) throws SemanticException {
        for (Node sibling : siblings)
            if (child.name.equals(sibling.name))
                throw new SemanticException("Procedure name " + child.name + ", Scope #" + child.scopeID + " is already used by a sibling");
    }

    // Set the scopes
    public void setScopes() {
        LinkedList<Node> open = new LinkedList<>();
        open.add(tree);
        Node temp;
        int scope = 0;

        while (!open.isEmpty()) {
            temp = open.remove(open.size()-1);
            if (temp.name.equals("proc"))  {
                scope++;
                temp.childScope = scope;
            }
            else if (temp.name.equals("main"))  scope = 0;
            temp.scopeID = scope;
            if (temp.name.equals("proc") || temp.classifier.equals("procUserDefinedName"))  temp.setCorrectScope();
            for (int i=temp.children.size()-1;  i>=0;   i--) open.add(temp.children.get(i));
        }
    }

    // Get nodes
    private void populateList(List<Node> list, Node n) {
        if (n == null) return;
        if (n.children.isEmpty()) list.add(n);
        for (Node child : n.children) populateList(list, child);
    }

    // Semantic Checking: No procedure can have the name main
    public void checkMainProcedure() throws SemanticException {
        for (int i=0;   i<tokens.size()-1;    i++)
            if (tokens.get(i).input.equals("proc") && tokens.get(i+1).input.equals("main"))
                throw new SemanticException("Word main is not allowed to be used as a procedure name.");
    }
}