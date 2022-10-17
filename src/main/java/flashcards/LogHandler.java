package flashcards;

import java.util.Scanner;

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

    public StringBuilder getLog() {
        return log;
    }

    @Override
    public String toString() {
        return log.toString();
    }
}
