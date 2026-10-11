package edu.ic6821.game.podium;

public interface Podium {
    boolean registerScore(String playerName, int score);
    boolean qualifiesForPodium(int score);
}
