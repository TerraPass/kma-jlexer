package com.tdenysenko.kma.lexer.fsm;

import static com.tdenysenko.kma.lexer.conditions.Conditions.IS_HYPHEN;
import static com.tdenysenko.kma.lexer.conditions.Conditions.IS_UKRAINIAN_LETTER;

public class UkrainianWordFSMFactory implements ILexingFSMFactory {    
    @Override
    public ILexingFSM create() {
        State s0 = new State(false);
        State s1 = new State(true);
        State s2 = new State(false);

        s0.addTransition(IS_UKRAINIAN_LETTER, s1);
        s1.addTransition(IS_UKRAINIAN_LETTER, s1);
        s1.addTransition(IS_HYPHEN, s2);
        s2.addTransition(IS_UKRAINIAN_LETTER, s1);

        return new LexingFSM(s0);
    }
}
