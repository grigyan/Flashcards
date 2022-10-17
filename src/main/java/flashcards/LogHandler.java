package flashcards;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import static flashcards.util.Paths.DATA_FILE_PATH;
import static flashcards.util.Paths.LOG_PATH;

public class LogHandler {
    private final StringBuilder log = new StringBuilder();

    public void printAndAppendToLog(String string) {
        log.append(string).append(System.lineSeparator());
        System.out.println(string);
    }

    public String askForInputStringAndAppendToLog() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        log.append(input).append(System.lineSeparator());
        return input;
    }

    public void appendToLog(String input) {
        log.append(input).append(System.lineSeparator());
    }


    @Override
    public String toString() {
        return log.toString();
    }

    public void saveLog() {
        appendToLog("log");
        printAndAppendToLog("File name:");
        String fileName = askForInputStringAndAppendToLog();
        String filePath = String.format(LOG_PATH, fileName);

        File file = new File(filePath);
        try(PrintWriter writer = new PrintWriter(file)) {
            file.createNewFile();
            printAndAppendToLog("The log has been saved");

            writer.printf(log.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
