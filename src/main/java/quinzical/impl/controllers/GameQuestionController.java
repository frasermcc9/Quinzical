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

package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import quinzical.impl.constants.GameScene;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.impl.util.questionparser.Solution;
import quinzical.impl.util.strategies.questionverifier.VerifierType;
import quinzical.impl.util.strategies.timer.TimerType;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.QuinzicalModel;
import quinzical.interfaces.strategies.timer.TimerContext;
import quinzical.interfaces.strategies.timer.TimerStrategy;

import java.util.List;

/**
 * Controls the question scene for the main game
 */
public class GameQuestionController extends AbstractQuestionController {

    @Inject
    TimerContext timerContext;

    @Inject
    private GameModel gameModel;

    @FXML
    private Label lblPrompt;

    @FXML
    private ProgressBar timerProgressBar;

    @FXML
    private HBox progressButtons;

    @FXML
    private Button btnNextQuestion;

    private TimerStrategy activeTimer;


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

    /**
     * submits the currently inputted text as an answer to the question
     */
    @FXML
    void onSubmitClicked() {
        activeTimer.stopTimeout();

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
        gameModel.answerActive(false);
        startTimer();
    }

    private void startTimer() {
        final Timeline timeline = new Timeline(
            new KeyFrame(
                Duration.ZERO,
                new KeyValue(timerProgressBar.progressProperty(), 1)
            ),
            new KeyFrame(
                Duration.seconds(gameModel.getTimerValue()),
                new KeyValue(timerProgressBar.progressProperty(), 0)
            )
        );
        timeline.playFromStart();

        activeTimer = timerContext.createTimer(TimerType.DEFAULT);
        activeTimer.setTimeout(() -> Platform.runLater(() -> btnSubmit.fire()),
            (int) gameModel.getTimerValue() * 1000);
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

    private void handleCompletion() {
        sceneHandler.setActiveScene(GameScene.END);
    }

    /**
     * refreshes the buttons onAction calls, back to the normal functions, as they change when a question is just
     * answered.
     */
    private void refreshButtonState() {
        progressButtons.getChildren().subList(1, progressButtons.getChildren().size()).clear();
        setSubmitButtonType(ButtonType.PASS);
    }

    @Override
    protected void refresh() {
        refreshButtonState();
        super.refresh();
    }

    @Override
    protected void keyPressed(KeyCode keyCode) {
        if (keyCode == KeyCode.BACK_SPACE) {
            if (textAreas.stream().anyMatch(t -> t.getText().isEmpty())) {
                setSubmitButtonType(ButtonType.PASS);
            }
        } else {
            setSubmitButtonType(ButtonType.SUBMIT);
        }
    }

    private void setSubmitButtonType(ButtonType buttonType) {
        btnSubmit.getStyleClass().clear();
        btnSubmit.getStyleClass().addAll("transparent", "button");
        switch (buttonType) {
            case PASS:
                btnSubmit.getStyleClass().add("pass-button");
                btnSubmit.setText("Pass");
                btnSubmit.setOnAction(e -> onSubmitClicked());
                break;
            case SUBMIT:
                btnSubmit.getStyleClass().add("submit-button");
                btnSubmit.setText("Submit");
                btnSubmit.setOnAction(e -> onSubmitClicked());
                break;
        }
    }

    private enum ButtonType {
        PASS, SUBMIT
    }
}
