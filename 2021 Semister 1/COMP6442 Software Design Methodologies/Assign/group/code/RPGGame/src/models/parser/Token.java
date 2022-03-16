package models.parser;

import enums.TokenType;

public class Token {
    private String token = "";
    private TokenType type = TokenType.UNKNOWN;
    private String value;

    public Token(String token, TokenType type) {
        this.token = token;
        this.type = type;
    }

    public Token(String token, TokenType type, String value) {
        this.token = token;
        this.type = type;
        this.value = value;
    }

    public String getToken() {
        return token;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
