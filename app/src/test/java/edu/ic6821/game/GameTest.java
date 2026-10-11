package edu.ic6821.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameTest {

    @Test
    public void retrieveVersion() {
        // given
        final Game game = new Game();

        // when
        final String version = game.version();

        // then
        Assertions.assertEquals("0.0.1", version);
    }
}
