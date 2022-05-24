import java.util.*;

public class Node {

    public String name;
    public int scopeID;
    public int childScope;

    public String classifier;
    public Node parent;
    ArrayList<Node> children = new ArrayList<>();


    public Node(String n, String c) {
        name = n;
        scopeID = -1;
        classifier = c;
        parent = null;
    }

    public void setCorrectScope() {
        scopeID = getScope(this.parent);

    }

    public int getScope(Node n) {
        if (n.name.equals("ProcDefs") && !n.parent.name.equals("ProcDefs"))
            return n.scopeID;
        return getScope(n.parent);
    }



    public void addChildren(Node child) {
        if (child != null) {
            child.parent = this;
            children.add(child);
        }
    }
}
