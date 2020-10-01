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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import quinzical.Entry;
import quinzical.impl.constants.GameScene;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.models.structures.Speaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PracticeQuestionController {


    
    @Inject
    private SceneHandler sceneHandler;

    @Inject
    private GameModel gameModel;

    @Inject
    private Speaker speaker;

    @FXML
    private ImageView imgBackground;

    @FXML
    private Pane paneSolutions;
    
    @FXML
    private ImageView imgOverlay;
    
    @FXML
    private Label lblPrompt;

    @FXML
    private Button btnSubmit;
    
    @FXML
    private Label lblAttempts;
    
    @FXML
    private Label lblHint;

    private List<TextArea> textAreas;
    
    @FXML
    void onSubmitClicked(ActionEvent actionEvent) {
    }

    @FXML
    void onPassClicked(ActionEvent actionEvent) {
    }

    @FXML
    void onReplyClick(MouseEvent mouseEvent) {
        speaker.speak(gameModel.getActiveQuestion().getHint());
    }

    @FXML
    void onBackClicked(ActionEvent actionEvent) {
        sceneHandler.setActiveScene(GameScene.PRACTICE);
    }

    private void listen() {
        sceneHandler.onBackgroundChange(img -> this.imgBackground.setImage(img));
        gameModel.onActiveQuestionUpdate(this::initialiseQuestion);
    }

    private void initialiseQuestion(GameQuestion gameQuestion) {

        
        textAreas = new ArrayList<>();
        paneSolutions.getChildren().clear();

        this.lblAttempts.setText("Attempt 1/3");
        this.lblHint.setText(gameQuestion.getHint());
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
    void initialize() {
        listen();
    }
    

}
