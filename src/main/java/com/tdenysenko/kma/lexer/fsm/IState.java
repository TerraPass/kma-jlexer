package com.tdenysenko.kma.lexer.fsm;

import com.tdenysenko.kma.lexer.conditions.ITransitionCondition;
import java.util.ArrayList;
import java.util.List;

public interface IState {
    public static final IState FAILED = new IState() {
        @Override
        public IState getNextState(char ch) {
            return this;
        }

        @Override
        public boolean isTerminal() {
            return false;
        }

        @Override
        public List<ITransitionCondition> getExpectations() {
            return new ArrayList<>();
        }
    };

    IState getNextState(final char ch);
    boolean isTerminal();

    /**
     * Get a list of conditions, permitting to transition from this state
     * to any (including self), except for IState.FAILED.
     * I.e. a list of conditions this state "expects".
     * 
     * @return A list of conditions, through which transitions are made from this state
     * to any state (including itself), except for IState.FAILED.
     */
    List<ITransitionCondition> getExpectations();
}
