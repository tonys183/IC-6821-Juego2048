package edu.ic6821.game.podium;

import edu.ic6821.game.persistence.TextFilePodiumPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public final class TopThreePodiumTest {
    private static final String DATA_DIR = "src/test/java/edu/ic6821/game/persistence";
    private static final String PODIUM_FILE_PATH = DATA_DIR + "podium.txt";
    private static final Integer FIRST_SUBJECTIVE_SCORE = 100;
    private static final Integer SECOND_SUBJECTIVE_SCORE = 200;
    private static final Integer THIRD_SUBJECTIVE_SCORE = 300;
    private static final Integer FOURTH_SUBJECTIVE_SCORE = 400;
    private static final TextFilePodiumPersistence TEXT_FILE_PODIUM_PERSISTENCE =
            new TextFilePodiumPersistence(PODIUM_FILE_PATH);

    @BeforeEach
    public void setup() throws IOException {
        Files.createDirectories(Paths.get(DATA_DIR));
        final var path = Paths.get(PODIUM_FILE_PATH);
        if (Files.notExists(path)) {
            Files.createFile(path);
        } else {
            Files.write(path, new byte[0]);
        }
    }

    @Test
    public void fullPodiumToString() {
        // given
        final Podium podium = new TopThreePodium(TEXT_FILE_PODIUM_PERSISTENCE);
        podium.registerScore("AAA", FIRST_SUBJECTIVE_SCORE);
        podium.registerScore("BBB", SECOND_SUBJECTIVE_SCORE);
        podium.registerScore("CCC", THIRD_SUBJECTIVE_SCORE);
        final String fullBoardText =
                "Récord\n"
                        + "CCC 300\n"
                        + "BBB 200\n"
                        + "AAA 100\n";

        // when
        final String result = podium.toString();

        // then
        assertEquals(fullBoardText, result);
    }

    @Test
    public void partialPodiumToString() {
        // given
        final Podium podium = new TopThreePodium(TEXT_FILE_PODIUM_PERSISTENCE);
        podium.registerScore("AAA", FIRST_SUBJECTIVE_SCORE);
        podium.registerScore("BBB", SECOND_SUBJECTIVE_SCORE);
        final String fullBoardText =
                "Récord\n"
                        + "BBB 200\n"
                        + "AAA 100\n";

        // when
        final String result = podium.toString();

        // then
        assertEquals(fullBoardText, result);
    }

    @Test
    public void emptyPodiumToString() {
        // given
        final Podium podium = new TopThreePodium(TEXT_FILE_PODIUM_PERSISTENCE);
        final String fullBoardText = "Récord\n"; 

        // when
        final String result = podium.toString();

        // then
        assertEquals(fullBoardText, result);
    }

    @Test
    public void newHighScorerPlayerPodiumTest() {
        // given
        final Podium podium1 = new TopThreePodium(TEXT_FILE_PODIUM_PERSISTENCE);
        final Podium podium2 = new TopThreePodium(TEXT_FILE_PODIUM_PERSISTENCE);

        podium1.registerScore("AAA", FIRST_SUBJECTIVE_SCORE);
        podium1.registerScore("BBB", SECOND_SUBJECTIVE_SCORE);
        podium1.registerScore("CCC", THIRD_SUBJECTIVE_SCORE);

        podium2.registerScore("AAA", FIRST_SUBJECTIVE_SCORE);
        podium2.registerScore("BBB", SECOND_SUBJECTIVE_SCORE);
        podium2.registerScore("CCC", THIRD_SUBJECTIVE_SCORE);
        podium2.registerScore("DDD", FOURTH_SUBJECTIVE_SCORE);

        final String fullBoardText1 =
                "Récord\n"
                        + "CCC 300\n"
                        + "BBB 200\n"
                        + "AAA 100\n";

        final String fullBoardText2 =
                "Récord\n"
                        + "DDD 400\n"
                        + "CCC 300\n"
                        + "BBB 200\n";

        // when
        final String result1 = podium1.toString();
        final String result2 = podium2.toString();

        // then
        assertEquals(fullBoardText1, result1);
        assertEquals(fullBoardText2, result2);
    }

    @Test
    public void registerBlankPlayerTest() {
        // given
        final Podium podium = new TopThreePodium(TEXT_FILE_PODIUM_PERSISTENCE);

        // when
        final boolean result = podium.registerScore("", FIRST_SUBJECTIVE_SCORE);

        // then
        assertFalse(result);
    }

    @Test
    public void registerNullPlayerTest() {
        // given
        final Podium podium = new TopThreePodium(TEXT_FILE_PODIUM_PERSISTENCE);

        // when
        final boolean result = podium.registerScore(null, FIRST_SUBJECTIVE_SCORE);

        // then
        assertFalse(result);
    }

    @Test
    public void registerPlayerExceedLengthTest() {
        // given
        final Podium podium = new TopThreePodium(TEXT_FILE_PODIUM_PERSISTENCE);

        // when
        final boolean result = podium.registerScore("ABFBS0", FIRST_SUBJECTIVE_SCORE);

        // then
        assertFalse(result);
    }

    @Test
    public void registerPlayerNotUpperCaseTest() {
        // given
        final Podium podium = new TopThreePodium(TEXT_FILE_PODIUM_PERSISTENCE);

        // when
        final boolean result = podium.registerScore("aAa", FIRST_SUBJECTIVE_SCORE);

        // then
        assertFalse(result);
    }

    @Test
    public void registerPlayerNumbersTest() {
        // given
        final Podium podium = new TopThreePodium(TEXT_FILE_PODIUM_PERSISTENCE);

        // when
        final boolean result = podium.registerScore("000", FIRST_SUBJECTIVE_SCORE);

        // then
        assertFalse(result);
    }

    @Test
    public void registerPlayerWithSameNameTest() {
        // given
        final Podium podium = new TopThreePodium(TEXT_FILE_PODIUM_PERSISTENCE);

        podium.registerScore("AAA", FIRST_SUBJECTIVE_SCORE);
        podium.registerScore("BBB", SECOND_SUBJECTIVE_SCORE);
        podium.registerScore("CCC", THIRD_SUBJECTIVE_SCORE);

        // when
        final boolean result = podium.registerScore("AAA", FIRST_SUBJECTIVE_SCORE);

        // then
        assertFalse(result);
    }

    @Test
    public void registerPlayerTest() {
        // given
        final Podium podium = new TopThreePodium(TEXT_FILE_PODIUM_PERSISTENCE);

        // when
        final boolean result = podium.registerScore("AAA", FIRST_SUBJECTIVE_SCORE);

        // then
        assertTrue(result);
    }
}
