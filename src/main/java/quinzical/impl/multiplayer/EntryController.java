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
import io.socket.client.IO;
import io.socket.client.Socket;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import quinzical.impl.constants.GameScene;
import quinzical.impl.controllers.StandardSceneController;
import quinzical.impl.multiplayer.models.SocketModel;
import quinzical.interfaces.models.SceneHandler;

import java.io.IOException;
import java.net.URISyntaxException;

public class EntryController extends StandardSceneController {

    @Inject
    SceneHandler sceneHandler;
    @Inject
    @Named("socketUrl")
    private String socketUrl;

    @FXML
    private TextField txtName;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Label lblProgress;

    private Socket socket;

    @FXML
    void btnConnect() throws URISyntaxException, IOException {

        socket = IO.socket(socketUrl);
        SocketModel.getInstance().setName(txtName.getText()).setSocket(socket).connect();

        setProgressVisible(true);

        socket.once("connect", objects -> sceneHandler.setActiveScene(GameScene.MULTI_MENU));
    }

    @Override
    protected void onLoad() {
        setProgressVisible(false);
    }

    private void setProgressVisible(boolean visible) {
        progressIndicator.setVisible(visible);
        lblProgress.setVisible(visible);
    }

    @FXML
    void btnQuitClick() {
        if (socket != null)
            socket.close();
        sceneHandler.setActiveScene(GameScene.INTRO);
    }
}
