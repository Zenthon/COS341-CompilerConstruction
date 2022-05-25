import java.util.*;

public class Node {

    public String name;
    public Integer scopeID;             //  Scope of the variable or function
    public int childScope;
    public String classifier;
    public Node parent;
    public ArrayList<Node> children = new ArrayList<>();
    public String type;

    public boolean alreadySet = false;



    public Node(String n, String c) {
        type = "U";
        name = n;
        scopeID = -1;
        classifier = c;
        parent = null;
    }

    public void setCorrectScope() {
        scopeID = getScope(this.parent);
    }

    public void setCorrectScope(String name) {
        scopeID = getNewScope(this.parent);
    }

    public int getNewScope(Node n) {
        if (n.parent == null)
            return n.scopeID;
        if (n.name.equals("PCall")) return n.scopeID;
        return getNewScope(n.parent);
    }

    public int getScope(Node n) {
        if (n.name.equals("ProcDefs") && !n.parent.name.equals("ProcDefs")) return n.scopeID;
        return getScope(n.parent);
    }

    public void addChildren(Node child) {
        if (child != null) {
            child.parent = this;
            children.add(child);
        }
    }
}
