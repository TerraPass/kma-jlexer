package com.tdenysenko.kma.lexer.conditions;

public interface ITransitionCondition {
    boolean isSatisfiedBy(final char ch);

    String getDescription();
}
