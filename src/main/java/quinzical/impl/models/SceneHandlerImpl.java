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
import javafx.scene.Scene;
import quinzical.impl.constants.GameScene;
import quinzical.impl.constants.Theme;
import quinzical.impl.models.structures.FxmlInfo;
import quinzical.interfaces.models.SceneHandler;

import java.io.IOException;

/**
 * Singleton class that handles switching between scenes on the main stage.
 */
@Singleton
public class SceneHandlerImpl implements SceneHandler {

    private final Scene scene;
    @Inject
    Injector injector;
    private Theme activeTheme = null;

    @Inject
    public SceneHandlerImpl(Scene scene) {
        this.scene = scene;
    }

    public <T> T setActiveScene(GameScene newScene) {
        try {
            final FxmlInfo<T> fxmlInfo = FxmlInfo.loadFXML(newScene.getFxmlName(), injector);
            scene.setRoot(fxmlInfo.getParent());
            return fxmlInfo.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void fireBackgroundChange(Theme theme) {
        activeTheme = theme;
    }

    public Theme getActiveTheme() {
        return activeTheme;
    }

}
