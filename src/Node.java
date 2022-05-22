import java.lang.reflect.Array;
import java.util.ArrayList;

public class Node {

    public String name;
    public int scopeID;
    public String classifier;
    ArrayList<Node> children = new ArrayList<>();

    public Node(String n, String c) {
        name = n;
        scopeID = -1;
        classifier = c;
    }

    public void addChildren(Node child) {
        children.add(child);
    }
}
