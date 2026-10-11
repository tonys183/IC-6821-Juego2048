package edu.ic6821.game.board;

import edu.ic6821.game.persistence.TextFileGameDataPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class GraphBoardTest {
    private Board graphBoard;
    private Optional<Tile>[] board;
    private static final Integer BOARD_SIZE = 4;
    private static final Integer FIFTH_NODE = 4;
    private static final Integer SECOND_NODE = 1;
    private static final Integer FIRST_NODE = 0;
    private static final Integer VICTORY_VALUE = 2048;
    private static final int EXPECTED_ADDITION = 4;
    private static final String DATA_DIR = "src/main/java/edu/ic6821/game/persistence";
    private static final String SAVE_GAME_STATE_DIR = DATA_DIR + "/gameSave.txt";
    private static final TextFileGameDataPersistence TEXT_FILE_GAME_DATA_PERSISTENCE =
            new TextFileGameDataPersistence(SAVE_GAME_STATE_DIR);

    @BeforeEach
    public void setup() throws IOException {
        board =  new Optional[BOARD_SIZE * BOARD_SIZE];
        graphBoard = new GraphBoard(
                BOARD_SIZE,
                board,
                TEXT_FILE_GAME_DATA_PERSISTENCE
        );

        Files.createDirectories(Paths.get(DATA_DIR));
        final var path = Paths.get(SAVE_GAME_STATE_DIR);
        if (Files.notExists(path)) {
            Files.createFile(path);
        } else {
            Files.write(path, new byte[0]);
        }
    }

    @Test
    public void randomBoardToString() {
        // given
        graphBoard.initializeBoard();

        // when
        final String boardString = graphBoard.getCurrentState().getBoardText();

        // then
        System.out.println(boardString);
    }

    @Test
    public void initializeBoardTest() {
        // given
        final int nonZeroTiles = 2;
        graphBoard.initializeBoard();

        // when
        final long nonZeroCount = java.util.Arrays.stream(board).filter(t -> t.get().getValue() != 0).count();

        // then
        assertEquals(BOARD_SIZE * BOARD_SIZE, board.length);
        assertNotNull(board[FIRST_NODE].get().getRight().orElse(null));
        assertNull(board[FIRST_NODE].get().getLeft().orElse(null));
        assertEquals(board[FIRST_NODE], board[SECOND_NODE].get().getLeft());
        assertEquals(nonZeroTiles, nonZeroCount);
    }

    @Test
    public void checkWinTest() {
        // given
        graphBoard.initializeBoard();
        board[FIRST_NODE].get().setValue(VICTORY_VALUE);

        // when
        final Tile[] previousBoard = graphBoard.getCurrentState().getCurrentTiles();
        final Integer previousScore = graphBoard.getCurrentState().getCurrentScore();
        graphBoard.moveUp();
        final MoveStatus status = graphBoard.checkMovement(previousBoard, previousScore);

        // then
        assertEquals(MoveStatus.WIN, status);
    }

    @Test
    public void checkDefeatTest() {
        // given
        graphBoard.initializeBoard();
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            board[i].get().setValue(i + 1);
        }

        // when
        final Tile[] previousBoard = graphBoard.getCurrentState().getCurrentTiles();
        final Integer previousScore = graphBoard.getCurrentState().getCurrentScore();
        graphBoard.moveUp();
        final MoveStatus status = graphBoard.checkMovement(previousBoard, previousScore);

        // then
        assertEquals(MoveStatus.DEFEAT, status);
    }

    @Test
    public void checkCollisionOneFreeTileTest() {
        // given
        graphBoard.initializeBoard();
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE - 1; i++) {
            board[i].get().setValue(i);
        }


        // when
        final Tile[] previousBoard = graphBoard.getCurrentState().getCurrentTiles();
        final Integer previousScore = graphBoard.getCurrentState().getCurrentScore();

        graphBoard.moveRight();
        final MoveStatus status = graphBoard.checkMovement(previousBoard, previousScore);

        // then
        assertEquals(MoveStatus.COLLISION, status);
    }

    @Test
    public void checkMergeUpTest() {
        // given
        graphBoard.initializeBoard();
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            board[i].get().setValue(0);
        }
        board[FIRST_NODE].get().setValue(2);
        board[FIFTH_NODE].get().setValue(2);

        // when
        final Tile[] previousBoard = graphBoard.getCurrentState().getCurrentTiles();
        final Integer previousScore = graphBoard.getCurrentState().getCurrentScore();
        graphBoard.moveUp();
        final MoveStatus status = graphBoard.checkMovement(previousBoard, previousScore);
        final int newScore = graphBoard.getCurrentState().getCurrentScore();

        // then
        assertEquals(MoveStatus.COLLISION, status);
        assertEquals(previousScore + EXPECTED_ADDITION, newScore);
    }

    @Test
    public void checkMergeLeftTest() {
        // given
        graphBoard.initializeBoard();
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            board[i].get().setValue(0);
        }
        board[FIRST_NODE].get().setValue(2);
        board[SECOND_NODE].get().setValue(2);

        // when
        final Tile[] previousBoard = graphBoard.getCurrentState().getCurrentTiles();
        final Integer previousScore = graphBoard.getCurrentState().getCurrentScore();
        graphBoard.moveLeft();
        final MoveStatus status = graphBoard.checkMovement(previousBoard, previousScore);
        final int newScore = graphBoard.getCurrentState().getCurrentScore();

        // then
        assertEquals(MoveStatus.COLLISION, status);
        assertEquals(previousScore + EXPECTED_ADDITION, newScore);
    }

    @Test
    public void checkMergeDownTest() {
        // given
        graphBoard.initializeBoard();
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            board[i].get().setValue(0);
        }
        board[FIRST_NODE].get().setValue(2);
        board[FIFTH_NODE].get().setValue(2);

        // when
        final Tile[] previousBoard = graphBoard.getCurrentState().getCurrentTiles();
        final Integer previousScore = graphBoard.getCurrentState().getCurrentScore();
        graphBoard.moveDown();
        final MoveStatus status = graphBoard.checkMovement(previousBoard, previousScore);
        final int newScore = graphBoard.getCurrentState().getCurrentScore();

        // then
        assertEquals(MoveStatus.COLLISION, status);
        assertEquals(previousScore + EXPECTED_ADDITION, newScore);
    }

    @Test
    public void checkMergeRightTest() {
        // given
        graphBoard.initializeBoard();
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            board[i].get().setValue(0);
        }
        board[FIRST_NODE].get().setValue(2);
        board[SECOND_NODE].get().setValue(2);

        // when
        final Tile[] previousBoard = graphBoard.getCurrentState().getCurrentTiles();
        final Integer previousScore = graphBoard.getCurrentState().getCurrentScore();
        graphBoard.moveRight();
        final MoveStatus status = graphBoard.checkMovement(previousBoard, previousScore);
        final int newScore = graphBoard.getCurrentState().getCurrentScore();

        // then
        assertEquals(MoveStatus.COLLISION, status);
        assertEquals(previousScore + EXPECTED_ADDITION, newScore);
    }

    @Test
    public void hasNotChangeTest() {
        // given
        graphBoard.initializeBoard();
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            board[i].get().setValue(0);
        }
        board[FIRST_NODE].get().setValue(2);
        board[FIFTH_NODE].get().setValue(2);

        // when
        final Tile[] previousBoard = graphBoard.getCurrentState().getCurrentTiles();
        final Integer previousScore = graphBoard.getCurrentState().getCurrentScore();
        graphBoard.moveLeft();
        final MoveStatus status = graphBoard.checkMovement(previousBoard, previousScore);

        // then
        assertEquals(MoveStatus.INVALID_MOVE, status);
    }
}
