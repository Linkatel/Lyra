import exceptions.LyraException;
import interpreter.Interpreter;
import lexer.Lexer;
import nodes.Node;
import parser.Parser;
import token.Token;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        String source = Files.readString(Path.of("test.lyra"));

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();

        Parser parser = new Parser(tokens);
        List<Node> nodes = parser.parse();

        Interpreter interpreter = new Interpreter();
        try {
            interpreter.run(nodes);
        } catch (LyraException e) {
            System.err.println("[Lyra] " + e.getMessage());
        }
    }
}