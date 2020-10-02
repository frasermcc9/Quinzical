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
