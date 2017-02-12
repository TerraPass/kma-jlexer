package com.tdenysenko.kma.lexer.fsm;

import com.tdenysenko.kma.lexer.conditions.AnyCharacter;
import com.tdenysenko.kma.lexer.conditions.ITransitionCondition;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Validate;

public class State implements IState {
    private final Map<ITransitionCondition, IState> transitions;
    private IState defaultTransitionState;
    private boolean terminal;

    public State(
        final Map<ITransitionCondition, IState> transitions,
        final IState defaultTransitionState,
        final boolean terminal
    ) {
        Validate.notNull(transitions);
        Validate.notNull(defaultTransitionState);

        this.transitions = new HashMap<>(transitions);
        this.defaultTransitionState = defaultTransitionState;
        this.terminal = terminal;
    }

    public State(final Map<ITransitionCondition, IState> transitions, final boolean terminal) {
        this(transitions, FAILED, terminal);
    }

    public State(final boolean terminal) {
        this(new HashMap<>(), FAILED, terminal);
    }

    public IState getDefaultTransitionState() {
        return defaultTransitionState;
    }

    public State setDefaultTransitionState(final IState defaultTransitionState) {
        this.defaultTransitionState = defaultTransitionState == null ? IState.FAILED : defaultTransitionState;
        return this;
    }

    public State addTransition(final ITransitionCondition condition, final IState destinationState) {
        Validate.notNull(condition);
        Validate.notNull(destinationState);

        this.transitions.put(condition, destinationState);
        return this;
    }

    @Override
    public List<ITransitionCondition> getExpectations() {
        final List<ITransitionCondition> expectations = this.transitions.entrySet().stream()
            .filter((entry) -> !IState.FAILED.equals(entry.getValue()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        // If default transition state is not FAILED,
        // include implicit AnyCharacter condition
        // in the list of expectations.
        if(!IState.FAILED.equals(defaultTransitionState)) {
            expectations.add(new AnyCharacter());
        }
        
        return expectations;
    }

    
    @Override
    public IState getNextState(char ch) {
        for(Entry<ITransitionCondition, IState> entry : transitions.entrySet()) {
            if(entry.getKey().isSatisfiedBy(ch)) {
                return entry.getValue();
            }
        }
        return this.defaultTransitionState;
    }

    @Override
    public boolean isTerminal() {
        return this.terminal;
    }

    public State setTerminal(final boolean value) {
        this.terminal = value;
        return this;
    }
}
