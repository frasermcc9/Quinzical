package quinzical.interfaces.models;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface GameModelSaver {
    void saveQuestionsToDisk() throws IOException;
}
