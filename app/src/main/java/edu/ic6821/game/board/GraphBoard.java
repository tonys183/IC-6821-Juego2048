package edu.ic6821.game.board;

import edu.ic6821.game.datastructure.Pair;
import edu.ic6821.game.persistence.GameDataPersistence;
import org.jspecify.annotations.NonNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public final class GraphBoard implements Board, BoardState {
    private final Integer boardSize;
    private final AtomicInteger currentScore;
    private final Optional<Tile>[] board;
    private final GameDataPersistence gameDataPersistence;

    private static final Integer MOSAIC_FIRST_VALUE = 2;
    private static final Integer MOSAIC_SECOND_VALUE = 4;
    private static final float FIRST_VALUE_THRESHOLD = 0.6f;
    private static final Integer VICTORY_VALUE = 2048;
    private static final int UNIT_SIZE = 1;
    private static final int TEN_SIZE = 2;
    private static final int HUNDRED_SIZE = 3;

    public GraphBoard(final Integer boardSize,
                      final Optional<Tile>[] board,
                      @NonNull final GameDataPersistence gameDataPersistence) {
        this.boardSize = boardSize;
        this.board = board;
        this.gameDataPersistence = gameDataPersistence;
        this.currentScore = new AtomicInteger(0);
    }

    @Override
    public void initializeBoard() {
        for (int i = 0; i < boardSize * boardSize; i++) {
            this.board[i] = Optional.of(new Tile(i));
        }

        for (int i = 0; i < boardSize * boardSize; i++) {
            final Tile currentTile = board[i].get();
            if (currentTile.getNodeId() > boardSize - 1 && board[i - boardSize].isPresent()) {
                currentTile.setUp(board[i - boardSize].get());
            }

            if (currentTile.getNodeId() < boardSize * (boardSize - 1) && board[i + boardSize].isPresent()) {
                currentTile.setDown(board[i + boardSize].get());
            }

            if (currentTile.getNodeId() % boardSize != 0 && board[i - 1].isPresent()) {
                currentTile.setLeft(board[i - 1].get());
            }

            if (currentTile.getNodeId() % boardSize != boardSize - 1 && board[i + 1].isPresent()) {
                currentTile.setRight(board[i + 1].get());
            }
        }

        final Pair<Integer, Integer> firstTile = generateRandomTile();
        board[firstTile.getFirst()].get().setValue(firstTile.getSecond());

        final Pair<Integer, Integer> secondTile = generateRandomTile();
        board[secondTile.getFirst()].get().setValue(secondTile.getSecond());
    }

    @Override
    public MoveStatus checkMovement(@NonNull Tile[] previousBoard, @NonNull Integer previousScore) {

        if (!hasBoardChange(previousBoard)) {
            this.currentScore.set(previousScore);
            return checkDefeat() ? MoveStatus.DEFEAT : MoveStatus.INVALID_MOVE;
        }

        final Pair<Integer, Integer> newTile = generateRandomTile();
        this.board[newTile.getFirst()].get().setValue(newTile.getSecond());

        if (checkVictory()) {
            return MoveStatus.WIN;
        }

        if (checkDefeat()) {
            return MoveStatus.DEFEAT;
        }

        return MoveStatus.COLLISION;
    }

    @Override
    public void moveUp() {
        for (int i = 0; i < boardSize; i++) {
            Optional<Tile> currentTile = board[i];
            while (currentTile.isPresent()) {
                Optional<Tile> nextTile = currentTile.get().getDown();

                while (nextTile.isPresent()) {
                    if (currentTile.get().getValue() == 0) {
                        currentTile.get().setValue(nextTile.get().getValue());
                        nextTile.get().setValue(0);
                        nextTile = nextTile.get().getDown();
                    } else if (currentTile.get().getValue().equals(nextTile.get().getValue())) {
                        final Integer newValue = currentTile.get().getValue() * 2;
                        currentTile.get().setValue(newValue);
                        nextTile.get().setValue(0);
                        this.currentScore.addAndGet(newValue);
                        break;
                    } else if (nextTile.get().getValue() != 0) {
                        break;
                    } else {
                        nextTile = nextTile.get().getDown();
                    }
                }
                currentTile = currentTile.get().getDown();
            }
        }
    }

    @Override
    public void moveDown() {
        for (int i = boardSize * (boardSize - 1); i < boardSize * boardSize; i++) {
            Optional<Tile> currentTile = board[i];
            while (currentTile.isPresent()) {
                Optional<Tile> nextTile = currentTile.get().getUp();

                while (nextTile.isPresent()) {
                    if (currentTile.get().getValue() == 0) {
                        currentTile.get().setValue(nextTile.get().getValue());
                        nextTile.get().setValue(0);
                        nextTile = nextTile.get().getUp();
                    } else if (currentTile.get().getValue().equals(nextTile.get().getValue())) {
                        final Integer newValue = currentTile.get().getValue() * 2;
                        currentTile.get().setValue(newValue);
                        nextTile.get().setValue(0);
                        this.currentScore.addAndGet(newValue);
                        break;
                    } else if (nextTile.get().getValue() != 0) {
                        break;
                    } else {
                        nextTile = nextTile.get().getUp();
                    }
                }
                currentTile = currentTile.get().getUp();
            }
        }
    }

    @Override
    public void moveLeft() {
        for (int i = 0; i < boardSize * boardSize; i += boardSize) {
            Optional<Tile> currentTile = board[i];
            while (currentTile.isPresent()) {
                Optional<Tile> nextTile = currentTile.get().getRight();

                while (nextTile.isPresent()) {
                    if (currentTile.get().getValue() == 0) {
                        currentTile.get().setValue(nextTile.get().getValue());
                        nextTile.get().setValue(0);
                        nextTile = nextTile.get().getRight();
                    } else if (currentTile.get().getValue().equals(nextTile.get().getValue())) {
                        final Integer newValue = currentTile.get().getValue() * 2;
                        currentTile.get().setValue(newValue);
                        nextTile.get().setValue(0);
                        this.currentScore.addAndGet(newValue);
                        break;
                    } else if (nextTile.get().getValue() != 0) {
                        break;
                    } else {
                        nextTile = nextTile.get().getRight();
                    }
                }
                currentTile = currentTile.get().getRight();
            }
        }
    }

    @Override
    public void moveRight() {
        for (int i = boardSize - 1; i < boardSize * boardSize; i += boardSize) {
            Optional<Tile> currentTile = board[i];
            while (currentTile.isPresent()) {
                Optional<Tile> nextTile = currentTile.get().getLeft();

                while (nextTile.isPresent()) {
                    if (currentTile.get().getValue() == 0) {
                        currentTile.get().setValue(nextTile.get().getValue());
                        nextTile.get().setValue(0);
                        nextTile = nextTile.get().getLeft();
                    } else if (currentTile.get().getValue().equals(nextTile.get().getValue())) {
                        final Integer newValue = currentTile.get().getValue() * 2;
                        currentTile.get().setValue(newValue);
                        nextTile.get().setValue(0);
                        this.currentScore.addAndGet(newValue);
                        break;
                    } else if (nextTile.get().getValue() != 0) {
                        break;
                    } else {
                        nextTile = nextTile.get().getLeft();
                    }
                }
                currentTile = currentTile.get().getLeft();
            }
        }
    }


    @Override
    public BoardState getCurrentState() {
        return this;
    }

    @Override
    public Integer getCurrentScore() {
        return this.currentScore.get();
    }

    @Override
    public String getBoardText() {
        return this.toString();
    }

    @Override
    public Tile[] getCurrentTiles() {
        final Tile[] currentBoard = new Tile[boardSize * boardSize];

        for (int i = 0; i < boardSize * boardSize; i++) {
            if (this.board[i].isEmpty()) {
                continue;
            }

            final Tile current = this.board[i].get();
            final Tile currentCopy = new Tile(current.getNodeId());
            currentCopy.setValue(current.getValue());
            currentBoard[i] = currentCopy;
        }

        return currentBoard;
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        final int[][] grid = new int[boardSize][boardSize];

        for (Optional<Tile> tile : this.board) {

            if (tile.isEmpty() || tile.get().getValue() == 0) {
                continue;
            }

            final int row = tile.get().getNodeId() / boardSize;
            final int col = tile.get().getNodeId() % boardSize;
            grid[row][col] = tile.get().getValue();
        }

        for (int r = 0; r < boardSize; r++) {
            stringBuilder.append("+");
            stringBuilder.append("----+".repeat(Math.max(0, boardSize)));
            stringBuilder.append("\n");

            stringBuilder.append("|");
            for (int c = 0; c < boardSize; c++) {
                final int value = grid[r][c];
                if (value == 0) {
                    stringBuilder.append("    |");
                } else {
                    stringBuilder.append(formatValue(value)).append("|");
                }
            }
            stringBuilder.append("\n");
        }

        stringBuilder.append("+");
        stringBuilder.append("----+".repeat(Math.max(0, boardSize)));
        stringBuilder.append("\n");

        stringBuilder.append("Puntaje: ")
                .append(this.currentScore.get())
                .append("\n");

        return stringBuilder.toString();
    }

    public void saveGameStateToFile() {
        final Integer[] boardValues = new Integer[boardSize * boardSize];

        for (int i = 0; i < boardValues.length; i++) {
            boardValues[i] = this.board[i].get().getValue();
        }

        this.gameDataPersistence.saveGameState(boardValues, this.currentScore.get());
    }

    public boolean loadGameStateToFile() {
        final Optional<Integer[]> loadedBoardValues = this.gameDataPersistence.loadBoardValues();
        final Integer loadedScore = this.gameDataPersistence.loadCurrentScore();

        if (loadedBoardValues.isPresent()) {
            this.currentScore.set(loadedScore);

            for (int i = 0; i < loadedBoardValues.get().length; i++) {
                this.board[i].get().setValue(loadedBoardValues.get()[i]);
            }
            return true;
        }
        return false;
    }

    public void eraseGameState() {
        this.gameDataPersistence.eraseGameState();
    }

    public boolean hasSavedGame() {
        return this.gameDataPersistence.hasSavedGame();
    }

    private String formatValue(int value) {
        final String stringValue = String.valueOf(value);
        return switch (stringValue.length()) {
            case UNIT_SIZE -> "  " + stringValue + " ";
            case TEN_SIZE -> " " + stringValue + " ";
            case HUNDRED_SIZE -> " " + stringValue;
            default -> stringValue;
        };
    }

    private Pair<Integer, Integer> generateRandomTile() {
        final List<Integer> emptyPositions = new ArrayList<>();

        for (Optional<Tile> tile : board) {
            if (tile.get().getValue() == 0) {
                emptyPositions.add(tile.get().getNodeId());
            }
        }
        if (emptyPositions.isEmpty()) {
            return new Pair<>(board[0].get().getNodeId(), board[0].get().getValue());
        }

        Collections.shuffle(emptyPositions);
        final Integer tileID = emptyPositions.getFirst();
        final Random rand = new Random();
        return new Pair<>(tileID,
                rand.nextFloat() < FIRST_VALUE_THRESHOLD ? MOSAIC_FIRST_VALUE : MOSAIC_SECOND_VALUE);
    }

    private boolean hasBoardChange(Tile[] previousBoard) {

        for (int i = 0; i < boardSize * boardSize; i++) {
            if (!Objects.equals(previousBoard[i].getValue(), this.board[i].get().getValue())) {
                return true;
            }
        }

        return false;
    }

    private boolean isFull() {
        for (int i = 0; i < boardSize * boardSize; i++) {
            if (this.board[i].get().getValue() == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean checkVictory() {
        for (Optional<Tile> tile : board) {
            if (Objects.equals(tile.get().getValue(), VICTORY_VALUE)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDefeat() {
        if (!isFull()) {
            return false;
        }

        for (Optional<Tile> optionalTile : board) {
            if (optionalTile.isEmpty()) {
                continue;
            }
            final Tile tile = optionalTile.get();
            if ((tile.getUp().isPresent() && tile.getValue().equals(tile.getUp().get().getValue()))
                    || (tile.getDown().isPresent() && tile.getValue().equals(tile.getDown().get().getValue()))
                    || (tile.getLeft().isPresent() && tile.getValue().equals(tile.getLeft().get().getValue()))
                    || (tile.getRight().isPresent() && tile.getValue().equals(tile.getRight().get().getValue()))) {
                return false;
            }
        }
        return true;
    }

}


