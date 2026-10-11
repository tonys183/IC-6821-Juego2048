package edu.ic6821.game.board;

public interface BoardState {
    String getBoardText();
    Integer getCurrentScore();
    Tile[] getCurrentTiles();
    void saveGameStateToFile();
    boolean loadGameStateToFile();
    boolean hasSavedGame();
    void eraseGameState();
}
