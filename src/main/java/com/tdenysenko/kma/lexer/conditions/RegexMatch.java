package com.tdenysenko.kma.lexer.conditions;

import org.apache.commons.lang3.Validate;

public class RegexMatch implements ITransitionCondition {
    private final String regex;
    private final String customDescription;

    public RegexMatch(final String regex, final String customDescription) {
        Validate.notBlank(regex, "regex must not be blank");

        this.regex = regex;
        this.customDescription = customDescription;
    }

    public RegexMatch(final String regex) {
        this(regex, null);
    }

    @Override
    public boolean isSatisfiedBy(char ch) {
        return String.valueOf(ch).matches(regex);
    }

    @Override
    public String getDescription() {
        return customDescription == null
            ? String.format("character, matched by regex \"%s\"", this.regex)
            : null;
    }   
}
