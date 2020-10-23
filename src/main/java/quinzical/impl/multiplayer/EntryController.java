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
import com.google.inject.name.Named;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import io.socket.client.IO;
import io.socket.client.Socket;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import org.json.JSONException;
import quinzical.impl.constants.GameScene;
import quinzical.impl.multiplayer.util.Util;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.multiplayer.SocketModel;

import java.net.URISyntaxException;

public class EntryController extends AbstractAlertController {

    @Inject
    private SceneHandler sceneHandler;
    @Inject
    @Named("socketUrl")
    private String socketUrl;
    @Inject
    private SocketModel socketModel;

    @FXML
    private JFXTextField txtName;
    @FXML
    private JFXPasswordField txtPassword;

    private static void createValidator(String message, TextField textField) {
        textField.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                if (textField.getText().isBlank()) {
                    textField.setEffect(null);
                    textField.getStyleClass().add("invalid-field");
                }
            } else {
                ColorAdjust ca = new ColorAdjust();
                ca.setBrightness(1);
                textField.setEffect(ca);
                textField.getStyleClass().remove("invalid-field");
            }
        });
    }

    @FXML
    void btnConnect() throws URISyntaxException, IllegalAccessException, NoSuchFieldException, JSONException {

        Socket socket = IO.socket(socketUrl);
        socketModel.setName(txtName.getText()).setSocket(socket);
        setProgressVisible(true);

        Object login = Util.asJson(new Login(txtName.getText(), txtPassword.getText()));

        socket.once("connect", (_1) -> {
            socket.emit("authentication", login);
            socket.once("authenticated",
                _2 -> Platform.runLater(() -> sceneHandler.setActiveScene(GameScene.MULTI_MENU)));
            socket.once("unauthorized", _3 -> Platform.runLater(() -> {
                createAlert("Could not Connect", "Could not connect to the online service. Please check your login " +
                    "details and try again.");

                setProgressVisible(false);
            }));
        });

        socketModel.connect();
    }

    @FXML
    void btnAccount() {
        sceneHandler.setActiveScene(GameScene.MULTI_ACCOUNT);
    }

    @Override
    protected void onLoad() {
        setProgressVisible(false);
        createValidator("Username cannot be blank", txtName);
        createValidator("Password cannot be blank", txtPassword);
    }


    @FXML
    void btnQuitClick() {
        if (socketModel.getSocket() != null)
            socketModel.destroy();
        sceneHandler.setActiveScene(GameScene.INTRO);
    }

    public static class Login {
        public final String username;
        public final String password;

        public Login(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
