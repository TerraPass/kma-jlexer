package com.tdenysenko.kma.lexer.fsm;

import com.tdenysenko.kma.lexer.conditions.Conditions;

public class PunctuationFSMFactory implements ILexingFSMFactory {
    @Override
    public ILexingFSM create() {
        State s0 = new State(false);
        State s1 = new State(true);

        s0.addTransition(Conditions.IS_NON_CONSUMABLE_SEPARATOR, s1);
        s1.addTransition(Conditions.IS_NON_CONSUMABLE_SEPARATOR, s1);

        return new LexingFSM(s0);
    }
}
