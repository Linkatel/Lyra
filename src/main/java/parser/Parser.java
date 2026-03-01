package parser;

import nodes.*;
import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Parser {
    private final List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Node> parse() {
        List<Node> nodes = new ArrayList<>();

        while (current().type != TokenType.EOF) {
            if (current().type == TokenType.JUMP) {
                consume();
            } else {
                nodes.add(parseStatement());
            }
        }

        return nodes;
    }

    private Token current() {
        return tokens.get(pos);
    }

    private Token consume() {
        return tokens.get(pos++);
    }

    private Token expect(TokenType type) {
        if (current().type != type) {
            throw new RuntimeException("Waiting " + type + " but found " + current().type);
        }
        return consume();
    }

    private IfNode parseIf() {
        consume();

        Node condition = parseExpression();

        expect(TokenType.THEN);
        Node thenBranch = parseStatement();

        expect(TokenType.ELSE);
        Node elseBranch = parseStatement();

        return new IfNode(condition, thenBranch, elseBranch);
    }


    private Node parseStatement() {
        return switch (current().type) {
            case CREATE -> parseFunction();
            case IF -> parseIf();
            case SEND -> parseSend();
            case SHOW -> parseShow();
            case SET -> parseSet();
            default -> parseExpression();
        };
    }

    private Node parseExpression() {
        Node left = parseArithmetic();

        if (current().type == TokenType.GREATER ||
                current().type == TokenType.SMALLER ||
                current().type == TokenType.EQUAL_EQUAL ||
                current().type == TokenType.GREATER_EQUAL ||
                current().type == TokenType.SMALLER_EQUAL) {

            String operator = consume().value;
            Node right = parsePrimary();
            return new BinaryNode(left, operator, right);
        }

        return left;
    }

    private Node parseArithmetic() {
        Node left = parseTerm();

        while (current().type == TokenType.ADD ||
                current().type == TokenType.SUBTRACT) {
            String operator = consume().value;
            Node right = parseTerm();
            left = new BinaryNode(left, operator, right);
        }

        return left;
    }

    private Node parseTerm() {
        Node left = parsePrimary();

        while (current().type == TokenType.MULTIPLY ||
                current().type == TokenType.DIVIDE) {
            String operator = consume().value;
            Node right = parsePrimary();
            left = new BinaryNode(left, operator, right);
        }

        return left;
    }

    private Node parsePrimary() {
        if (current().type == TokenType.LEFT_PARENTHESIS) {
            consume();
            Node expr = parseArithmetic();
            expect(TokenType.RIGHT_PARENTHESIS);
            return expr;
        }

        Token t = consume();
        return switch (t.type) {
            case NUMBER -> new LiteralNode(Double.parseDouble(t.value));
            case STRING -> new LiteralNode(t.value);
            case IDENTIFIER -> {
                String name = t.value;
                if (current().type == TokenType.LEFT_PARENTHESIS) {
                    consume(); // "("
                    List<Node> args = new ArrayList<>();
                    while (current().type != TokenType.RIGHT_PARENTHESIS) {
                        args.add(parseArithmetic());
                    }
                    expect(TokenType.RIGHT_PARENTHESIS);
                    yield new CallNode(name, args);
                }
                yield new IdentifierNode(t.value);
            }
            default -> throw new RuntimeException("Valeur attendue, trouvé : " + t.type);
        };
    }

    private Node parseSend() {
        consume();
        Node value = parseArithmetic();
        return new SendNode(value);
    }

    private Node parseShow() {
        consume();
        Node value = parseArithmetic();
        return new ShowNode(value);
    }

    private Node parseFunction() {
        consume();
        String name = consume().value;
        expect(TokenType.LEFT_PARENTHESIS);

        List<String> params = new ArrayList<>();
        while (current().type != TokenType.RIGHT_PARENTHESIS) {
            params.add(consume().value);
        }
        expect(TokenType.RIGHT_PARENTHESIS);

        List<Node> body = new ArrayList<>();
        while (current().type != TokenType.END && current().type != TokenType.EOF) {
            if (current().type == TokenType.JUMP) {
                consume();
            } else {
                body.add(parseStatement());
            }
        }
        expect(TokenType.END);

        return new FunctionNode(name, params, body);
    }

    private Node parseSet() {
        consume();
        String name = consume().value;
        expect(TokenType.EQUAL);
        Node value = parseExpression();
        return new SetNode(name, value);
    }
}