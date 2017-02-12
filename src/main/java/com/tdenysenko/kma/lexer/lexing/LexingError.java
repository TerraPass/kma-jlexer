package com.tdenysenko.kma.lexer.lexing;

import org.apache.commons.lang3.Validate;

public class LexingError {

    public static enum Type {
        UNEXPECTED_CHARACTER("Unexpected character"),
        UNEXPECTED_END_OF_INPUT("Unexpected end of input");

        private final String description;

        private Type(final String description) {
            assert description != null;

            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    private final Type errorType;
    private final String quasiLexeme;
    private final LexemeType candidateType;
    private final String expectationDetails;
    private final int problematicPosition;

    public LexingError(
        final Type type, 
        final String quasiLexeme, 
        final LexemeType candidateType, 
        final String expectationDetails,
        final int problematicPosition
    ) {
        Validate.notNull(type);
        Validate.notNull(quasiLexeme);
        Validate.notNull(candidateType);
        Validate.notNull(expectationDetails);

        this.errorType = type;
        this.quasiLexeme = quasiLexeme;
        this.candidateType = candidateType;
        this.expectationDetails = expectationDetails;
        this.problematicPosition = problematicPosition;
    }

    public String getQuasiLexeme() {
        return quasiLexeme;
    }

    public LexemeType getCandidateType() {
        return candidateType;
    }

    public Type getErrorType() {
        return errorType;
    }

    public String getExpectationDetails() {
        return expectationDetails;
    }

    public int getProblematicPosition() {
        return problematicPosition;
    }    
    
    public static class Builder {
        private final Type errorType;
        private String quasiLexeme;
        private final LexemeType candidateType;
        private final String expectations;
        private final int problematicPosition;
        private Character problematicChar;

        private static String formatMessage(final String expectations, final Character problematicChar) {
            return String.format(
                "expected %s, got %s", 
                expectations, 
                problematicChar == null
                    ? "end of input"
                    : String.format("\'%s\'", String.valueOf(problematicChar))
            );
        }

        public Builder(
            final Type errorType, 
            final LexemeType candidateType, 
            final String expectations, 
            final int problematicPosition,
            final Character problematicChar
        ) {
            Validate.notNull(errorType);
            Validate.notNull(candidateType);
            Validate.notNull(expectations);

            this.errorType = errorType;
            this.candidateType = candidateType;
            this.expectations = expectations;
            this.problematicPosition = problematicPosition;
            this.problematicChar = problematicChar;
        }

        public Builder(
            final Type errorType, 
            final LexemeType candidateType, 
            final String expectations,
            final int problematicPosition
        ) {
            this(errorType, candidateType, expectations, problematicPosition, null);
        }
        
        public Builder withQuasiLexeme(final String quasiLexeme) {
            this.quasiLexeme = quasiLexeme;
            return this;
        }

        public Builder withProblematicChar(Character problematicChar) {
            this.problematicChar = problematicChar;
            return this;
        }

        public LexingError build() {
            return this.quasiLexeme != null
                ? new LexingError(errorType, quasiLexeme, candidateType, formatMessage(this.expectations, this.problematicChar), problematicPosition)
                : null;
        }
    }
}
