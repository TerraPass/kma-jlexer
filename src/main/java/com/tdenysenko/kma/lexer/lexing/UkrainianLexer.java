package com.tdenysenko.kma.lexer.lexing;

import com.tdenysenko.kma.lexer.fsm.ILexingFSM;
import com.tdenysenko.kma.lexer.fsm.UkrainianWordFSMFactory;
import com.tdenysenko.kma.lexer.fsm.NumberFSMFactory;
import com.tdenysenko.kma.lexer.fsm.PunctuationFSMFactory;
import com.tdenysenko.kma.lexer.fsm.DateFSMFactory;
import com.tdenysenko.kma.lexer.conditions.IExpectationsFormatter;
import com.tdenysenko.kma.lexer.conditions.Conditions;
import com.tdenysenko.kma.lexer.conditions.CommaSeparatedExpectationsFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class UkrainianLexer implements ILexer {
    // Wether to add artificial "\n" to the end of lexed strings.
    private final boolean implyNewlines;

    private final IExpectationsFormatter expectationsFormatter;
    
    private final Map<LexemeType, ILexingFSM> lexingFSMs;

    private final StringBuilder currentLexemeBuilder;
    private LexingError.Builder pendingError;
    
    public UkrainianLexer(final boolean implyNewlines) {
        this.implyNewlines = implyNewlines;

        this.expectationsFormatter = new CommaSeparatedExpectationsFormatter();

        this.lexingFSMs = new HashMap<>();

        this.currentLexemeBuilder = new StringBuilder();
        this.pendingError = null;

        // Create FSMs
        this.lexingFSMs.put(LexemeType.WORD, new UkrainianWordFSMFactory().create());
        this.lexingFSMs.put(LexemeType.PUNCTUATION, new PunctuationFSMFactory().create());
        this.lexingFSMs.put(LexemeType.NUMBER, new NumberFSMFactory().create());
        this.lexingFSMs.put(LexemeType.DATE, new DateFSMFactory().create());
    }

    @Override
    public LexingResult lex(final String inputString) {
        return this.lex(inputString, null, null);
    }

    @Override
    public LexingResult lex(final String inputString, final ILexemeProcessedListener onLexemeProcessed, final ILexingErrorHandler onLexingError) {
        final LexingResult.Builder resultBuilder = new LexingResult.Builder();

        for(char ch : inputString.toCharArray()) {
            this.processChar(ch, resultBuilder, onLexemeProcessed, onLexingError);
        }
        
        if(implyNewlines) {
            this.processChar('\n', resultBuilder, onLexemeProcessed, onLexingError);
        }

        return resultBuilder.build();
    }

    private void processChar(
        final char ch,
        final LexingResult.Builder resultBuilder,
        final ILexemeProcessedListener onLexemeProcessed, 
        final ILexingErrorHandler onLexingError
    ) {
        // If we have reached a separator, check what we have detected so far,
        // if anything, and report lexeme detection or error accordingly.
        if(pendingError == null && Conditions.isSeparator(ch)) {
            final String lexemeValue = currentLexemeBuilder.toString();

            final Set<LexemeType> detectedLexemeTypes = this.getSuccessfulFSMs().keySet();

            if(detectedLexemeTypes.size() > 0) {    // If successfully detected
                // Pick any one of the lexeme types, corresponding to successful FSMs.
                final LexemeType lexemeType = detectedLexemeTypes.iterator().next();                    

                final Lexeme lexeme = new Lexeme(lexemeType, lexemeValue);

                resultBuilder.withAnotherLexeme(lexeme);
                if(onLexemeProcessed != null) {
                    onLexemeProcessed.onLexemeProcessed(lexeme);
                }
            } else {    // If failed to recognize a lexeme
                final Map<LexemeType, ILexingFSM> pendingFSMs = this.getPendingFSMs();

                assert pendingFSMs.size() > 0;

                // Pick any one of the lexeme types, corresponding to still pending FSMs.
                final Entry<LexemeType, ILexingFSM> pendingEntry = pendingFSMs.entrySet().iterator().next();

                final LexingError lexingError = new LexingError(
                    LexingError.Type.UNEXPECTED_END_OF_INPUT,
                    lexemeValue,
                    pendingEntry.getKey(),
                    pendingEntry.getValue().describeExpectations(expectationsFormatter),
                    currentLexemeBuilder.length()
                );

                resultBuilder.withAnotherError(lexingError);
                if(onLexingError != null) {
                    onLexingError.onLexingError(lexingError);
                }
            }

            this.resetAllFSMs();
            currentLexemeBuilder.setLength(0);  // Clear and start tracking next lexeme

            if(Conditions.isConsumableSeparator(ch)) {
                return;
            }
        }

        // Append current character to current lexeme
        currentLexemeBuilder.append(ch);

        // If we are currently recovering from an error, 
        // simply ignore everything until we get
        // to the next separator
        if(pendingError != null && !Conditions.isSeparator(ch)) {
            return;
        } else if(pendingError != null) {
            final LexingError error = pendingError.withQuasiLexeme(currentLexemeBuilder.toString()).build();
            this.pendingError = null;

            resultBuilder.withAnotherError(error);
            if(onLexingError != null) {
                onLexingError.onLexingError(error);
            }

            this.resetAllFSMs();
            currentLexemeBuilder.setLength(0);  // Clear and start tracking next lexeme

            if(Conditions.isConsumableSeparator(ch)) {
                return;
            }
        }

        // FSMs not yet in failed state
        final Map<LexemeType, ILexingFSM> initiallyPendingFSMs = this.getPendingFSMs();

        assert !initiallyPendingFSMs.isEmpty() : "There must be at least 1 pending FSM at this point";

        // Take the first out of previously pending FSMs as the candidate
        final Entry<LexemeType, ILexingFSM> candidateEntry = initiallyPendingFSMs.entrySet().iterator().next();
        // Cache expectations for possible use in LexingError building.
        // (Expectations will change to "end of input", if this candidate FSM fails on this iteration,
        // therefore they need to be cached before the iteration for correct error reporting.)
        final String expectations = candidateEntry.getValue().describeExpectations(this.expectationsFormatter);

        for(Entry<LexemeType, ILexingFSM> entry : initiallyPendingFSMs.entrySet()) {
            entry.getValue().processChar(ch);
        }

        // If the last pending FSMs have failed to parse
        if(this.getPendingFSMs().isEmpty()) {
            // Set pending unexpected character error
            pendingError = new LexingError.Builder(
                LexingError.Type.UNEXPECTED_CHARACTER,
                candidateEntry.getKey(),
                expectations,
                currentLexemeBuilder.length(),  // problematic position
                ch
            );
        }
    }

    private Map<LexemeType, ILexingFSM> getPendingFSMs() {
        return lexingFSMs.entrySet().stream()
            .filter((entry) -> !entry.getValue().isInFailedState())
            .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    private Map<LexemeType, ILexingFSM> getSuccessfulFSMs() {
        return lexingFSMs.entrySet().stream()
            .filter((entry) -> entry.getValue().isInTerminalState())
            .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    private void resetAllFSMs() {
        this.lexingFSMs.values().stream().forEach((fsm) -> fsm.reset());
    }
}
