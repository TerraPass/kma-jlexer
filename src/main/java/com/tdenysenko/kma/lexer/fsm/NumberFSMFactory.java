package com.tdenysenko.kma.lexer.fsm;

import com.tdenysenko.kma.lexer.conditions.Conditions;

public class NumberFSMFactory implements ILexingFSMFactory {
    @Override
    public ILexingFSM create() {
        State s0 = new State(false);
        State s1 = new State(true);
        State s2 = new State(false);
        State s3 = new State(true);

        s0.addTransition(Conditions.IS_HYPHEN, s1);
        s0.addTransition(Conditions.IS_DIGIT, s1);
        s1.addTransition(Conditions.IS_DIGIT, s1);
        s1.addTransition(Conditions.IS_DECIMAL_SEPARATOR, s2);
        s2.addTransition(Conditions.IS_DIGIT, s3);

        return new LexingFSM(s0);
    }
}
