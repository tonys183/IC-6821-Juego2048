package edu.ic6821.game.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.util.Optional;

public final class TextFileGameDataPersistence implements GameDataPersistence {
    private Optional<Integer[]> boardValues;
    private Integer currentScore;

    private final String saveFilePath;
    private static final String SCORE_PREFIX = "SCORE:";
    private static final String BOARD_PREFIX = "BOARD_VALUES:";

    public TextFileGameDataPersistence(final String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    @Override
    public void eraseGameState() {
        try (PrintWriter pw = new PrintWriter(saveFilePath)) {
            pw.print("");
        } catch (IOException e) {
            System.out.println("Error al limpiar el archivo: " + e.getMessage());
        }
    }

    @Override
    public void saveGameState(Integer[] currentBoardValues, Integer obtainedScore) {
        try (FileWriter writer = new FileWriter(saveFilePath)) {
            writer.write("SCORE:" + obtainedScore + "\n");
            writer.write("BOARD_VALUES:");

            for (int i = 0; i < currentBoardValues.length; i++) {
                writer.write(currentBoardValues[i].toString());
                if (i < currentBoardValues.length - 1) {
                    writer.write(",");
                }
            }
            writer.write("\n");
        } catch (IOException e) {
            System.out.println("Error al guardar el estado del juego: " + e.getMessage());
        }
    }

    @Override
    public Optional<Integer[]> loadBoardValues() {
        loadGameData();
        return boardValues;
    }

    @Override
    public Integer loadCurrentScore() {
        loadGameData();
        return currentScore;
    }

    @Override
    public boolean hasSavedGame() {
        return new File(saveFilePath).exists();
    }

    private void loadGameData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(saveFilePath))) {
            String line;
            currentScore = 0;
            boardValues = Optional.empty();

            while ((line = reader.readLine()) != null) {
                final String trimmedLine = line.trim();

                if (trimmedLine.startsWith(SCORE_PREFIX)) {
                    try {
                        currentScore = Integer.parseInt(trimmedLine.substring(SCORE_PREFIX.length()).trim());
                    } catch (NumberFormatException e) {
                        currentScore = 0;
                    }
                } else if (trimmedLine.startsWith(BOARD_PREFIX)) {
                    final String boardValuesLine = trimmedLine.substring(BOARD_PREFIX.length());
                    final String[] tokens = boardValuesLine.split(",");
                    boardValues = Optional.of(new Integer[tokens.length]);

                    for (int i = 0; i < tokens.length; i++) {
                        try {
                            boardValues.get()[i] = Integer.parseInt(tokens[i].trim());
                        } catch (NumberFormatException e) {
                            boardValues.get()[i] = 0;
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error al cargar el estado del juego: " + e.getMessage());
            currentScore = 0;
            boardValues = Optional.empty();
        }
    }
}
