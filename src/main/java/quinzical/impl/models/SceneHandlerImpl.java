package quinzical.impl.models;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.models.SceneRegistry;

@Singleton
public class SceneHandlerImpl implements SceneHandler {
    private final Stage stage;

    @Inject
    SceneRegistry sceneRegistry;

    @Inject
    public SceneHandlerImpl(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setActiveScene(GameScene scene) {
        Scene s = sceneRegistry.getScene(scene);
        stage.setScene(s);
    }
}
