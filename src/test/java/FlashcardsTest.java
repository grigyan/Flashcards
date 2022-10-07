import flashcards.Flashcards;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FlashcardsTest {
    private static Stream<Arguments> streamForFindByDefinition() {
        return Stream.of(Arguments.of("Paris", Map.of("France", "Paris",
                "US", "Washington"), "France"),
                Arguments.of("uno", Map.of("two", "dos"), ""));
    }

    private static Stream<Arguments> streamForFindMostMistakes() {
        return Stream.of(Arguments.of(Map.of("Red", 1, "Blue", 2, "Orange", 2),
                List.of("Blue", "Orange")),
                Arguments.of(Map.of("Red", 0, "Blue", 0), List.of()));
    }


    @ParameterizedTest
    @MethodSource("streamForFindByDefinition")
    public void shouldFindByDefinition(String definition, Map<String, String> mapOfFlashcards, String expected) {
        // given
        Flashcards cards = new Flashcards();

        // when
        String actual = cards.findByDefinition(definition, mapOfFlashcards);

        // then
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("streamForFindMostMistakes")
    public void shouldFindMostMistakenCards(Map<String, Integer> mistakes, List<String> expected) {
        // given
        Flashcards cards = new Flashcards();

        // when
        List<String> mistakenTerms = cards.mistakenTermsList(mistakes);

        // then
        assertTrue(expected.containsAll(mistakenTerms));
        assertTrue(mistakenTerms.containsAll(expected));
    }

    @Test
    public void shouldResetStats() {
        // given
        Flashcards cards = new Flashcards();
        cards.importFlashcards("ImportForTests_1.txt");
        // when
        cards.resetStats();
        // then
        assertEquals(Collections.max(cards.getMistakes().values()), 0);
    }

}
