package edu.ic6821.game.persistence;

import java.util.Optional;

public interface GameDataPersistence {
    void saveGameState(Integer[] boardValues, Integer currentScore);
    Optional<Integer[]> loadBoardValues();
    Integer loadCurrentScore();
    void eraseGameState();
    boolean hasSavedGame();
}
