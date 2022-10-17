package flashcards.util;

import flashcards.Flashcards;
import flashcards.LogHandler;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static flashcards.util.Paths.DATA_FILE_PATH;

public class ExportFlashcards {
    private LogHandler log;
    private Flashcards flashcards;

    public ExportFlashcards(Flashcards flashcards) {
        this.log = flashcards.getLog();
        this.flashcards = flashcards;
    }

    public void exportFlashcards() {
        log.appendToLog("export");
        log.printAndAppendToLog("File name:");
        String fileName = log.askForInputStringAndAppendToLog();

        exportFlashcards(fileName);
    }

    public void exportFlashcards(String fileName) {
        String filePath = String.format(DATA_FILE_PATH, fileName);
        File file = new File(filePath);

        try(PrintWriter writer = new PrintWriter(file)) {
            file.createNewFile();
            for (Map.Entry<String, String> entry : flashcards.getFlashcards().entrySet()) {
                writer.printf("%s:%s:%s\n", entry.getKey(), entry.getValue(), flashcards.getMistakes().get(entry.getKey()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.printAndAppendToLog(String.format("%d cards have been saved.", flashcards.getFlashcards().size()));
    }
}
