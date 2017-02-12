package com.tdenysenko.kma.lexer.fsm;

import com.tdenysenko.kma.lexer.fsm.IState;
import com.tdenysenko.kma.lexer.fsm.ILexingFSM;
import com.tdenysenko.kma.lexer.conditions.IExpectationsFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.Validate;

public class LexingFSM implements ILexingFSM {
    //private final List<IState> states;
    private IState initialState;

    private IState currentState;

    public LexingFSM(/*final List<IState> states,*/ final IState initialState) {
        //Validate.notNull(states);
        Validate.notNull(initialState);

        //this.states = new ArrayList<>(states);
        this.initialState = initialState;

        this.reset();
    }

    public LexingFSM() {
        this(/*new ArrayList<>(),*/ IState.FAILED);
    }

    public LexingFSM setInitialState(IState initialState) {
        Validate.notNull(initialState);

        this.initialState = initialState;

        return this;
    }
    
    @Override
    public void processChar(final char ch) {
        this.currentState = this.currentState.getNextState(ch);
    }

    @Override
    public final void reset() {
        this.currentState = this.initialState;
    }
    
    @Override
    public boolean isInTerminalState() {
        return this.currentState.isTerminal();
    }

    @Override
    public boolean isInFailedState() {
        return IState.FAILED.equals(this.currentState);
    }

    @Override
    public String describeExpectations(final IExpectationsFormatter expectationsFormatter) {
        Validate.notNull(expectationsFormatter);

        return expectationsFormatter.formatExpectations(this.currentState.getExpectations());
    }
}
