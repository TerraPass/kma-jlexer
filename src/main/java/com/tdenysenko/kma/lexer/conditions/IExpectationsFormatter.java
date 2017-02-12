package com.tdenysenko.kma.lexer.conditions;

import java.util.List;

public interface IExpectationsFormatter {
    String formatExpectations(final List<ITransitionCondition> expectations);
}
