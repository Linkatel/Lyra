package nodes;

// "hello" ou 42
public class LiteralNode extends Node {
    public final Object value;

    public LiteralNode(Object value) {
        this.value = value;
    }
}
