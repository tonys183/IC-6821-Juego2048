package edu.ic6821.game.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public final class TextFileGameDataPersistenceTest {
    private static final String DATA_DIR = "src/test/java/edu/ic6821/game/persistence";
    private static final String SAVE_FILE = DATA_DIR + "/gameSave.txt";
    private static final int SCORE_100 = 100;
    private static final int TILE_VALUE = 3;
    private GameDataPersistence persistence;

    @BeforeEach
    public void setup() throws IOException {
        persistence = new TextFileGameDataPersistence(SAVE_FILE);
        Files.createDirectories(Paths.get(DATA_DIR));

        final var path = Paths.get(SAVE_FILE);
        if (Files.notExists(path)) {
            Files.createFile(path);
        } else {
            Files.write(path, new byte[0]);
        }
    }

    @Test
    public void saveGameStateTest() {
        // given
        final Integer[] board = {2, 4, 8, 16};
        final Integer score = SCORE_100;

        // when
        persistence.saveGameState(board, score);

        // then
        assertTrue(persistence.hasSavedGame());
    }

    @Test
    public void loadGameStateTest() {
        // given
        final Integer[] board = {2, 4, 8, 16};
        final Integer score = SCORE_100;

        // when
        persistence.saveGameState(board, score);

        // then
        assertArrayEquals(board, persistence.loadBoardValues().orElse(null));
        assertEquals(score, persistence.loadCurrentScore());
    }

    @Test
    public void eraseGameStateTest() {
        // given
        persistence.saveGameState(new Integer[]{1, 2, TILE_VALUE}, SCORE_100);

        // when
        persistence.eraseGameState();

        // then
        assertNull(persistence.loadBoardValues().orElse(null));
        assertEquals(0, persistence.loadCurrentScore());
    }

    @Test
    public void fileNotFoundTest() throws IOException {
        // given
        final var path = Paths.get(SAVE_FILE);
        Files.deleteIfExists(path);

        // when
        final Optional<Integer[]> boardValues = persistence.loadBoardValues();
        final Integer currentScore = persistence.loadCurrentScore();

        // then
        assertNull(boardValues.orElse(null));
        assertEquals(0, currentScore);
    }
}
