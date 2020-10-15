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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import org.json.JSONException;
import quinzical.impl.controllers.StandardSceneController;
import quinzical.impl.multiplayer.models.GameSettings;
import quinzical.impl.multiplayer.models.MultiplayerGame;
import quinzical.impl.multiplayer.models.SocketModel;
import quinzical.impl.multiplayer.util.Util;

import java.io.IOException;

public class HostController  extends StandardSceneController {

    private final Socket socket = SocketModel.getInstance().getSocket();
    private final String name = SocketModel.getInstance().getName();

    @FXML
    private TextField txtQuestions;

    @FXML
    private TextField txtPlayers;

    @FXML
    private TextField txtTime;

    @FXML
    private CheckBox chkPublic;

    @FXML
    void btnCancel(ActionEvent event) throws IOException {
        App.setRoot("menu");
    }

    @FXML
    void btnStart(ActionEvent event) throws IllegalAccessException, NoSuchFieldException, JSONException {

        int questions = parseToInt(txtQuestions);
        if (questions == 0) questions = 10;
        int time = parseToInt(txtTime);
        if (time == 0) time = 10;
        int maxPlayers = parseToInt(txtPlayers);
        if (maxPlayers == 0) maxPlayers = 2;

        boolean isPublic = chkPublic.selectedProperty().get();

        socket.once("gameHostGiven", (code) -> {
            MultiplayerGame.getInstance().setCode((String) code[0]);
            MultiplayerGame.getInstance().addPlayer(name);
            App.setRoot("host-wait");
        });
        socket.emit("hostGameRequest", name, Util.asJson(new GameSettings(questions, time, maxPlayers, isPublic)));
    }

    private int parseToInt(TextField textField) {
        String text = textField.getText();
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }

    @Override
    protected void onLoad() {
        
    }
}
