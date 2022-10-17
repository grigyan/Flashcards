package flashcards.util;

import flashcards.Flashcards;
import flashcards.LogHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

import static flashcards.util.Paths.DATA_FILE_PATH;

public class ImportFlashcards {
    private final Map<String, String> flashcards;
    private final Map<String, Integer> mistakes;
    private final LogHandler log;

    public ImportFlashcards(Flashcards flashcards) {
        this.flashcards = flashcards.getFlashcards();
        this.mistakes = flashcards.getMistakes();
        this.log = flashcards.getLog();
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
            log.printAndAppendToLog(String.format("%d cards have been loaded.", count));
        } catch (FileNotFoundException e) {
            log.printAndAppendToLog("File not found");
        }
    }

    public void importFlashcards() {
        log.appendToLog("import");
        log.printAndAppendToLog("File name:");
        String fileName = log.askForInputStringAndAppendToLog();

        importFlashcards(fileName);
    }
}
