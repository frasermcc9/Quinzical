package quinzical.impl.models;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quinzical.impl.constants.GameEvent;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.events.EventEmitter;
import quinzical.interfaces.events.EventFunction;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.models.SceneRegistry;

/**
 * Singleton class that handles switching between scenes on the main stage.
 */
@Singleton
public class SceneHandlerImpl implements SceneHandler {
    private final Stage stage;

    @Inject
    EventEmitter emitter;

    @Inject
    SceneRegistry sceneRegistry;

    @Inject
    public SceneHandlerImpl(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the main stage theme to the selected theme.
     */
    @Override
    public void setActiveScene(GameScene scene) {
        Scene s = sceneRegistry.getScene(scene);
        stage.setScene(s);
    }

    /**
     * Delegate method to the event emitter. Registers a function to execute to an
     * event.
     */
    @Override
    public EventEmitter on(GameEvent event, EventFunction cb) {
        return emitter.on(event, cb);
    }

    /**
     * Delegate method to the event emitter. Emits an event for this object.
     */
    @Override
    public boolean emit(GameEvent event) {
        return emitter.emit(event);
    }
}
