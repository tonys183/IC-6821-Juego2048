package edu.ic6821.game.controller;

import edu.ic6821.game.board.Board;
import edu.ic6821.game.board.MoveStatus;
import edu.ic6821.game.board.Movement;
import edu.ic6821.game.board.Tile;

import java.util.Map;
import java.util.Optional;

public final class WASDGameController implements GameController {

    private final Map<Character, Movement> possibleMovementCharacters = Map.of(
        'W', Movement.UP,
        'S', Movement.DOWN,
        'A', Movement.LEFT,
        'D', Movement.RIGHT,
        'Q', Movement.QUIT
    );

    public WASDGameController() {

    }

    @Override
    public MoveStatus doMove(char move, Board board) {

        final Optional<Movement> parseMovement = parseMove(move);

        if (parseMovement.isEmpty()) {
            return MoveStatus.INVALID_MOVE;
        }

        final Movement movement = parseMovement.get();

        if (Movement.QUIT.equals(movement)) {
            return MoveStatus.QUITED;
        }

        final Tile[] previousBoard = board.getCurrentState().getCurrentTiles();
        final Integer previousScore = board.getCurrentState().getCurrentScore();

        switch (movement) {
            case UP -> board.moveUp();
            case DOWN -> board.moveDown();
            case LEFT -> board.moveLeft();
            case RIGHT -> board.moveRight();
            default -> {
                return MoveStatus.INVALID_MOVE;
            }
        }

        return board.checkMovement(previousBoard, previousScore);
    }

    private Optional<Movement> parseMove(char movement) {
        return Optional.ofNullable(possibleMovementCharacters.get(movement));
    }
}
