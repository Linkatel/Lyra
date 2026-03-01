package nodes;

// send "a"
public class SendNode extends Node {
    public final Node value;

    public SendNode(Node value) {
        this.value = value;
    }
}
