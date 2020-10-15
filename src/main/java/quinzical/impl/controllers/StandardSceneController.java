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

package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import quinzical.interfaces.models.SceneHandler;

public abstract class StandardSceneController {

    @FXML
    protected ImageView background;

    @Inject
    protected SceneHandler sceneHandler;

    @FXML
    public final void initialize() {
        this.background.setImage(sceneHandler.getActiveTheme().getImage());
        
        onLoad();
        refresh();
    }
    
    protected abstract void onLoad();
    
    protected void refresh() {
    }
    
}
