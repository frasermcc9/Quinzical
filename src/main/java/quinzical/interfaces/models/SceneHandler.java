package quinzical.interfaces.models;

import javafx.scene.image.Image;

import quinzical.impl.constants.GameScene;
import quinzical.interfaces.events.ChangeableBackground;


public interface SceneHandler {
    void onBackgroundChange(ChangeableBackground fn);

    void fireBackgroundChange(Image img);

    void setActiveScene(GameScene scene);
    
}
