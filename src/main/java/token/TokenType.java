package token;

public enum TokenType {
    // Mots clés
    CREATE,
    IF,
    THEN,
    ELSE,
    SEND,
    SET,
    SHOW,
    END,

    // Prebuild functions
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    ADD_EQUAL,
    SUBTRACT_EQUAL,

    // Types
    NUMBER,
    STRING,
    IDENTIFIER,
    EOF,

    // Caractères spéciaux
    LEFT_PARENTHESIS,
    RIGHT_PARENTHESIS,
    EQUAL,
    SMALLER_EQUAL,
    GREATER_EQUAL,
    EQUAL_EQUAL,
    SMALLER,
    GREATER,
    JUMP
}
