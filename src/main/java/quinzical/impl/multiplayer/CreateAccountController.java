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
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.SceneHandler;

import java.io.IOException;

public class CreateAccountController extends AbstractAlertController {

    @Inject
    private SceneHandler sceneHandler;
    @Inject
    @Named("socketUrl")
    private String socketUrl;

    @FXML
    private AnchorPane background;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXPasswordField txtConfirm;

    @FXML
    final void btnCreate() {
        final String username = txtName.getText().trim();
        final String password = txtPassword.getText();
        if (username.isBlank()) txtName.setPromptText("Please give a valid username");
        if (password.isBlank()) txtPassword.setPromptText("Please give a valid password.");
        if (!password.equals(txtConfirm.getText())) {
            createAlert("Passwords do not match", "Please check that the two password fields are the same.");
            return;
        }

        setProgressVisible(true);

        final OkHttpClient client = new OkHttpClient();

        final RequestBody requestBody = new FormBody.Builder()
            .addEncoded("username", username)
            .addEncoded("password", password)
            .build();

        final Request request = new Request.Builder()
            .url(socketUrl + "/register")
            .post(requestBody)
            .build();

        final Runnable runnable = () -> {
            try {
                final Response response = client.newCall(request).execute();
                final int code = response.code();

                switch (code) {
                    case 200:
                        createAlert("Account Created", "You can now play online!",
                            () -> sceneHandler.setActiveScene(GameScene.MULTI_INTRO));
                        break;
                    case 400:
                    case 403:
                        String responseMessage = "Unknown Error";
                        if (response.body() != null) {
                            final String message = response.body().string();
                            final JSONObject obj = new JSONObject(message);
                            responseMessage = obj.getString("message");
                        }
                        createAlert("Error Creating Account", responseMessage);
                        break;
                }

            } catch (final IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                Platform.runLater(() -> setProgressVisible(false));
            }
        };

        final Thread thread = new Thread(runnable);
        thread.start();
    }

    @FXML
    final void btnQuitClick() {
        sceneHandler.setActiveScene(GameScene.MULTI_INTRO);
    }

    @Override
    protected final void onLoad() {
        setProgressVisible(false);
    }


}
