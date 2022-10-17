package flashcards;

import com.google.common.annotations.VisibleForTesting;
import flashcards.util.ImportFlashcards;

import java.util.*;
import java.util.stream.Collectors;

public class Flashcards {
    private final Map<String, String> flashcards = new LinkedHashMap<>();
    private final Map<String, Integer> mistakes = new HashMap<>();
    private final LogHandler log = new LogHandler();
    private final ImportFlashcards importFlashcards = new ImportFlashcards(this);


    public LogHandler getLog() {
        return this.log;
    }

    public Map<String, String> getFlashcards() {
        return this.flashcards;
    }

    @VisibleForTesting
    public Map<String, Integer> getMistakes() {
        return mistakes;
    }

    public void addFlashcard() {
        log.appendToLog("add");
        log.printAndAppendToLog("The card:");
        String term = log.askForInputStringAndAppendToLog();
        if (flashcards.containsKey(term)) {
            log.printAndAppendToLog(String.format("The card \"%s\" already exists.", term));
            return;
        }

        log.printAndAppendToLog("The definition of the card:");
        String definition = log.askForInputStringAndAppendToLog();
        if (flashcards.containsValue(definition)) {
            log.printAndAppendToLog(String.format("The definition \"%s\" already exists.", definition));
            return;
        }

        flashcards.put(term, definition);
        mistakes.put(term, 0);
        log.printAndAppendToLog(String.format("The pair (\"%s\":\"%s\") has been added.", term, definition));
    }


    public void removeFlashcard() {
        log.appendToLog("remove");
        log.printAndAppendToLog("Which card?");
        String input = log.askForInputStringAndAppendToLog();
        if (flashcards.containsKey(input)) {
            flashcards.remove(input);
            mistakes.remove(input);
            log.printAndAppendToLog("The card has been removed.");
        } else {
            log.printAndAppendToLog(String.format("Can't remove \"%s\": there is no such card.", input));
        }
    }


    public void askFlashcards() {
        log.appendToLog("ask");
        log.printAndAppendToLog("How many times to ask?");
        int count = Integer.parseInt(log.askForInputStringAndAppendToLog());
        int asked = 0;
        var iterator = flashcards.entrySet().iterator();

        while (asked < count) {
            if (iterator.hasNext()) {
                askNextQuestion(iterator.next());
            } else {
                iterator = flashcards.entrySet().iterator();
                askNextQuestion(iterator.next());
            }
            asked++;
        }
    }


    private void askNextQuestion(Map.Entry<String, String> flashcard) {
        log.printAndAppendToLog(String.format("Print the definition of \"%s\":", flashcard.getKey()));
        String rightAnswer = flashcard.getValue();
        String userAnswer = log.askForInputStringAndAppendToLog();

        if (checkAnswer(rightAnswer, userAnswer)) {
            log.printAndAppendToLog("Correct!");
        } else if (flashcards.containsValue(userAnswer)) {
            String otherTerm = findByDefinition(userAnswer, flashcards);
            log.printAndAppendToLog(String.format("Wrong. The right answer is \"%s\", but your definition is correct for \"%s\"",
                    flashcard.getValue(), otherTerm));
            mistakes.replace(flashcard.getKey(), mistakes.get(flashcard.getKey()) + 1);
        } else {
            mistakes.replace(flashcard.getKey(), mistakes.get(flashcard.getKey()) + 1);
            log.printAndAppendToLog(String.format("Wrong. The right answer is \"%s\".", flashcard.getValue()));
        }
    }


    private boolean checkAnswer(String definition, String answer) {
        return Objects.equals(definition, answer);
    }


    @VisibleForTesting
    public String findByDefinition(String userDefinition, Map<String, String> flashcards) { //find by value in map
        for (Map.Entry<String, String> card : flashcards.entrySet()) {
            String cardTerm = card.getKey();
            String cardDefinition = card.getValue();

            if (Objects.equals(cardDefinition, userDefinition)) {
                return cardTerm;
            }
        }

        return "";
    }


    public void getHardestCard() {
        log.appendToLog("hardest card");
        if (mistakes.isEmpty()) {
            log.printAndAppendToLog("There are no cards with errors.");
            return;
        }

        List<String> mistakenTermsList = mistakenTermsList(mistakes);

        if (mistakenTermsList.isEmpty()) {
            log.printAndAppendToLog("There are no cards with errors.");
        } else if (mistakenTermsList.size() == 1) {
            String term = mistakenTermsList.get(0);

            log.printAndAppendToLog(String.format("The hardest card is \"%s\". You have %d errors answering it.",
                    term, mistakes.get(term)));
        } else {
            String mistakenTermsToString = mistakenTermsList.stream()
                    .map(s -> "\"" + s + "\"")
                    .collect(Collectors.joining(", ", "", "."));
            String firstTerm = mistakenTermsList.get(0);
            log.printAndAppendToLog(String.format("The hardest cards are %s You have %d errors answering them.",
                    mistakenTermsToString, mistakes.get(firstTerm)));
        }
    }

    @VisibleForTesting
    public List<String> mistakenTermsList(Map<String, Integer> mistakes) {
        int maxMistakes = Collections.max(mistakes.values());

        if (maxMistakes == 0) {
            return List.of();
        }

        return mistakes.entrySet().stream()
                .filter(entry -> entry.getValue().equals(maxMistakes))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }


    public void resetStats() {
        log.appendToLog("reset stats");
        for (Map.Entry<String, Integer> entry : mistakes.entrySet()) {
            mistakes.replace(entry.getKey(), 0);
        }
        log.printAndAppendToLog("Card statistics have been reset.");
    }

}
