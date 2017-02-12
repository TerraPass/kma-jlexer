package com.tdenysenko.kma.lexer.conditions;

import org.apache.commons.lang3.Validate;

public class OneOf implements ITransitionCondition {
    private final String matchedChars;
    private final String customDescription;

    public OneOf(final String chars, final String customDescription) {
        Validate.notNull(chars);

        this.matchedChars = chars;
        this.customDescription = customDescription;
    }

    public OneOf(final String chars) {
        this(chars, null);
    }
    
    @Override
    public boolean isSatisfiedBy(char ch) {
        return this.matchedChars.contains(String.valueOf(ch));
    }

    @Override
    public String getDescription() {
        return customDescription == null
            ? String.format(
                "one of the following characters {%s}",
                formatCharArray(matchedChars.toCharArray())
            )
            : customDescription;
    }

    private String formatCharArray(final char[] chars) {
        final StringBuilder resultBuilder = new StringBuilder();

        for(int i = 0; i < chars.length; i++) {
            if(i != 0) {
                resultBuilder.append(", ");
            }
            resultBuilder.append(String.format("\'%s\'", String.valueOf(chars[i])));
        }

        return resultBuilder.toString();
    }
}
