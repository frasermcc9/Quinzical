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
import com.jfoenix.controls.JFXCheckBox;
import io.socket.client.Socket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.json.JSONException;
import quinzical.impl.constants.GameScene;
import quinzical.impl.controllers.AbstractSceneController;
import quinzical.impl.multiplayer.models.GameSettings;
import quinzical.impl.multiplayer.models.MultiplayerGame;
import quinzical.impl.multiplayer.util.Util;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.multiplayer.SocketModel;

import java.io.IOException;

public class HostController extends AbstractSceneController {

    @Inject
    private SocketModel socketModel;
    @Inject
    private SceneHandler sceneHandler;

    @FXML
    private Button qDec;
    @FXML
    private Label txtQuestions;
    @FXML
    private Button qInc;
    @FXML
    private Button pDec;
    @FXML
    private Label txtPlayers;
    @FXML
    private Button pInc;
    @FXML
    private Button aDec;
    @FXML
    private Label txtTime;
    @FXML
    private Button aInc;
    @FXML
    private JFXCheckBox chkPublic;

    @FXML
    void btnCancel(final ActionEvent event) throws IOException {
        sceneHandler.setActiveScene(GameScene.MULTI_MENU);
    }

    @FXML
    void btnStart(final ActionEvent event) throws IllegalAccessException, NoSuchFieldException, JSONException {

        final Socket socket = socketModel.getSocket();
        final String name = socketModel.getName();

        int questions = parseToInt(txtQuestions);
        if (questions == 0) questions = 10;
        int time = parseToInt(txtTime);
        if (time == 0) time = 10;
        int maxPlayers = parseToInt(txtPlayers);
        if (maxPlayers == 0) maxPlayers = 2;

        final boolean isPublic = chkPublic.selectedProperty().get();

        socket.once("gameHostGiven", (code) -> {
            MultiplayerGame.getInstance().setCode((String) code[0]);
            MultiplayerGame.getInstance().addPlayer(name);
            sceneHandler.setActiveScene(GameScene.MULTI_HOST_WAIT);
        });
        socket.emit("hostGameRequest", name, Util.asJson(new GameSettings(questions, time, maxPlayers, isPublic)));
    }

    private int parseToInt(final Label textField) {
        final String text = textField.getText();
        try {
            return Integer.parseInt(text);
        } catch (final NumberFormatException numberFormatException) {
            return 0;
        }
    }

    @Override
    protected void onLoad() {

    }

    @FXML
    void playersDec(final ActionEvent event) {
        final int val = parseToInt(txtPlayers) - 1;
        txtPlayers.setText(val + "");
        checkValues(val, 2, 10, pDec, pInc);
    }

    @FXML
    void playersInc(final ActionEvent event) {
        final int val = parseToInt(txtPlayers) + 1;
        txtPlayers.setText(val + "");
        checkValues(val, 2, 10, pDec, pInc);
    }

    @FXML
    void questionDec(final ActionEvent event) {
        final int val = parseToInt(txtQuestions) - 1;
        txtQuestions.setText(val + "");
        checkValues(val, 2, 25, qDec, qInc);
    }

    @FXML
    void questionInc(final ActionEvent event) {
        final int val = parseToInt(txtQuestions) + 1;
        txtQuestions.setText(val + "");
        checkValues(val, 2, 25, qDec, qInc);
    }

    @FXML
    void timeDec(final ActionEvent event) {
        final int val = parseToInt(txtTime) - 1;
        txtTime.setText(val + "");
        checkValues(val, 5, 30, aDec, aInc);
    }

    @FXML
    void timeInc(final ActionEvent event) {
        final int val = parseToInt(txtTime) + 1;
        txtTime.setText(val + "");
        checkValues(val, 5, 30, aDec, aInc);
    }

    private void checkValues(final int val, final int min, final int max, final Button btnDec, final Button btnInc) {
        btnDec.setDisable(val == min);
        btnInc.setDisable(val == max);
    }
}
