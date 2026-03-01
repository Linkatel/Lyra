package nodes;

public class SendNode extends Node {
    public final Node value;

    public SendNode(Node value) {
        this.value = value;
    }
}
