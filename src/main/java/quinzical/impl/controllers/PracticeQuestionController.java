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
import com.google.inject.name.Named;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import quinzical.impl.constants.Attempts;
import quinzical.impl.constants.GameScene;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.impl.util.questionparser.Question;
import quinzical.impl.util.questionparser.Solution;
import quinzical.impl.util.strategies.questionverifier.VerifierType;
import quinzical.interfaces.models.PracticeModel;
import quinzical.interfaces.models.QuinzicalModel;

import java.util.List;

/**
 * Controller for the practice question scene
 */
public class PracticeQuestionController extends AbstractQuestionController {

    @Inject
    private PracticeModel gameModel;
    @Inject()
    @Named("attempts")
    private int attempts;

    @FXML
    private Label lblAttempts;
    @FXML
    private Label lblHint;

    /**
     * Called when the submit button is clicked. When called it gets the solutions to the current question and compares
     * them to the correct answers, and depending on how many attempts have been made, either reveals the answer and
     * prep for a new question, or gives the user another attempt, or if correct, move onto the next question regardless
     * of attempts.
     */
    @FXML
    void onSubmitClicked() {

        GameQuestion question = gameModel.getActiveQuestion();
        List<Solution> solutions = question.getSolutionsCopy();
        List<Boolean> corrects;

        solutionContainer.getChildren().forEach(text -> {
            text.setEffect(null);
        });


        attempts++;
        switch (attempts) {
            case 1:
                corrects = questionVerifierFactory.getQuestionVerifier(VerifierType.HIDE_SOLUTION)
                    .verifySolutions(solutions, textAreas);
                if (corrects.contains(false)) {
                    lblAttempts.setText(Attempts.ATTEMPT_2.getMessage());
                    colourTextAreas(textAreas, corrects);
                } else {
                    prepForNewQuestion();
                }
                break;
            case 2:
                corrects = questionVerifierFactory.getQuestionVerifier(VerifierType.HINT_SOLUTION)
                    .verifySolutions(solutions, textAreas);
                if (corrects.contains(false)) {
                    lblAttempts.setText(Attempts.ATTEMPT_3.getMessage());
                    colourTextAreas(textAreas, corrects);
                } else {
                    prepForNewQuestion();
                }
                break;
            case 3:
                corrects = questionVerifierFactory.getQuestionVerifier(VerifierType.FILL_SOLUTION)
                    .verifySolutions(solutions, textAreas);
                if (corrects.contains(false)) {
                    lblAttempts.setText(Attempts.ATTEMPT_4.getMessage());
                }
                prepForNewQuestion();
                break;
        }
    }

    @Override
    protected void refresh() {
        refreshButtonState();
        super.refresh();
    }

    /**
     * Sets up the scene for a new question, resetting the relevant buttons and text areas
     */
    private void prepForNewQuestion() {
        attempts = 0;
        getNewQuestion();
        textAreas.forEach(textArea -> textArea.setEditable(false));
        setSubmitButtonType(ButtonType.PASS);
        btnSubmit.setText("Categories");
        btnSubmit.setOnAction(e -> sceneHandler.setActiveScene(GameScene.PRACTICE));

        progressButtons.getChildren().add(btnNextQuestion);
        btnNextQuestion.setOnAction(e -> sceneHandler.setActiveScene(GameScene.PRACTICE_QUESTION));
        btnNextQuestion.requestFocus();
    }

    /**
     * Gets the gameModel associated with this controller
     *
     * @return the gameModel that this controller is using.
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
        this.lblHint.setText(hint);
        this.lblPrompt.setText(prompt);
    }


    /**
     * Called when the pass button is clicked, to go to a new question.
     */
    @FXML
    void onPassClicked() {
        attempts = 2;
        onSubmitClicked();
    }

    /**
     * Get a new random question, ensuring it is not the same as the previous question, and then setting the question as
     * the gameModels current active question.
     */
    private void getNewQuestion() {
        Question question = gameModel.getRandomQuestion();
        while (question.equals(gameModel.getActiveQuestion())) {
            question = gameModel.getRandomQuestion();
        }
        gameModel.activateQuestion(question);
    }

    /**
     * Re-enables the submit button when a new question is loaded, as it is disabled when in the prepping for question
     * state.
     */
    @Override
    protected void onQuestionLoad() {
        this.btnSubmit.setDisable(false);
    }

}
