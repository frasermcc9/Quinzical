package quinzical.impl.controllers;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

public class PracticeQuestionController extends AbstractQuestionController {

    @Inject
    private PracticeModel gameModel;

    @FXML
    private Label lblPrompt;

    @FXML
    private Button btnPass;

    @FXML
    private Label lblAttempts;

    @FXML
    private Label lblHint;

    @Inject()
    @Named("attempts")
    private int attempts;


    @FXML
    void onSubmitClicked() {

        GameQuestion question = gameModel.getActiveQuestion();
        List<Solution> solutions = question.getSolutionsCopy();
        List<Boolean> corrects;

        attempts++;
        switch (attempts) {
            case 1:
                corrects =
                    questionVerifierFactory.getQuestionVerifier(VerifierType.HIDE_SOLUTION).verifySolutions(solutions,
                        textAreas);
                if (corrects.contains(false)) {
                    lblAttempts.setText(Attempts.ATTEMPT_2.getMessage());
                    gameModel.colourTextAreas(textAreas, corrects);
                } else {
                    attempts = 0;
                    btnPass.requestFocus();
                    prepForNewQuestion();
                }
                break;
            case 2:
                corrects =
                    questionVerifierFactory.getQuestionVerifier(VerifierType.HINT_SOLUTION).verifySolutions(solutions,
                        textAreas);
                if (corrects.contains(false)) {
                    lblAttempts.setText(Attempts.ATTEMPT_3.getMessage());
                    gameModel.colourTextAreas(textAreas, corrects);
                } else {
                    attempts = 0;
                    btnPass.requestFocus();
                    prepForNewQuestion();
                }
                break;
            case 3:
                corrects =
                    questionVerifierFactory.getQuestionVerifier(VerifierType.FILL_SOLUTION).verifySolutions(solutions,
                        textAreas);
                if (!corrects.contains(false)) {
                    attempts = 0;
                    btnPass.requestFocus();
                }
                textAreas.forEach(textArea -> textArea.setEditable(false));
                prepForNewQuestion();
                break;
        }
    }

    @Override
    protected QuinzicalModel getGameModel() {
        return this.gameModel;
    }

    @Override
    protected void setPrompts(String hint, String prompt) {
        this.lblHint.setText(hint);
        this.lblPrompt.setText(prompt);
    }

    private void prepForNewQuestion() {
        btnPass.setText("Next Question");
        btnSubmit.setDisable(true);
        btnPass.setOnAction(e -> getNewQuestion());
    }

    private void getNewQuestion() {
        Question question = gameModel.getRandomQuestion(gameModel.getActiveQuestion().getCategory());
        gameModel.activateQuestion(question);

        btnSubmit.setDisable(false);
        btnPass.setText("Pass");
        btnPass.setOnAction(e -> getNewQuestion());
        this.lblAttempts.setText(Attempts.ATTEMPT_1.getMessage());
    }

    @FXML
    void onPassClicked() {
        getNewQuestion();
    }

    @FXML
    void onReplayClick() {
        speaker.speak(gameModel.getActiveQuestion().getHint());
    }

    @FXML
    void onBackClicked(ActionEvent actionEvent) {
        sceneHandler.setActiveScene(GameScene.PRACTICE);
    }


    @FXML
    void initialize() {
        this.lblAttempts.setText(Attempts.ATTEMPT_1.getMessage());
        initMacronButtons();
        listen();
    }


}
