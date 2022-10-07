package flashcards;

import com.google.common.annotations.VisibleForTesting;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static flashcards.Main.MENU;

public class Flashcards {
    private final String DATA_FILE_PATH = "src/main/java/flashcards/IOFiles/%s";
    private final Map<String, String> flashcards = new LinkedHashMap<>();
    private final Map<String, Integer> mistakes = new HashMap<>();
    private final StringBuilder log = new StringBuilder();


    @VisibleForTesting
    public Map<String, Integer> getMistakes() {
        return mistakes;
    }

    public void printAndAppendToLog(String string) {
        log.append(string).append(System.lineSeparator());
        System.out.println(string);
    }

    private String askForInputStringAndAppendToLog() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        log.append(input).append(System.lineSeparator());
        return input;
    }

    private void appendToLog(String input) {
        log.append(input).append(System.lineSeparator());
    }

    public void addFlashcard() {
        appendToLog("add");
        printAndAppendToLog("The card:");
        String term = askForInputStringAndAppendToLog();
        if (flashcards.containsKey(term)) {
            printAndAppendToLog(String.format("The card \"%s\" already exists.", term));
            return;
        }

        printAndAppendToLog("The definition of the card:");
        String definition = askForInputStringAndAppendToLog();
        if (flashcards.containsValue(definition)) {
            printAndAppendToLog(String.format("The definition \"%s\" already exists.", definition));
            return;
        }

        flashcards.put(term, definition);
        mistakes.put(term, 0);
        printAndAppendToLog(String.format("The pair (\"%s\":\"%s\") has been added.", term, definition));
    }


    public void removeFlashcard() {
        appendToLog("remove");
        printAndAppendToLog("Which card?");
        String input = askForInputStringAndAppendToLog();
        if (flashcards.containsKey(input)) {
            flashcards.remove(input);
            mistakes.remove(input);
            printAndAppendToLog("The card has been removed.");
        } else {
            printAndAppendToLog(String.format("Can't remove \"%s\": there is no such card.", input));
        }
    }


    public void askFlashcards() {
        appendToLog("ask");
        printAndAppendToLog("How many times to ask?");
        int count = Integer.parseInt(askForInputStringAndAppendToLog());
        int asked = 0;
        var iterator = flashcards.entrySet().iterator();

        while (asked < count) {
            if (iterator.hasNext()) {
                askNextQuestion(iterator.next());
            }
            else {
                iterator = flashcards.entrySet().iterator();
                askNextQuestion(iterator.next());
            }
            asked++;
        }
    }


    private void askNextQuestion(Map.Entry<String, String> flashcard) {
        printAndAppendToLog(String.format("Print the definition of \"%s\":", flashcard.getKey()));
        String rightAnswer = flashcard.getValue();
        String userAnswer = askForInputStringAndAppendToLog();

        if (checkAnswer(rightAnswer, userAnswer)) {
            printAndAppendToLog("Correct!");
        } else if(flashcards.containsValue(userAnswer)) {
            String otherTerm = findByDefinition(userAnswer, flashcards);
            printAndAppendToLog(String.format("Wrong. The right answer is \"%s\", but your definition is correct for \"%s\"",
                    flashcard.getValue(), otherTerm));
            mistakes.replace(flashcard.getKey(), mistakes.get(flashcard.getKey()) + 1);
        } else {
            mistakes.replace(flashcard.getKey(), mistakes.get(flashcard.getKey()) + 1);
            printAndAppendToLog(String.format("Wrong. The right answer is \"%s\".", flashcard.getValue()));
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


    public void importFlashcards(String fileName) {
        String filePath = String.format(DATA_FILE_PATH, fileName);
        File file = new File(filePath);

        try(Scanner scanner = new Scanner(file)) {
            int count = 0;
            while (scanner.hasNext()) {
                String[] data = scanner.nextLine().split(":");
                flashcards.put(data[0], data[1]);
                mistakes.put(data[0], Integer.parseInt(data[2]));
                count++;
            }
            printAndAppendToLog(String.format("%d cards have been loaded.", count));
        } catch (FileNotFoundException e) {
            printAndAppendToLog("File not found");
        }
    }

    public void importFlashcards() {
        appendToLog("import");
        printAndAppendToLog("File name:");
        String fileName = askForInputStringAndAppendToLog();

        importFlashcards(fileName);
    }


    public void exportFlashcards() {
        appendToLog("export");
        printAndAppendToLog("File name:");
        String fileName = askForInputStringAndAppendToLog();

        exportFlashcards(fileName);
    }

    public void exportFlashcards(String fileName) {
        String filePath = String.format(DATA_FILE_PATH, fileName);
        File file = new File(filePath);

        try(PrintWriter writer = new PrintWriter(file)) {
            file.createNewFile();
            for (Map.Entry<String, String> entry : flashcards.entrySet()) {
                writer.printf("%s:%s:%s\n", entry.getKey(), entry.getValue(), mistakes.get(entry.getKey()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        printAndAppendToLog(String.format("%d cards have been saved.", flashcards.size()));
    }


    public void saveLog() {
        appendToLog("log");
        printAndAppendToLog("File name:");
        String fileName = askForInputStringAndAppendToLog();
        String filePath = String.format(DATA_FILE_PATH, fileName);

        File file = new File(filePath);
        try(PrintWriter writer = new PrintWriter(file)) {
            file.createNewFile();
            printAndAppendToLog("The log has been saved");
            appendToLog(MENU);
            writer.printf(log.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void getHardestCard() {
        appendToLog("hardest card");
        if (mistakes.isEmpty()) {
            printAndAppendToLog("There are no cards with errors.");
            return;
        }

        List<String> mistakenTermsList = mistakenTermsList(mistakes);

        if (mistakenTermsList.isEmpty()) {
            printAndAppendToLog("There are no cards with errors.");
        } else if (mistakenTermsList.size() == 1) {
            String term = mistakenTermsList.get(0);

            printAndAppendToLog(String.format("The hardest card is \"%s\". You have %d errors answering it.",
                    term, mistakes.get(term)));
        } else {
            String mistakenTermsToString = mistakenTermsList.stream()
                    .map(s -> "\"" + s + "\"")
                    .collect(Collectors.joining(", ", "", "."));
            String firstTerm = mistakenTermsList.get(0);
            printAndAppendToLog(String.format("The hardest cards are %s You have %d errors answering them.",
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
        appendToLog("reset stats");
        for (Map.Entry<String, Integer> entry : mistakes.entrySet()) {
            mistakes.replace(entry.getKey(), 0);
        }
        printAndAppendToLog("Card statistics have been reset.");
    }

}
