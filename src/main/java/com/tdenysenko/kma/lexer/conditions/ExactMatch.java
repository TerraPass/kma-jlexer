package com.tdenysenko.kma.lexer.conditions;

public class ExactMatch implements ITransitionCondition {
    private final char matchedChar;

    public ExactMatch(char matchedChar) {
        this.matchedChar = matchedChar;
    }
    
    @Override
    public boolean isSatisfiedBy(char ch) {
        return matchedChar == ch;
    }

    @Override
    public String getDescription() {
        return String.format("character \'%s\'", String.valueOf(matchedChar));
    }
}
