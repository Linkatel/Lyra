package nodes;

import java.util.List;

// create main(nb) ...
public class FunctionNode extends Node {
    public final String name;
    public final List<String> params;
    public final List<Node> body;

    public FunctionNode(String name, List<String> params, List<Node> body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }
}