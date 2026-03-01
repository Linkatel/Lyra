package nodes;

// if ... then ... else ...
public class IfNode extends Node {
    public final Node condition;
    public final Node thenBranch;
    public final Node elseBranch;

    public IfNode(Node condition, Node thenBranch, Node elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
}
