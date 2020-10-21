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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import quinzical.impl.constants.GameScene;
import quinzical.impl.controllers.AbstractSceneController;
import quinzical.impl.multiplayer.models.Player;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.multiplayer.ActiveGame;


public class GameEndController extends AbstractSceneController {

    @FXML
    private TableView<Player> tablePlayers;
    @FXML
    private TableColumn<Player, String> columnName;
    @FXML
    private TableColumn<Player, String> columnPoints;
    @FXML
    private Label lblPoints;

    @Inject
    private SceneHandler sceneHandler;
    @Inject
    private ActiveGame activeGame;

    @Override
    protected void onLoad() {
        columnName.setCellValueFactory(v -> v.getValue().nameProperty());
        columnPoints.setCellValueFactory(v -> v.getValue().scoreProperty());
        tablePlayers.setItems(activeGame.getPlayers());

        int points = activeGame.getPoints();
        lblPoints.setText("You Finished With " + points + " Points!");
    }

    @FXML
    void onHomeClick(ActionEvent event) {
        sceneHandler.setActiveScene(GameScene.MULTI_MENU);
    }

}

