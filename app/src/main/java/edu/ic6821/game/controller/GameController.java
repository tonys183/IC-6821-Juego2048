package edu.ic6821.game.controller;

import edu.ic6821.game.board.Board;
import edu.ic6821.game.board.MoveStatus;

public interface GameController {
    MoveStatus doMove(char move, Board board);
}
