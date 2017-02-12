package com.tdenysenko.kma.lexer.fsm;

import com.tdenysenko.kma.lexer.conditions.ExactMatch;
import com.tdenysenko.kma.lexer.conditions.Conditions;
import com.tdenysenko.kma.lexer.conditions.OneOf;

public class DateFSMFactory implements ILexingFSMFactory {
    @Override
    public ILexingFSM create() {
        State s0 = new State(false);
        State s1 = new State(false);
        State s2 = new State(false);
        State s3 = new State(false);
        State s4 = new State(false);
        State s5 = new State(false);
        State s6 = new State(false);
        State s7 = new State(true);

        s0.addTransition(new OneOf("012"), s1);
        s0.addTransition(new ExactMatch('3'), s2);
        s0.addTransition(new OneOf("456789"), s3);
        s1.addTransition(Conditions.IS_DIGIT, s3);
        s1.addTransition(Conditions.IS_DATE_SEPARATOR, s4);
        s2.addTransition(new OneOf("01"), s3);
        s2.addTransition(Conditions.IS_DATE_SEPARATOR, s3);
        s3.addTransition(Conditions.IS_DATE_SEPARATOR, s4);
        s4.addTransition(new ExactMatch('0'), s5);
        s4.addTransition(new ExactMatch('1'), s6);
        s5.addTransition(Conditions.IS_DIGIT, s7);
        s6.addTransition(new OneOf("012"), s7);

        return new LexingFSM(s0);
    }
}
