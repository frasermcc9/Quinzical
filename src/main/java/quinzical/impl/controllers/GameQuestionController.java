package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import quinzical.Entry;
import quinzical.impl.constants.GameScene;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.impl.util.questionparser.Solution;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.models.structures.Speaker;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GameQuestionController {

    @Inject
    SceneHandler sceneHandler;

    @Inject
    GameModel gameModel;

    @Inject
    Speaker speaker;

    @FXML
    private AnchorPane background;

    @FXML
    private ImageView imgBackground;

    @FXML
    private ImageView imgOverlay;

    @FXML
    private Pane paneSolutions;

    @FXML
    private Label lblQuestion;

    @FXML
    private Label lblPrompt;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button btnPass;

    private List<TextArea> textAreas;

    public String normaliseText(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{M}", "").trim().toLowerCase().replaceFirst("^the", "").trim();
    }

    @FXML
    void initialize() {
        listen();
    }

    private void listen() {
        sceneHandler.onBackgroundChange(img -> this.imgBackground.setImage(img));
        gameModel.onActiveQuestionUpdate(this::initialiseQuestion);
    }

    private void initialiseQuestion(GameQuestion gameQuestion) {

        textAreas = new ArrayList<>();
        paneSolutions.getChildren().clear();

        this.lblQuestion.setText(gameQuestion.getHint());
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
            paneSolutions.getChildren().add(ta);
            textAreas.add(ta);
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
        List<Boolean> corrects = new ArrayList<>();

        for (TextArea textArea : textAreas) {
            String submission = normaliseText(textArea.getText());
            boolean solutionFound = false;
            for (Solution solution : solutions) {
                List<String> variants = solution.getVariants();
                Optional<String> found = variants.stream().filter(v -> normaliseText(v).equals(submission)).findAny();
                if (found.isPresent()) {
                    solutions.remove(solution);
                    corrects.add(true);
                    solutionFound = true;
                    textArea.setStyle("-fx-background-color: #ceffc3; -fx-text-fill: #ceffc3");

                    textArea.setText(found.get());
                    break;
                }
            }
            if (!solutionFound) {
                corrects.add(false);
            }
        }

        for (int i = 0; i < corrects.size(); i++) {
            if (!corrects.get(i)) {
                TextArea textArea = textAreas.get(i);
                textArea.setStyle("-fx-background-color: #ff858c; -fx-text-fill: #ffc7ca");
                String sln = solutions.remove(0).getVariants().get(0);
                if (sln != null)
                    textArea.setText(sln);
            }
        }

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
