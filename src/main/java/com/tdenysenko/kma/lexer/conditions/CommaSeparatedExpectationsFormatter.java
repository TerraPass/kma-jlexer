package com.tdenysenko.kma.lexer.conditions;

import java.util.List;

public class CommaSeparatedExpectationsFormatter implements IExpectationsFormatter {
    private static final String END_OF_INPUT_NAME = "end of input";

    private static final String COMMA = ", ";
    private static final String OR = " or ";

    @Override
    public String formatExpectations(List<ITransitionCondition> expectations) {
        if(expectations.isEmpty()) {
            return END_OF_INPUT_NAME;
        }
        
        final StringBuilder resultBuilder = new StringBuilder();
        
        for(int i = 0; i < expectations.size(); i++) {
            if(i != 0) {
                resultBuilder.append(
                    i < expectations.size() - 1
                        ? COMMA
                        : OR
                );
            }
            resultBuilder.append(expectations.get(i).getDescription());
        }

        return resultBuilder.toString();
    }
}
