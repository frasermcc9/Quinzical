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
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextArea;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import quinzical.impl.controllers.AbstractSceneController;
import quinzical.interfaces.multiplayer.ActiveGame;
import quinzical.interfaces.multiplayer.SocketModel;

public class GameController extends AbstractSceneController {

    @Inject
    private ActiveGame activeGame;
    @Inject
    private SocketModel socketModel;

    @FXML
    private Label lblQuestion;
    @FXML
    private JFXTextArea txtInput;
    @FXML
    private Label lblPrompt;
    @FXML
    private Button btnSubmit;
    @FXML
    private HBox macronBar;
    @FXML
    private JFXProgressBar timerProgressBar;
    @FXML
    private Label lblCounter;

    private Timeline timeline;
    private Timeline timelineCountdown;

    @FXML
    void onSubmitClick() {
        timeline.stop();
        timelineCountdown.stop();

        btnSubmit.setDisable(true);
        btnSubmit.setText("Submitted...");
        btnSubmit.setText("Waiting for Others...");
        txtInput.setDisable(true);
        String submission = txtInput.getText();
        if (submission == null) {
            submission = "";
        }
        submission = submission.toLowerCase().trim();
        socketModel.getSocket().emit("questionAnswered", submission);
        activeGame.setGivenSolution(submission);
    }

    @Override
    protected void onLoad() {
        startAnimations();
        initMacronButtons();
        
        lblQuestion.setText(activeGame.getQuestion());
        lblPrompt.setText(activeGame.getPrompt());

        txtInput.focusedProperty().addListener((_l, _o, isFocused) -> focusFixer(isFocused, txtInput));
        txtInput.setOnKeyPressed(this::onKeyPress);

        txtInput.requestFocus();
    }

    private void startAnimations() {
        final ColorAdjust ca = new ColorAdjust();
        ca.setHue(0);
        timerProgressBar.setEffect(ca);
        timeline = new Timeline(
            new KeyFrame(
                Duration.ZERO,
                new KeyValue(timerProgressBar.progressProperty(), 1),
                new KeyValue(ca.hueProperty(), 0)
            ),
            new KeyFrame(
                Duration.seconds(activeGame.getQuestionDuration()),
                new KeyValue(timerProgressBar.progressProperty(), 0),
                new KeyValue(ca.hueProperty(), -0.85)
            )
        );
        timeline.playFromStart();

        lblCounter.setText(Math.round(activeGame.getQuestionDuration()) + "");
        timelineCountdown = new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> lblCounter.setText(Integer.parseInt(lblCounter.getText()) - 1 + "")
            )
        );
        timelineCountdown.setCycleCount(Timeline.INDEFINITE);
        timelineCountdown.playFromStart();
    }

    private void onKeyPress(final KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            if (e.getSource() instanceof JFXTextArea) {
                final JFXTextArea ta = (JFXTextArea) e.getSource();
                final String text = ta.getText().trim();
                ta.setText(text);
                onSubmitClick();
            }

        }
    }

    private void focusFixer(final Boolean isFocused, final JFXTextArea field) {
        if (isFocused) {
            field.setEffect(null);
        } else {
            final ColorAdjust colorFixer = new ColorAdjust();
            colorFixer.setBrightness(1);
            field.setEffect(colorFixer);
        }
    }

    private void initMacronButtons() {
        macronBar.getChildren().stream().filter(b -> b instanceof Button).map(b -> (Button) b).forEach(btn -> btn.setOnAction(e -> {
            if (!txtInput.isEditable()) return;
            txtInput.insertText(txtInput.getCaretPosition(), btn.getText());
            txtInput.requestFocus();
            txtInput.positionCaret(txtInput.getCaretPosition());

            onKeyPress(new KeyEvent(null, null, null, null, false, false, false, false));
        }));
    }

    @FXML
    void onSubmitted(final ActionEvent event) {
        btnSubmit.fire();
    }
}
