package lexer;

import token.Token;
import token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String source;
    private int pos = 0;

    public Lexer(String source) {
        this.source = source;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (pos < source.length()) {
            char c = source.charAt(pos);
            
            switch (c) {
                case ' ' -> {
                    pos++;
                }
                case '(' -> {
                    tokens.add(new Token(TokenType.LEFT_PARENTHESIS, "("));
                    pos++;
                }
                case ')' -> {
                    tokens.add(new Token(TokenType.RIGHT_PARENTHESIS, ")"));
                    pos++;
                }
                case '<' -> {
                    if (pos + 1 < source.length() && source.charAt(pos + 1) == '=') {
                        tokens.add(new Token(TokenType.SMALLER_EQUAL, "<="));
                        pos += 2;
                    } else {
                        tokens.add(new Token(TokenType.SMALLER, "<"));
                        pos++;
                    }
                }
                case '>' -> {
                    if (pos + 1 < source.length() && source.charAt(pos + 1) == '=') {
                        tokens.add(new Token(TokenType.GREATER_EQUAL, ">="));
                        pos += 2;
                    } else {
                        tokens.add(new Token(TokenType.GREATER, ">"));
                        pos++;
                    }
                }
                case '=' -> {
                    if (pos + 1 < source.length() && source.charAt(pos + 1) == '=') {
                        tokens.add(new Token(TokenType.EQUAL_EQUAL, "=="));
                        pos += 2;
                    } else {
                        tokens.add(new Token(TokenType.EQUAL, "="));
                        pos++;
                    }
                }
                case '+' -> {
                    if (pos + 1 < source.length() && source.charAt(pos + 1) == '=') {
                        tokens.add(new Token(TokenType.ADD_EQUAL, "+="));
                        pos += 2;
                    } else {
                        tokens.add(new Token(TokenType.ADD, "+"));
                        pos++;
                    }
                }
                case '-' -> {
                    if (pos + 1 < source.length() && source.charAt(pos + 1) == '=') {
                        tokens.add(new Token(TokenType.SUBTRACT_EQUAL, "-="));
                        pos += 2;
                    } else {
                        tokens.add(new Token(TokenType.SUBTRACT, "-"));
                        pos++;
                    }
                }
                case '/' -> {
                    tokens.add(new Token(TokenType.DIVIDE, "/"));
                    pos++;
                }
                case '*' -> {
                    tokens.add(new Token(TokenType.MULTIPLY, "*"));
                    pos++;
                }
                case '"' -> {
                    pos++;
                    StringBuilder str = new StringBuilder();
                    while (pos < source.length() && source.charAt(pos) != '"') {
                        str.append(source.charAt(pos));
                        pos++;
                    }
                    pos++;
                    tokens.add(new Token(TokenType.STRING, str.toString()));
                }
                case '\'' -> {
                    pos++;
                    StringBuilder str = new StringBuilder();
                    while (pos < source.length() && source.charAt(pos) != '\'') {
                        str.append(source.charAt(pos));
                        pos++;
                    }
                    pos++;
                    tokens.add(new Token(TokenType.STRING, str.toString()));
                }

                case '\n' -> {
                    tokens.add(new Token(TokenType.JUMP, "\n"));
                    pos++;
                }
                default -> {
                    if (Character.isLetter(c)) {
                        StringBuilder word = new StringBuilder();
                        while (pos < source.length() && Character.isLetterOrDigit(source.charAt(pos))) {
                            word.append(source.charAt(pos));
                            pos++;
                        }
                        String w = word.toString();
                        switch (w) {
                            case "create"  -> tokens.add(new Token(TokenType.CREATE, w));
                            case "if"      -> tokens.add(new Token(TokenType.IF, w));
                            case "then"    -> tokens.add(new Token(TokenType.THEN, w));
                            case "else"    -> tokens.add(new Token(TokenType.ELSE, w));
                            case "send"    -> tokens.add(new Token(TokenType.SEND, w));
                            case "show"    -> tokens.add(new Token(TokenType.SHOW, w));
                            case "end"     -> tokens.add(new Token(TokenType.END, w));
                            case "set"     -> tokens.add(new Token(TokenType.SET, w));
                            default        -> tokens.add(new Token(TokenType.IDENTIFIER, w));
                        }
                    } else if (Character.isDigit(c)) {
                        StringBuilder word = new StringBuilder();
                        while (pos < source.length() && (Character.isDigit(source.charAt(pos)) || source.charAt(pos) == '.')) { // <- ici
                            word.append(source.charAt(pos));
                            pos++;
                        }
                        tokens.add(new Token(TokenType.NUMBER, word.toString()));
                    } else {
                        pos++;
                    }
                }
            }
        }

        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }
}