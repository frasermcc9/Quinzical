package quinzical.impl.models;

import com.google.inject.Singleton;
import javafx.scene.Scene;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.SceneRegistry;

import java.util.HashMap;
import java.util.Map;


@Singleton
public class SceneRegistryImpl implements SceneRegistry {
    private final Map<GameScene, Scene> map = new HashMap<>();

    @Override
    public void addScene(GameScene name, Scene scene) {
        this.map.put(name, scene);
    }

    @Override
    public Scene getScene(GameScene name) {
        return this.map.get(name);
    }
}
