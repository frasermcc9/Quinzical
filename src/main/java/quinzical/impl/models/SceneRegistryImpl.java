package quinzical.impl.models;

import com.google.inject.Singleton;
import javafx.scene.Scene;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.SceneRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton registry for all scenes.
 */
@Singleton
public class SceneRegistryImpl implements SceneRegistry {
    private final Map<GameScene, Scene> map = new HashMap<>();

    /**
     * Adds a scene to the registry, bound to a name.
     */
    @Override
    public void addScene(GameScene name, Scene scene) {
        this.map.put(name, scene);
    }

    /**
     * Gets a scene by its name.
     */
    @Override
    public Scene getScene(GameScene name) {
        return this.map.get(name);
    }
}
