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

package quinzical.impl.multiplayer;

import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.multiplayer.ActiveGame;

import java.io.IOException;

public class HostWaitController extends AbstractWaitController {

    @Inject
    ActiveGame activeGame;

    @Inject
    SceneHandler sceneHandler;

    @FXML
    void btnCancel(final ActionEvent event) {
        socketModel.getSocket().emit("quit");
        sceneHandler.setActiveScene(GameScene.MULTI_MENU);
    }

    @FXML
    void btnStart(final ActionEvent event) throws IOException {
        socketModel.getSocket().emit("startGame");
    }

    @Override
    protected void addListeners() {
        socketModel.getSocket().on("gameStart", (objects) -> Platform.runLater(() -> {
            final int time = Integer.parseInt(objects[0].toString());
            activeGame.reset().init(time);
        }));
    }

}
