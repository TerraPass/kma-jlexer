package com.tdenysenko.kma.lexer.lexing;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Validate;

public class LexingResult {
    private final List<Lexeme> lexemes;
    private final List<LexingError> errors;

    private LexingResult(final List<Lexeme> lexemes, final List<LexingError> errors) {
        Validate.notNull(lexemes);
        Validate.notNull(errors);
        
        this.lexemes = new ArrayList<>(lexemes);
        this.errors = new ArrayList<>(errors);
    }

    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }
    
    public int getErrorCount() {
        return this.errors.size();
    }

    public List<Lexeme> getLexemes() {
        return this.lexemes;
    }

    public List<LexingError> getLexingErrors() {
        return this.errors;
    }

    public static class Builder {
        private final List<Lexeme> lexemes;
        private final List<LexingError> errors;

        public Builder() {
            this.lexemes = new ArrayList<>();
            this.errors = new ArrayList<>();
        }

        public Builder withAnotherLexeme(final Lexeme lexeme) {
            this.lexemes.add(lexeme);
            return this;
        }

        public Builder withoutLexemes(final Lexeme lexeme) {
            this.lexemes.clear();
            return this;
        }

        public Builder withAnotherError(final LexingError error) {
            this.errors.add(error);
            return this;
        }

        public Builder withoutErrors() {
            this.errors.clear();
            return this;
        }
        
        public LexingResult build() {
            return new LexingResult(this.lexemes, this.errors);
        }
    }
}
