package nodes;


public class SetNode extends Node {
    public final String name;
    public final Node value;

    public SetNode(String name, Node value) {
        this.name = name;
        this.value = value;
    }
}