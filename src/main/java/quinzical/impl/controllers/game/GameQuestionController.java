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

package quinzical.impl.controllers.game;

import com.google.inject.Inject;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.ColorAdjust;
import javafx.util.Duration;
import quinzical.impl.constants.GameScene;
import quinzical.impl.controllers.AbstractQuestionController;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.impl.util.questionparser.Solution;
import quinzical.impl.util.strategies.questionverifier.VerifierType;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.QuinzicalModel;

import java.util.List;

/**
 * Controls the question scene for the main game
 */
public class GameQuestionController extends AbstractQuestionController {

    @Inject
    private GameModel gameModel;

    @FXML
    private ProgressBar timerProgressBar;
    @FXML
    private Label lblCounter;

    private Timeline timeline;
    private Timeline timelineCountdown;


    // #region Injected handlers

    /**
     * gets the gameModel associated with this controller.
     *
     * @return - the gameModel that this controller uses
     */
    @Override
    protected QuinzicalModel getGameModel() {
        return this.gameModel;
    }

    /**
     * Sets the Text of the prompt for the current question in the correct label
     *
     * @param hint   - the current hint being read out
     * @param prompt - the prompt for the current question to be set in the label
     */
    @Override
    protected void setPrompts(String hint, String prompt) {
        this.lblPrompt.setText(prompt);
    }

    // #endregion


    @Override
    protected void onPassClicked() {
        onSubmitClicked();
    }

    /**
     * submits the currently inputted text as an answer to the question
     */
    @FXML
    protected void onSubmitClicked() {
        timeline.stop();
        timelineCountdown.stop();
        textAreas.forEach(a -> a.setEffect(null));

        GameQuestion question = gameModel.getActiveQuestion();

        List<Solution> solutions = question.getSolutionsCopy();

        textAreas.forEach(textArea -> textArea.setEditable(false));

        List<Boolean> corrects = questionVerifierFactory.getQuestionVerifier(VerifierType.FILL_SOLUTION)
            .verifySolutions(solutions, textAreas);

        gameModel.answerActive(corrects.stream().allMatch(e -> e));

        setSubmitButtonType(ButtonType.PASS);
        btnSubmit.setText("Categories");
        btnSubmit.setOnAction(this::handleReturnToCategories);

        progressButtons.getChildren().add(btnNextQuestion);
        btnNextQuestion.setOnAction(e -> handleNextQuestion(question));
        btnNextQuestion.requestFocus();

    }

    /**
     * Makes it so that when a question is initially set as active, it will be set as incorrectly answered.
     */
    @Override
    protected void onQuestionLoad() {
        lblCounter.setText(Math.round(gameModel.getTimerValue()) + "");
        timerProgressBar.setProgress(1);
        gameModel.answerActive(false);
    }

    @Override
    protected void speakQuestion(String question) {
        speaker.speak(question, this::startTimer);
    }

    @Override
    protected void refresh() {
        refreshButtonState();
        super.refresh();
    }

    /**
     * Handles the return to the main game scene.
     */
    private void handleReturnToCategories(ActionEvent e) {
        if (gameModel.numberOfQuestionsRemaining() == 0) {
            handleCompletion();
            return;
        }
        sceneHandler.setActiveScene(GameScene.GAME);
    }

    /**
     * handles the moving to the next question, used when the pass button is clicked
     *
     * @param question - The current active question.
     */
    private void handleNextQuestion(GameQuestion question) {
        if (gameModel.numberOfQuestionsRemaining() == 0) {
            handleCompletion();
            return;
        }

        GameQuestion next = gameModel.getNextActiveQuestion(question);
        if (next == null) {
            sceneHandler.setActiveScene(GameScene.GAME);
        } else {
            gameModel.activateQuestion(next);
            refresh();
        }

    }

    /**
     * Displays the end of game view
     */
    private void handleCompletion() {
        sceneHandler.setActiveScene(GameScene.END);
    }

    /**
     * Starts the timer for the question answering
     */
    private void startTimer() {
        ColorAdjust ca = new ColorAdjust();
        ca.setHue(0);
        timerProgressBar.setEffect(ca);

        timeline = new Timeline(
            new KeyFrame(
                Duration.ZERO,
                new KeyValue(timerProgressBar.progressProperty(), 1),
                new KeyValue(ca.hueProperty(), 0)
            ),
            new KeyFrame(
                Duration.seconds(gameModel.getTimerValue()),
                new KeyValue(timerProgressBar.progressProperty(), 0),
                new KeyValue(ca.hueProperty(), -0.85)
            )
        );

        timelineCountdown = new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> lblCounter.setText(Integer.parseInt(lblCounter.getText()) - 1 + "")
            )
        );

        timelineCountdown.setCycleCount(Timeline.INDEFINITE);
        timeline.setOnFinished((event) -> Platform.runLater(() -> {
            btnSubmit.fire();
            lblCounter.setText("0");
        }));

        timelineCountdown.playFromStart();
        timeline.playFromStart();
    }


}
