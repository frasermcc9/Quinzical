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
import io.socket.client.Socket;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import quinzical.impl.constants.GameScene;
import quinzical.impl.controllers.StandardSceneController;
import quinzical.impl.multiplayer.models.MultiplayerGame;
import quinzical.impl.multiplayer.models.SocketModel;
import quinzical.interfaces.models.SceneHandler;

public class MenuController extends StandardSceneController {

    private final Socket socket = SocketModel.getInstance().getSocket();
    private final String name = SocketModel.getInstance().getName();
    
    @Inject
    SceneHandler sceneHandler;
    
    @FXML
    private TextField txtCode;
    @FXML
    private Label lblName;

    @Override
    protected void onLoad() {
        lblName.setText(name);
    }

    @FXML
    void btnBrowse(ActionEvent event) {
        sceneHandler.setActiveScene(GameScene.MULTI_BROWSE);
    }

    @FXML
    void btnHost(ActionEvent event) {
        sceneHandler.setActiveScene(GameScene.MULTI_HOST);
    }

    @FXML
    void btnJoin() {        
        String code = txtCode.getText().toUpperCase();
        socket.emit("joinGameRequest", name, code);
        socket.once("joinGameNotification", (arg) -> {
            if (arg[0].equals(false)) {
                txtCode.setPromptText(arg[1] + "");
                txtCode.setText("");
            } else {
                JSONArray jsonArray = (JSONArray) arg[1];
                MultiplayerGame mg = MultiplayerGame.getInstance();
                mg.setCode(code);
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        mg.addPlayer((String) jsonArray.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                sceneHandler.setActiveScene(GameScene.MULTI_PLAYER_WAIT);
            }
        });
    }


    @FXML
    void btnQuit(ActionEvent event) {
        Platform.runLater(() -> {
            SocketModel.getInstance().destroy();
        });
        sceneHandler.setActiveScene(GameScene.MULTI_INTRO);
    }

    void joinGame(String code){
        
    }
    
}
