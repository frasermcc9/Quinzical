package quinzical.interfaces.models;

import java.io.IOException;

/**
 * Partial interface of GameModel for saving the game state.
 */
public interface GameModelSaver {
    void saveQuestionsToDisk() throws IOException;
}
