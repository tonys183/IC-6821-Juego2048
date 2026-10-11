package edu.ic6821.game.persistence;

import java.util.Map;

public interface PodiumPersistence {
    void savePodium(Map<String, Integer> podium);
    Map<String, Integer> loadPodium();
}
