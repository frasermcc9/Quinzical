package quinzical.impl.models;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.events.ChangeableBackground;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.models.SceneRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class that handles switching between scenes on the main stage.
 */
@Singleton
public class SceneHandlerImpl implements SceneHandler {
    
    private final Stage stage;
    
    private final List<ChangeableBackground> changeableBackgrounds = new ArrayList<>();
    
    @Inject
    SceneRegistry sceneRegistry;

    @Inject
    public SceneHandlerImpl(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void onBackgroundChange(ChangeableBackground fn) {
        changeableBackgrounds.add(fn);
    }

    @Override
    public void fireBackgroundChange(Image img) {
        changeableBackgrounds.forEach(el -> el.updateBackground(img));
    }

    /**
     * Sets the main stage theme to the selected theme.
     */
    @Override
    public void setActiveScene(GameScene scene) {
        Scene s = sceneRegistry.getScene(scene);
        stage.setScene(s);
    }

}
