package com.tdenysenko.kma.lexer.conditions;

public class AnyCharacter implements ITransitionCondition {

    @Override
    public boolean isSatisfiedBy(char ch) {
        return true;
    }

    @Override
    public String getDescription() {
        return "any character";
    }
}
