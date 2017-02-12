package com.tdenysenko.kma.lexer.lexing;

public interface ILexer {
    public interface ILexemeProcessedListener {
        void onLexemeProcessed(final Lexeme lexeme);
    }

    public interface ILexingErrorHandler {
        void onLexingError(final LexingError error);
    }

    LexingResult lex(final String inputString);
    LexingResult lex(final String inputString, final ILexemeProcessedListener onLexemeProcessed, final ILexingErrorHandler onLexingError);
}
