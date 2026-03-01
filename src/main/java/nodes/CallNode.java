package nodes;

import java.util.List;

public class CallNode extends Node {
    public final String name;
    public final List<Node> args;

    public CallNode(String name, List<Node> args) {
        this.name = name;
        this.args = args;
    }
}