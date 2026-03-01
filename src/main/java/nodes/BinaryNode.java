package nodes;

// nb > 3
public class BinaryNode extends Node {
    public final Node left;
    public final String operator;
    public final Node right;

    public BinaryNode(Node left, String operator, Node right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}
