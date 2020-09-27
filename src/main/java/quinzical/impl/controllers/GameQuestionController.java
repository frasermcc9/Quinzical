package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import quinzical.Entry;
import quinzical.impl.constants.GameScene;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.impl.util.questionparser.Solution;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.models.structures.Speaker;
import quinzical.interfaces.strategies.questionverifier.QuestionVerifierFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameQuestionController {

    @Inject
    private SceneHandler sceneHandler;

    @Inject
    private GameModel gameModel;

    @Inject
    private Speaker speaker;

    @Inject
    private QuestionVerifierFactory questionVerifierFactory;

    @FXML
    private AnchorPane background;

    @FXML
    private ImageView imgBackground;

    @FXML
    private ImageView imgOverlay;

    @FXML
    private Pane paneSolutions;

    @FXML
    private Label lblPrompt;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button btnPass;

    private List<TextArea> textAreas;

    @FXML
    void initialize() {
        listen();
    }

    private void listen() {
        sceneHandler.onBackgroundChange(img -> this.imgBackground.setImage(img));
        gameModel.onActiveQuestionUpdate(this::initialiseQuestion);
    }

    @FXML
    void onReplyClick() {
        String s = gameModel.getActiveQuestion().getHint();
        speaker.speak(s);
    }

    private void initialiseQuestion(GameQuestion gameQuestion) {

        textAreas = new ArrayList<>();
        paneSolutions.getChildren().clear();

        this.lblPrompt.setText(gameQuestion.getPrompt() + ":");

        speaker.speak(gameQuestion.getHint());

        int slnSize = gameQuestion.getSolutions().size();

        for (int i = 0; i < slnSize; i++) {
            TextArea ta = new TextArea();
            ta.getStylesheets().add(Objects.requireNonNull(Entry.class.getClassLoader().getResource("css/game" +
                "-question.css")).toExternalForm());
            ta.applyCss();
            ta.setPrefWidth(paneSolutions.getPrefWidth());
            ta.setMaxHeight(paneSolutions.getPrefHeight() / slnSize - slnSize);
            ta.setLayoutY(0 + i * paneSolutions.getPrefHeight() / slnSize + 1);
            ta.setPromptText("Enter your solution here...");
            ta.setOnKeyPressed(this::onEnterPressed);
            paneSolutions.getChildren().add(ta);
            textAreas.add(ta);
        }
        textAreas.get(0).requestFocus();
    }

    private void onEnterPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            if (e.getSource() instanceof TextArea) {
                TextArea ta = (TextArea) e.getSource();
                ta.setText(ta.getText().trim());
                int idx = textAreas.indexOf(ta);
                if (idx + 1 == textAreas.size()) {
                    btnSubmit.fire();
                } else {
                    textAreas.get(idx + 1).requestFocus();
                }
            }

        }
    }

    @FXML
    void onPassClicked() {
        onSubmitClicked();
    }

    @FXML
    void onSubmitClicked() {
        GameQuestion question = gameModel.getActiveQuestion();

        List<Solution> solutions = question.getSolutionsCopy();

        List<Boolean> corrects = questionVerifierFactory.getQuestionVerifier().verifySolutions(solutions, textAreas);
        
        gameModel.answerActive(corrects.stream().allMatch(e -> e));

        btnSubmit.setText("Categories");
        btnSubmit.setOnAction(this::handleReturnToCategories);
        btnPass.setText("Next Question");
        btnPass.setOnAction(e -> handleNextQuestion(question));

    }

    private void handleReturnToCategories(ActionEvent e) {
        btnSubmit.setOnAction(_e -> onSubmitClicked());
        btnPass.setOnAction(_e -> onSubmitClicked());
        btnPass.setText("Pass");
        btnSubmit.setText("Submit");

        sceneHandler.setActiveScene(GameScene.GAME);

    }

    private void handleNextQuestion(GameQuestion question) {
        btnSubmit.setOnAction(_e -> onSubmitClicked());
        btnPass.setOnAction(_e -> onSubmitClicked());
        btnPass.setText("Pass");
        btnSubmit.setText("Submit");

        GameQuestion next = gameModel.getNextActiveQuestion(question);
        if (next == null) {
            sceneHandler.setActiveScene(GameScene.GAME);
        } else {
            gameModel.activateQuestion(next);
        }

    }


}
