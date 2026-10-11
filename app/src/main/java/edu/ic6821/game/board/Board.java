package edu.ic6821.game.board;

import org.jspecify.annotations.NonNull;

public interface Board {
    MoveStatus checkMovement(@NonNull Tile[] previousBoard, @NonNull Integer previousScore);
    BoardState getCurrentState();
    void initializeBoard();
    void moveUp();
    void moveDown();
    void moveLeft();
    void moveRight();
}
