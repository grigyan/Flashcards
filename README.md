# Flashcards

### What is a Flashcard? 🃏
A card that has the term and its definition on it. 

## About ℹ️
With this program you can create your own cards for just any term, check
your knowledge of cards, import cards from the file
and export cards that you created to the export file.

## To Start The Program 🚀
Run `Main.java` file inside `src/main/java/flashcards` folder.

## Menu Of The Program Explained 🔬

1. `add`: Create new flashcard with its term and definition. You will be asked
to provide the term first and then the definition.

2. `remove`: Delete the card. You will be asked to enter the term of the card
that you want to delete.

3. `import`: Import cards from the import file that you specify. You should
enter the full name of the file including the file type (e.g. `importFile.txt`).
The file should be located in `src/main/java/flashcards/resources` folder.

4. `export`: Export cards to the file that yoyu specify. File will be
saved in `src/main/java/flashcards/resources` directory. You should provide the
full name for the file (e.g `exportFile.txt`).

5. `ask`: Ask you the cards that you created and/or imported. After choosing this
option, additionally provide number of times you want the program to ask questions.

6. `exit`: Simply exits the program

7. `log`: Saves the log of the program to the file. File will be saved in 
`src/main/java/flashcards/log` directory. You should provide full name for
the file (e.g. `log.txt`).

8. `hardest card`: Program will show you the card that you answered wrong for the highest number of times.
9. `reset stats`: Will reset the mistake count to zero for all cards.

## Data Format For Input/Output Files. 🪢

When creating flashcards for importing or when they are being exported 
to export file, information will be saved in the follwing format:

`term:defintion:number_of_mistakes`