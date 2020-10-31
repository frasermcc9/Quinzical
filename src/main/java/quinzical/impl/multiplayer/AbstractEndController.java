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
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import quinzical.impl.controllers.AbstractSceneController;
import quinzical.impl.multiplayer.models.Player;
import quinzical.interfaces.multiplayer.ActiveGame;

public abstract class AbstractEndController extends AbstractSceneController {

    @Inject
    protected ActiveGame activeGame;

    @FXML
    private JFXTreeTableView<Player> tablePlayers;

    protected final void onLoad() {
        final JFXTreeTableColumn<Player, String> playerCol = new JFXTreeTableColumn<>("Player Name");
        final JFXTreeTableColumn<Player, String> pointsCol = new JFXTreeTableColumn<>("Points");

        final ObservableList<Player> players = activeGame.getPlayers();

        Platform.runLater(() -> {
            playerCol.setCellValueFactory(v -> v.getValue().getValue().nameProperty());
            pointsCol.setCellValueFactory(v -> v.getValue().getValue().scoreProperty());
            tablePlayers.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

            final TreeItem<Player> root = new RecursiveTreeItem<>(players, RecursiveTreeObject::getChildren);
            tablePlayers.setRoot(root);
            tablePlayers.setShowRoot(false);
            tablePlayers.getColumns().setAll(playerCol, pointsCol);

        });

        initializeComponents();
        
    }

    protected void initializeComponents() {
        
    }

}
