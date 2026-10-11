package edu.ic6821.game.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class TextFilePodiumPersistenceTest {
    private static final String DATA_DIR = "src/test/java/edu/ic6821/game/persistence";
    private static final String PODIUM_FILE_PATH = DATA_DIR + "/podium.txt";
    private TextFilePodiumPersistence persistence;
    private static final int SCORE_100 = 100;
    private static final int SCORE_200 = 200;
    private static final int SCORE_300 = 300;
    private static final int MAX_PODIUM_SIZE = 3;


    @BeforeEach
    public void setup() throws IOException {
        persistence = new TextFilePodiumPersistence(PODIUM_FILE_PATH);
        Files.createDirectories(Paths.get(DATA_DIR));

        final var path = Paths.get(PODIUM_FILE_PATH);
        System.out.println(path);
        if (Files.notExists(path)) {
            Files.createFile(path);
        } else {
            Files.write(path, new byte[0]);
        }
    }

    @Test
    public void saveAndLoadSinglePlayerTest() {
        // given
        final Map<String, Integer> podium = new HashMap<>();
        podium.put("AAA", SCORE_100);

        // when
        persistence.savePodium(podium);
        final Map<String, Integer> loadedPodium = persistence.loadPodium();

        // then
        assertEquals(1, loadedPodium.size());
        assertEquals(SCORE_100, loadedPodium.get("AAA"));
    }

    @Test
    public void saveAndLoadMultiplePlayerTest() {
        // given
        final Map<String, Integer> podium = new HashMap<>();
        podium.put("AAA", SCORE_100);
        podium.put("BBB", SCORE_200);
        podium.put("CCC", SCORE_300);

        // when
        persistence.savePodium(podium);
        final Map<String, Integer> loadedPodium = persistence.loadPodium();

        // then
        assertEquals(MAX_PODIUM_SIZE, loadedPodium.size());
        assertEquals(SCORE_100, loadedPodium.get("AAA"));
        assertEquals(SCORE_200, loadedPodium.get("BBB"));
        assertEquals(SCORE_300, loadedPodium.get("CCC"));
    }

    @Test
    public void loadEmptyFileTest() {
        // given

        // when
        final Map<String, Integer> loadedPodium = persistence.loadPodium();

        // then
        assertTrue(loadedPodium.isEmpty());
    }

    @Test
    public void loadFileWithInvalidScoreTest() throws IOException {
        // given
        final String invalidContent = "AAA notANumber\n";
        Files.write(Paths.get(PODIUM_FILE_PATH), invalidContent.getBytes());

        // when
        final Map<String, Integer> loadedPodium = persistence.loadPodium();

        // then
        assertTrue(loadedPodium.isEmpty());
    }

    @Test
    public void loadFileWithExtraSpacesTest() throws IOException {
        // given
        final int podiumSize = 1;
        final String content = "AAA   100   \n";
        Files.write(Paths.get(PODIUM_FILE_PATH), content.getBytes());

        // when
        final Map<String, Integer> loadedPodium = persistence.loadPodium();

        // then
        assertEquals(podiumSize, loadedPodium.size());
        assertEquals(SCORE_100, loadedPodium.get("AAA"));
    }
}
