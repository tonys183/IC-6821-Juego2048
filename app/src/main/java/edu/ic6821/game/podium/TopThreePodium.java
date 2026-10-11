package edu.ic6821.game.podium;

import edu.ic6821.game.persistence.PodiumPersistence;
import org.jspecify.annotations.NonNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class TopThreePodium implements Podium {
    private final Map<String, Integer> podium;
    private final PodiumPersistence persistence;
    private static final Integer MAX_PODIUM_SIZE = 3;
    private static final Integer EXPECTED_NAME_LENGTH = 3;

    public TopThreePodium(@NonNull final PodiumPersistence persistence) {
        this.persistence = persistence;
        this.podium = this.persistence.loadPodium();
    }

    @Override
    public boolean registerScore(String playerName, int score) {

        if (playerName == null
                || playerName.trim().isEmpty()
                || !playerName.toUpperCase().equals(playerName)
                || playerName.length() != EXPECTED_NAME_LENGTH
                || !playerName.matches("[A-Z]+")) {
            return false;
        }

        if (this.podium.containsKey(playerName)) {
            return false;
        }

        if (this.podium.size() < MAX_PODIUM_SIZE) {
            this.podium.put(playerName, score);
            this.persistence.savePodium(this.podium);
            return true;
        }

        String lowestScorePlayer = this.podium.entrySet().iterator().next().getKey();

        for (Map.Entry<String, Integer> topPlayer : this.podium.entrySet()) {
            lowestScorePlayer = topPlayer.getValue() < this.podium.get(lowestScorePlayer)
                    ? topPlayer.getKey() : lowestScorePlayer;
        }

        if (score > this.podium.get(lowestScorePlayer)) {
            this.podium.put(playerName, score);
            this.podium.remove(lowestScorePlayer);
        }

        this.persistence.savePodium(this.podium);
        return true;
    }

    @Override
    public boolean qualifiesForPodium(int score) {
        if (this.podium.size() < MAX_PODIUM_SIZE) {
            return true;
        }

        String lowestScorePlayer = this.podium.entrySet().iterator().next().getKey();

        for (Map.Entry<String, Integer> topPlayer : this.podium.entrySet()) {
            lowestScorePlayer = topPlayer.getValue() < this.podium.get(lowestScorePlayer)
                    ? topPlayer.getKey() : lowestScorePlayer;
        }

        return score > this.podium.get(lowestScorePlayer);
    }

    @Override
    public String toString() {
        final List<Map.Entry<String, Integer>> sortedPodium = new ArrayList<>(this.podium.entrySet());

        sortedPodium.sort(Comparator.comparingInt(Map.Entry<String, Integer>::getValue).reversed());

        final StringBuilder podiumString = new StringBuilder("Récord\n");
        for (Map.Entry<String, Integer> topPlayer : sortedPodium) {
            podiumString.append(topPlayer.getKey() + " " + topPlayer.getValue() + "\n");
        }

        return podiumString.toString();
    }
}
