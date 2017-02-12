package com.tdenysenko.kma.lexer.lexing;

import org.apache.commons.lang3.Validate;

public class Lexeme {
    private final LexemeType type;
    private final String value;
    
    public Lexeme(final LexemeType type, final String value) {
        Validate.notNull(type);
        Validate.notNull(value);

        this.type = type;
        this.value = value;
    }

    public LexemeType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
