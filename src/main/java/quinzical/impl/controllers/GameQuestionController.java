package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.fxml.FXML;
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

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameQuestionController {

    @Inject
    SceneHandler sceneHandler;

    @Inject
    GameModel gameModel;

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

    private List<TextArea> textAreas;

    public String normaliseText(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{M}", "").trim().toLowerCase();
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

        int slnSize = gameQuestion.getSolutions().size();

        for (int i = 0; i < slnSize; i++) {
            TextArea ta = new TextArea();
            ta.getStylesheets().add(Objects.requireNonNull(Entry.class.getClassLoader().getResource("css/game" +
                "-question.css")).toExternalForm());
            ta.applyCss();
            ta.setPrefWidth(paneSolutions.getPrefWidth());
            ta.setPrefHeight(paneSolutions.getPrefHeight() / slnSize);
            ta.setLayoutY(0 + i * paneSolutions.getPrefHeight() / slnSize);
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
        List<Solution> solutions = gameModel.getActiveQuestion().getSolutionsCopy();

        List<Boolean> corrects = new ArrayList<>();

        for (TextArea text : textAreas) {
            String submission = normaliseText(text.getText());
            for (Solution solution : solutions) {
                List<String> variants = solution.getVariants();
                for (int i = 0; i < variants.size(); i++) {
                    String variant = variants.get(i);
                    String actual = normaliseText(variant);
                    if (actual.equals(submission)) {
                        solutions.clear();
                        corrects.add(true);
                        text.setStyle("-fx-background-color: #aaebff");
                        break;
                    } else if (i + 1 == variants.size()) {
                        text.setStyle("-fx-background-color: #ff858c");
                        corrects.add(false);
                    }
                }
                if (solutions.size() == 0) break;
            }
        }

        gameModel.answerActive();


        if (corrects.stream().allMatch(e -> e)) {
            handleCorrect();
        } else {
            handleIncorrect();
        }
    }

    private void handleCorrect() {
        sceneHandler.setActiveScene(GameScene.GAME);
    }

    private void handleIncorrect() {
        sceneHandler.setActiveScene(GameScene.GAME);
    }


}
