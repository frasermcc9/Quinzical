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
import com.google.inject.Singleton;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.events.BackgroundObserver;
import quinzical.interfaces.models.SceneChangeObserver;
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

    private final List<BackgroundObserver> backgroundObservers = new ArrayList<>();

    private final List<SceneChangeObserver> sceneChangeObservers = new ArrayList<>();

    @Inject
    SceneRegistry sceneRegistry;

    @Inject
    public SceneHandlerImpl(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void onBackgroundChange(BackgroundObserver fn) {
        backgroundObservers.add(fn);
    }

    @Override
    public void fireBackgroundChange(Image img) {
        backgroundObservers.forEach(el -> el.updateBackground(img));
    }

    /**
     * Sets the main stage theme to the selected theme.
     */
    @Override
    public void setActiveScene(GameScene scene) {
        Scene s = sceneRegistry.getScene(scene);
        stage.setScene(s);
        sceneChangeObservers.forEach(o -> o.sceneChanged(scene));
    }

    @Override
    public void onSceneChange(SceneChangeObserver sceneChangeObserver) {
        sceneChangeObservers.add(sceneChangeObserver);
    }
}
