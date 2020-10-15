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

import io.socket.client.Socket;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import quinzical.impl.controllers.StandardSceneController;
import quinzical.impl.multiplayer.models.MultiplayerGame;
import quinzical.impl.multiplayer.models.SocketModel;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWaitController extends StandardSceneController {
    final protected Socket socket = SocketModel.getInstance().getSocket();
    final protected String name = SocketModel.getInstance().getName();

    @FXML
    Label lblCode;

    @FXML
    ListView<String> listPlayers;

    @FXML
    Label lblPlayers;

    void loadWaitScreen() {

        listPlayers.setItems(MultiplayerGame.getInstance().getObservablePlayers());
        lblCode.setText(MultiplayerGame.getInstance().getCode());


        socket.on("playersChange", objects -> Platform.runLater(() -> {

            List<String> javaList = new ArrayList<>();

            JSONArray jsonArray = (JSONArray) objects[0];
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    javaList.add((String) jsonArray.get(i));
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
            ObservableList<String> mg = MultiplayerGame.getInstance().getObservablePlayers();
            mg.clear();
            mg.addAll(javaList);

            Platform.runLater(() -> lblPlayers.setText(listPlayers.getItems().size() + "/" + objects[1]));
        }));
    }
    
    /**
     * optional hook
     */
    protected void addListeners(){

    }

    @Override
    protected void onLoad() {
        loadWaitScreen();
        addListeners();
        socket.emit("clientReady");
    }
}
