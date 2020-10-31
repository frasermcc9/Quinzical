// Copyright 2020 Fraser McCallum and Braden Palmer
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//  
//     http://www.apache.org/licenses/LICENSE-2.0
//  
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package quinzical.impl.models;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import quinzical.impl.constants.GameScene;
import quinzical.impl.constants.Theme;
import quinzical.impl.controllers.AbstractSceneController;
import quinzical.impl.models.structures.FxmlInfo;
import quinzical.impl.util.strategies.timer.TimerType;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.strategies.timer.TimerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class that handles switching between scenes on the main stage.
 */
@Singleton
public class SceneHandlerImpl implements SceneHandler {

    private final Scene scene;
    private final Map<GameScene, FxmlInfo<AbstractSceneController>> cachedScenes;
    @Inject
    Injector injector;
    @Inject
    TimerContext timerContext;
    private Theme activeTheme = null;

    @Inject
    public SceneHandlerImpl(final Scene scene) {
        this.scene = scene;
        this.cachedScenes = new HashMap<>();
    }

    public AbstractSceneController setActiveScene(final GameScene newScene) {

        FxmlInfo<AbstractSceneController> fxmlInfo = null;
        if (cachedScenes.containsKey(newScene)) {
            fxmlInfo = cachedScenes.get(newScene);
        } else {
            try {
                fxmlInfo = FxmlInfo.loadFXML(newScene.getFxmlName(), injector);
            } catch (final Exception e) {
                e.printStackTrace();
            }

        }
        if (fxmlInfo == null) return null;

        final Parent p = fxmlInfo.getParent();

        AnchorPane.setTopAnchor(p, 0.0);
        AnchorPane.setBottomAnchor(p, 0.0);
        AnchorPane.setLeftAnchor(p, 0.0);
        AnchorPane.setRightAnchor(p, 0.0);

        Platform.runLater(() -> ((AnchorPane) scene.getRoot()).getChildren().add(p));

        timerContext.createTimer(TimerType.DEFAULT).setTimeout(() -> Platform.runLater(() -> {
            if (((AnchorPane) scene.getRoot()).getChildren().size() == 2)
                ((AnchorPane) scene.getRoot()).getChildren().remove(0);
        }), 300);

        final var ft = new FadeTransition(Duration.millis(300));
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setNode(p);
        ft.playFromStart();
        
        return fxmlInfo.getController();
    }

    @Override
    public void fireBackgroundChange(final Theme theme) {
        activeTheme = theme;
    }

    public Theme getActiveTheme() {
        return activeTheme;
    }

    public void cacheScenes() {

        //cachedScenes.put(GameScene.MULTI_INTRO, FxmlInfo.loadFXML(GameScene.MULTI_INTRO.getFxmlName(), injector));

    }

}
