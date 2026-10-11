package edu.ic6821.game.persistence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class TextFilePodiumPersistence implements PodiumPersistence {
    private final String saveFilePath;
    private static final int NAME_INDEX = 0;
    private static final int SCORE_INDEX = 1;

    public TextFilePodiumPersistence(final String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    @Override
    public void savePodium(Map<String, Integer> podium) {
        try (FileWriter writer = new FileWriter(saveFilePath)) {
            for (Map.Entry<String, Integer> entry : podium.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error al guardar el podium: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Integer> loadPodium() {
        final Map<String, Integer> podium = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(saveFilePath))) {
            String line;
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                final String[] playerData = line.trim().split("\\s+");
                if (playerData.length >= 2) {
                    try {
                        final String playerName = playerData[NAME_INDEX];
                        final int score = Integer.parseInt(playerData[SCORE_INDEX]);
                        podium.put(playerName, score);
                    } catch (NumberFormatException e) {
                        System.out.println("Error al cargar el podium: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar el podium: " + e.getMessage());
        }

        return podium;
    }
}
