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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import quinzical.impl.controllers.StandardSceneController;
import quinzical.impl.multiplayer.models.SocketModel;
import quinzical.interfaces.multiplayer.ActiveGame;

public class GameController extends StandardSceneController {

    private final Socket socket = SocketModel.getInstance().getSocket();
    private final String name = SocketModel.getInstance().getName();
    @Inject
    private ActiveGame activeGame;

    @FXML
    private Label lblQuestion;

    @FXML
    private TextField txtInput;

    @FXML
    private Label lblPrompt;

    @FXML
    private Label lblStatus;

    @FXML
    private Button btnSubmit;

    @FXML
    void onSubmitClick(ActionEvent event) {
        btnSubmit.setDisable(true);
        btnSubmit.setText("Submitted...");
        lblStatus.setText("Waited for Others to Submit...");
        txtInput.setDisable(true);
        String submission = txtInput.getText();
        if (submission == null) {
            submission = "";
        }
        submission = submission.toLowerCase().trim();
        socket.emit("questionAnswered", submission);
        activeGame.setGivenSolution(submission);
    }

    @Override
    protected void onLoad() {
        lblQuestion.setText(activeGame.getQuestion());
        lblPrompt.setText(activeGame.getPrompt());
        txtInput.requestFocus();
    }

    @FXML
    void onSubmitted(ActionEvent event) {
        btnSubmit.fire();
    }
}
