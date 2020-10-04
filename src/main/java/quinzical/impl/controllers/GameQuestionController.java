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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import quinzical.impl.constants.GameScene;
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

    //#region Injected classes

    @Inject
    private GameModel gameModel;


    //#endregion

    //#region Injected FXML components

    @FXML
    private Label lblPrompt;

    @FXML
    private Button btnPass;

    //#endregion


    //#region Initialisation at FXML load


    /**
     * Sets up the Game Question scene.
     */
    @FXML
    void initialize() {
        listen();
        initMacronButtons();
    }


    //#endregion

    //#region Injected handlers

    /**
     * Repeats the question using the speaker.speak method
     */
    @FXML
    void onReplayClick() {
        String s = gameModel.getActiveQuestion().getHint();
        speaker.speak(s);
    }

    /**
     * submits the currently inputted text as an answer to the question
     */
    @FXML
    void onPassClicked() {
        onSubmitClicked();
    }

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
     * @param hint - the current hint being read out
     * @param prompt - the prompt for the current question to be set in the label
     */
    @Override
    protected void setPrompts(String hint, String prompt) {
        this.lblPrompt.setText(prompt);
    }

    /**
     * submits the currently inputted text as an answer to the question
     */
    @FXML
    void onSubmitClicked() {
        GameQuestion question = gameModel.getActiveQuestion();

        List<Solution> solutions = question.getSolutionsCopy();

        textAreas.forEach(textArea -> textArea.setEditable(false));

        List<Boolean> corrects =
            questionVerifierFactory.getQuestionVerifier(VerifierType.FILL_SOLUTION).verifySolutions(solutions,
                textAreas);

        gameModel.answerActive(corrects.stream().allMatch(e -> e));


        btnSubmit.setText("Categories");
        btnSubmit.setOnAction(this::handleReturnToCategories);
        btnPass.setText("Next Question");
        btnPass.setOnAction(e -> handleNextQuestion(question));

        btnPass.requestFocus();
    }

    //#endregion

    /**
     * Makes it so that when a question is initially set as active, it will be 
     * set as incorrectly answered.
     */
    @Override
    protected void onQuestionLoad() {
        gameModel.answerActive(false);
    }

    /**
     * Handles the return to the main game scene.
     */
    private void handleReturnToCategories(ActionEvent e) {
        refreshSubmissionButtonsState();

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
        refreshSubmissionButtonsState();

        if (gameModel.numberOfQuestionsRemaining() == 0) {
            handleCompletion();
            return;
        }

        GameQuestion next = gameModel.getNextActiveQuestion(question);
        if (next == null) {
            sceneHandler.setActiveScene(GameScene.GAME);
        } else {
            gameModel.activateQuestion(next);
        }

    }

    /**
     * refreshes the buttons onAction calls, back to the normal functions,
     * as they change when a question is just answered.
     */
    private void refreshSubmissionButtonsState() {
        btnSubmit.setOnAction(e -> onSubmitClicked());
        btnPass.setOnAction(e -> onSubmitClicked());
        btnPass.setText("Pass");
        btnSubmit.setText("Submit");
    }

    private void handleCompletion() {
        sceneHandler.setActiveScene(GameScene.END);
    }

}
