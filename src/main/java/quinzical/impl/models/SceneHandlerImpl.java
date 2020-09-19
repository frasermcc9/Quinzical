package quinzical.impl.models;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quinzical.interfaces.models.SceneHandler;

@Singleton
public class SceneHandlerImpl implements SceneHandler {
    private final Stage stage;

    @Inject
    public SceneHandlerImpl(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setActiveScene(Scene scene) {
        stage.setScene(scene);
    }
}
