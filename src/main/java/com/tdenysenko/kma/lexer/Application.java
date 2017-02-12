package com.tdenysenko.kma.lexer;

import com.tdenysenko.kma.lexer.lexing.UkrainianLexer;
import com.tdenysenko.kma.lexer.lexing.Lexeme;
import com.tdenysenko.kma.lexer.lexing.ILexer;
import com.tdenysenko.kma.lexer.lexing.LexingError;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Application {
    private static final boolean STEP_BY_STEP = true;

    private static Stream<String> getStdinStream() {
        return new BufferedReader(new InputStreamReader(System.in)).lines();
    }

    private static Stream<String> getFileStream(String filename) throws IOException {
        return Files.lines(Paths.get(filename));
    }

    public static void main(String[] args) {
        try {
            final ILexer lexer = new UkrainianLexer(true);

            try(final Stream<String> inputStream = args.length > 0
                ? getFileStream(args[0])
                : getStdinStream()) {

                inputStream.forEach(
                    (str) -> lexer.lex(
                        str,
                        (lexeme) -> {
                            System.out.print(formatLexeme(lexeme));
                            onEndOfStep();
                        },
                        (error) -> {
                            System.err.print(formatLexingError(error));
                            onEndOfStep();
                        }
                    )
                );
            }
        } catch (IOException e) {
            System.err.printf("ERROR: Failed to open file %s (%s)", args[0], e.getMessage());
        }
    }

    private static String formatLexeme(final Lexeme lexeme) {
        return String.format(
            "%s\t: %s%s", 
            lexeme.getValue(), 
            lexeme.getType(),
            STEP_BY_STEP ? "" : "\n"
        );
    }

    private static String formatLexingError(final LexingError error) {
        return String.format(
            "%s\t: ERROR: %s at position %d. Candidate: %s (%s)%s",
            error.getQuasiLexeme(),
            error.getErrorType().getDescription(),
            error.getProblematicPosition(),
            error.getCandidateType().toString(),
            error.getExpectationDetails(),
            STEP_BY_STEP ? "" : "\n"
        );
    }

    private static void onEndOfStep() {
        if(!STEP_BY_STEP) {
            return;            
        }

        try {
            System.in.read();
        } catch (IOException e) {}
    }
}
