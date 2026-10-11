package edu.ic6821.game.controller;

import java.util.Optional;

import edu.ic6821.game.board.Board;
import edu.ic6821.game.board.GraphBoard;
import edu.ic6821.game.board.MoveStatus;
import edu.ic6821.game.board.Tile;
import edu.ic6821.game.persistence.TextFileGameDataPersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class WASDGameControllerTest {
   private Optional<Tile>[] board;
   private Board graphBoard;
   private static final int BOARD_SIZE = 4;
    private static final int FIRST_SUBJECTIVE_TILE = 6;
    private static final int SECOND_SUBJECTIVE_TILE = 7;
    private GameController wasdGameController;
    private static final String DATA_DIR = "src/test/java/edu/ic6821/game/persistence";
    private static final String SAVE_GAME_STATE_DIR = DATA_DIR + "/gameSave.txt";
    private static final TextFileGameDataPersistence TEXT_FILE_GAME_DATA_PERSISTENCE =
            new TextFileGameDataPersistence(SAVE_GAME_STATE_DIR);

    @BeforeEach
    public void setup() {
        board =  new Optional[BOARD_SIZE * BOARD_SIZE];
        graphBoard = new GraphBoard(
                BOARD_SIZE,
                board,
                TEXT_FILE_GAME_DATA_PERSISTENCE
        );

        wasdGameController = new WASDGameController();
    }

    @Test
    public void doMoveInvalidCharTest() {
        //given
        final char invalidMove = 'O';

        //when
        final MoveStatus moveStatus = wasdGameController.doMove(invalidMove, graphBoard);

        //then
        assertEquals(MoveStatus.INVALID_MOVE, moveStatus);
    }

    @Test
    public void doMoveBlankCharTest() {
        //given
        final char invalidMove = ' ';

        //when
        final MoveStatus moveStatus = wasdGameController.doMove(invalidMove, graphBoard);

        //then
        assertEquals(MoveStatus.INVALID_MOVE, moveStatus);
    }

    @Test
    public void doMoveQuitTest() {
        //given
        final char quit = 'Q';

        //when
        final MoveStatus moveStatus = wasdGameController.doMove(quit, graphBoard);

        //then
        assertEquals(MoveStatus.QUITED, moveStatus);
    }

    @Test
    public void doMoveWTest() {

        //given
        final char move = 'W';
        graphBoard.initializeBoard();
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            board[i].get().setValue(0);
        }
        board[FIRST_SUBJECTIVE_TILE].get().setValue(2);
        board[SECOND_SUBJECTIVE_TILE].get().setValue(2);

        //when
        final MoveStatus moveStatus = wasdGameController.doMove(move, graphBoard);

        //then
        assertEquals(MoveStatus.COLLISION, moveStatus);
    }

    @Test
    public void doMoveSTest() {
        //given
        final char move = 'S';
        graphBoard.initializeBoard();
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            board[i].get().setValue(0);
        }
        board[FIRST_SUBJECTIVE_TILE].get().setValue(2);
        board[SECOND_SUBJECTIVE_TILE].get().setValue(2);

        //when
        final MoveStatus moveStatus = wasdGameController.doMove(move, graphBoard);

        //then
        assertEquals(MoveStatus.COLLISION, moveStatus);
    }

    @Test
    public void doMoveDTest() {
        //given
        final char move = 'D';
        graphBoard.initializeBoard();
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            board[i].get().setValue(0);
        }
        board[FIRST_SUBJECTIVE_TILE].get().setValue(2);
        board[SECOND_SUBJECTIVE_TILE].get().setValue(2);

        //when
        final MoveStatus moveStatus = wasdGameController.doMove(move, graphBoard);

        //then
        assertEquals(MoveStatus.COLLISION, moveStatus);
    }

    @Test
    public void doMoveATest() {
        //given
        final char move = 'A';
        graphBoard.initializeBoard();
        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
            board[i].get().setValue(0);
        }
        board[FIRST_SUBJECTIVE_TILE].get().setValue(2);
        board[SECOND_SUBJECTIVE_TILE].get().setValue(2);

        //when
        final MoveStatus moveStatus = wasdGameController.doMove(move, graphBoard);

        //then
        assertEquals(MoveStatus.COLLISION, moveStatus);
    }
}
