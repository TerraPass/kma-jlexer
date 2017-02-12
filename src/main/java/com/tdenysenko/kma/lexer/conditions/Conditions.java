package com.tdenysenko.kma.lexer.conditions;

public final class Conditions {
    private Conditions() {
        
    }

    public final static ITransitionCondition IS_CONSUMABLE_SEPARATOR       = new OneOf(" \t\n\r", "a whitespace");
    public final static ITransitionCondition IS_NON_CONSUMABLE_SEPARATOR   = new OneOf(",.!?:\"()", "a separator");

    public final static ITransitionCondition IS_UKRAINIAN_LETTER = new OneOf(
        "ЙЦУКЕНГШЩЗХЇҐФІВАПРОЛДЖЄЯЧСМИТЬБЮйцукенгшщзхїґфівапролджєячсмитьбю",
        "a Ukrainian letter"
    );
    public final static ITransitionCondition IS_HYPHEN = new ExactMatch('-');

    public final static ITransitionCondition IS_DIGIT = new OneOf("0123456789", "a digit");
    public final static ITransitionCondition IS_DECIMAL_SEPARATOR = new OneOf(",.", "a decimal point or comma");

    public final static ITransitionCondition IS_DATE_SEPARATOR = new OneOf("/.-");
    
    public static boolean isSeparator(final char ch) {
        return isConsumableSeparator(ch) || isNonConsumableSeparator(ch);
    }

    public static boolean isConsumableSeparator(final char ch) {
        return IS_CONSUMABLE_SEPARATOR.isSatisfiedBy(ch);
    }

    public static boolean isNonConsumableSeparator(final char ch) {
        return IS_NON_CONSUMABLE_SEPARATOR.isSatisfiedBy(ch);
    }
}
