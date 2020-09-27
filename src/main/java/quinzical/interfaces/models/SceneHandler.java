package quinzical.interfaces.models;

import javafx.scene.image.Image;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.events.BackgroundObserver;


public interface SceneHandler {
    void onBackgroundChange(BackgroundObserver fn);

    void fireBackgroundChange(Image img);

    void setActiveScene(GameScene scene);
    
}
