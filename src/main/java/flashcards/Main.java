package flashcards;

import java.util.*;
public class Main {
    public static final String MENU = "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats)";

    public static void main(String[] args) {
        String importFile = null;
        String exportFile = null;
        Flashcards flashcards = new Flashcards();
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < args.length - 1; i++) {
            if ("-import".equals(args[i])) {
                importFile = args[i + 1];
                i++;
            } else if ("-export".equals(args[i])) {
                exportFile = args[i + 1];
                i++;
            }
        }

        if (importFile != null) {
            flashcards.importFlashcards(importFile);
        }


        loop:
        while (true) {
            flashcards.getLog().printAndAppendToLog(MENU);
            String input = scanner.nextLine();

            switch (input) {
                case "add":
                    flashcards.addFlashcard();
                    break;
                case "remove":
                    flashcards.removeFlashcard();
                    break;
                case "import":
                    flashcards.importFlashcards();
                    break;
                case "export":
                    flashcards.exportFlashcards();
                    break;
                case "ask":
                    flashcards.askFlashcards();
                    break;
                case "log":
                    flashcards.saveLog();
                    break;
                case "hardest card":
                    flashcards.getHardestCard();
                    break;
                case "reset stats":
                    flashcards.resetStats();
                    break;
                case "exit":
                    System.out.println("Bye bye!");
                    break loop;
                default:
                    System.out.println("Invalid input");
                    break;
            }
        }


        if (exportFile != null) {
            flashcards.exportFlashcards(exportFile);
        }

    }
}
