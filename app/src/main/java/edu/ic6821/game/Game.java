package edu.ic6821.game;

import edu.ic6821.game.board.Board;
import edu.ic6821.game.board.GraphBoard;
import edu.ic6821.game.board.MoveStatus;
import edu.ic6821.game.controller.WASDGameController;
import edu.ic6821.game.controller.GameController;
import edu.ic6821.game.persistence.TextFileGameDataPersistence;
import edu.ic6821.game.persistence.TextFilePodiumPersistence;
import edu.ic6821.game.podium.TopThreePodium;
import edu.ic6821.game.podium.Podium;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

public final class Game {

    private static final String SEMANTIC_VERSION = "0.0.1";
    private static final Integer BOARD_SIZE = 4;
    private static final int KEY_ENTER = 10;
    private static final int KEY_CR = 13;
    private static final int KEY_SPACE = 32;
    private static final int END_OF_STREAM = -1;
    private static final String VALID_KEYS = "WASDQ";
    private static final String[] STTY_RAW = {"/bin/sh", "-c", "stty raw -echo < /dev/tty"};
    private static final String[] STTY_NORMAL = {"/bin/sh", "-c", "stty sane < /dev/tty"};
    private static final String MSG_WIN = "¡Felicidades! Has ganado el juego.";
    private static final String MSG_DEFEAT = "¡Lo siento! Has perdido el juego.";
    private static final String DATA_DIR = "app/src/main/java/edu/ic6821/game/persistence";
    private static final String SAVE_GAME_STATE_DIR = DATA_DIR + "/gameSave.txt";
    private static final String SAVE_PODIUM_DIR = DATA_DIR + "/podium.txt";
    private static final TextFileGameDataPersistence TEXT_FILE_GAME_DATA_PERSISTENCE =
            new TextFileGameDataPersistence(SAVE_GAME_STATE_DIR);
    private static final TextFilePodiumPersistence TEXT_FILE_PODIUM_PERSISTENCE =
            new TextFilePodiumPersistence(SAVE_PODIUM_DIR);
    private static final Podium TOP_THREE_PODIUM =
            new TopThreePodium(TEXT_FILE_PODIUM_PERSISTENCE);

    public static void main(String[] args) throws IOException, InterruptedException {
        startGame();
    }

    private static char readRawKey() throws IOException, InterruptedException {
        Runtime.getRuntime().exec(STTY_RAW).waitFor();
        char key = 0;
        int inputChar;
        while ((inputChar = System.in.read()) != END_OF_STREAM) {
            if (inputChar == KEY_ENTER || inputChar == KEY_CR || inputChar == KEY_SPACE) {
                continue;
            }
            key = Character.toUpperCase((char) inputChar);
            if (VALID_KEYS.indexOf(key) >= 0) {
                break;
            }
        }

        Runtime.getRuntime().exec(STTY_NORMAL).waitFor();
        System.out.println();
        return key;
    }

    private static void startGame() throws IOException, InterruptedException {
        System.out.println("Bienvenido a 2048!");
        boolean gameFinished = false;

        final Board board = new GraphBoard(BOARD_SIZE,
                            new Optional[BOARD_SIZE * BOARD_SIZE],
                            TEXT_FILE_GAME_DATA_PERSISTENCE);
        final GameController game = new WASDGameController();
        final Scanner scanner = new Scanner(System.in);

        if (board.getCurrentState().hasSavedGame()) {
            board.initializeBoard();
            if (!board.getCurrentState().loadGameStateToFile()) {
                board.initializeBoard();
            }
        } else {
            board.initializeBoard();
        }

        System.out.println(TOP_THREE_PODIUM);
        System.out.println(board.getCurrentState().getBoardText());

        while (!gameFinished) {
            System.out.println("Presiona una tecla (se captura sin Enter): ");
            final char move = readRawKey();

            if (!VALID_KEYS.contains(Character.toString(move))) {
                System.out.println("Movimiento inválido, intente de nuevo.");
                System.out.println(board.getCurrentState().getBoardText());
                continue;
            }

            final MoveStatus status = game.doMove(move, board);

            if (status.equals(MoveStatus.DEFEAT)
                    || status.equals(MoveStatus.WIN)
                    || status.equals(MoveStatus.QUITED)) {
                if (status.equals(MoveStatus.QUITED)) {
                    board.getCurrentState().saveGameStateToFile();
                } else {
                    System.out.println(board.getCurrentState().getBoardText());
                    System.out.println(status.equals(MoveStatus.WIN) ? MSG_WIN : MSG_DEFEAT);
                    board.getCurrentState().eraseGameState();
                    final int currentScore = board.getCurrentState().getCurrentScore();

                    if (TOP_THREE_PODIUM.qualifiesForPodium(currentScore)) {
                        boolean scoreRegistered = false;
                        while (!scoreRegistered) {
                            System.out.print("Ingrese 3 iniciales: ");
                            final String playerName = scanner.nextLine().trim();
                            scoreRegistered = TOP_THREE_PODIUM.registerScore(
                                    playerName,
                                    currentScore);
                            if (!scoreRegistered) {
                                System.out.println("Nombre inválido. "
                                + "El nombre deben ser tres letras mayúsculas (A-Z) "
                                + "y no estar en el podio.");
                            }
                        }
                    }

                }
                gameFinished = true;
            } else {
                if (status.equals(MoveStatus.INVALID_MOVE)) {
                    System.out.println("Movimiento inválido, intente de nuevo.");
                }
                System.out.println(board.getCurrentState().getBoardText());
            }
        }
        System.out.println("Juego finalizado");
        scanner.close();
    }

    public String version() {
        return SEMANTIC_VERSION;
    }
}
