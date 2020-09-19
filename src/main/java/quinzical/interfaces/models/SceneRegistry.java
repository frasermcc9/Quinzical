package quinzical.interfaces.models;

import javafx.scene.Scene;
import quinzical.impl.constants.GameScene;


public interface SceneRegistry {
    void addScene(GameScene name, Scene scene);

    Scene getScene(GameScene name);
}
