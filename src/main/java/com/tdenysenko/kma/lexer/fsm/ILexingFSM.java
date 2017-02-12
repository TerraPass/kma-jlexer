package com.tdenysenko.kma.lexer.fsm;

import com.tdenysenko.kma.lexer.conditions.IExpectationsFormatter;

public interface ILexingFSM {
    void processChar(final char ch);
    void reset();

    boolean isInTerminalState();
    boolean isInFailedState();

    String describeExpectations(final IExpectationsFormatter expectationsFormatter);
}
