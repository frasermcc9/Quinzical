package quinzical.interfaces.models;

import quinzical.impl.constants.GameScene;

@FunctionalInterface
public interface SceneChangeObserver {
    void sceneChanged(GameScene gameScene);
}
