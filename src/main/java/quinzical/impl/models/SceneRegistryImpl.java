package quinzical.impl.models;

import com.google.inject.Singleton;
import javafx.scene.Scene;

import java.util.HashMap;
import java.util.Map;

enum SceneName {
    INTRO, PRACTICE, GAME, GAME_QUESTION, PRACTICE_QUESTION, END
}

@Singleton
public class SceneRegistryImpl implements quinzical.interfaces.models.SceneRegistry {
    private Map<SceneName, Scene> map = new HashMap<>();

    @Override
    public void addScene(SceneName name, Scene scene) {
        this.map.put(name, scene);
    }

    @Override
    public Scene getScene(SceneName name) {
        return this.map.get(name);
    }
}
