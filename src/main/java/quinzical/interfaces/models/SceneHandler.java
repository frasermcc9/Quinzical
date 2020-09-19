package quinzical.interfaces.models;

import quinzical.impl.constants.GameEvent;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.events.EventEmitter;
import quinzical.interfaces.events.EventFunction;

public interface SceneHandler {
    abstract void setActiveScene(GameScene scene);

    EventEmitter on(GameEvent event, EventFunction cb);

    boolean emit(GameEvent event);
}
