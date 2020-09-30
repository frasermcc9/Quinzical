package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

    //#region Injected classes

    @Inject
    private SceneHandler sceneHandler;

    @Inject
    private GameModel gameModel;

    @Inject
    private Speaker speaker;

    @Inject
    private QuestionVerifierFactory questionVerifierFactory;

    //#endregion

    //#region Injected FXML components

    @FXML
    private ImageView imgBackground;

    @FXML
    private Pane paneSolutions;

    @FXML
    private Label lblPrompt;

    @FXML
    private Button btnSubmit;

    @FXML
    private Button btnPass;

    @FXML
    private ButtonBar macronBar;

    //#endregion

    private List<TextArea> textAreas;

    private TextArea activeText = null;

    //#region Initialisation at FXML load

    @FXML
    void initialize() {
        listen();
        initMacronButtons();
    }

    private void listen() {
        //Listens for theme change
        sceneHandler.onBackgroundChange(this.imgBackground::setImage);
        //Listens for when a new active question is activated
        gameModel.onActiveQuestionUpdate(this::initialiseQuestion);
    }

    /**
     * This sets up the macron buttons
     */
    private void initMacronButtons() {
        //gets each button in the bar, and programs its handler
        macronBar.getButtons().forEach(b -> {
            if (b instanceof Button) {
                Button btn = (Button) b;
                // Gets the text and current cursor location from the selected text area and inserts the selected 
                // macron character after the cursor. Then reselect the text area and set the cursor to after the 
                // inserted character.
                btn.setOnAction(e -> {
                    String text = activeText.getText();
                    int currentPos = activeText.getCaretPosition();
                    String newText = text.substring(0, currentPos) + btn.getText() + text.substring(currentPos);
                    activeText.setText(newText);
                    activeText.requestFocus();
                    activeText.positionCaret(currentPos + 1);
                });
            }
        });
    }

    //#endregion

    //#region Injected handlers

    @FXML
    void onReplyClick() {
        String s = gameModel.getActiveQuestion().getHint();
        speaker.speak(s);
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

    //#endregion

    /**
     * Runs whenever a new active question is set.
     */
    private void initialiseQuestion() {

        GameQuestion gameQuestion = gameModel.getActiveQuestion();

        // As soon as the question is 
        gameModel.answerActive(false);

        // Clear the text area list and the pane of any existing text areas from previous questions.
        textAreas = new ArrayList<>();
        paneSolutions.getChildren().clear();

        //set the prompt text (i.e. what is...)
        this.lblPrompt.setText(gameQuestion.getPrompt() + ":");

        //speak the question
        speaker.speak(gameQuestion.getHint());

        // Generate a text area for each solution.
        int slnSize = gameQuestion.getSolutions().size();
        for (int i = 0; i < slnSize; i++) {
            TextArea ta = new TextArea();
            // Style the text area
            ta.getStylesheets().add(Objects.requireNonNull(Entry.class.getClassLoader().getResource("css/game" +
                "-question.css")).toExternalForm());
            ta.applyCss();
            // Apply layout to the text area so it fits in the bounding box
            ta.setPrefWidth(paneSolutions.getPrefWidth());
            ta.setMaxHeight(paneSolutions.getPrefHeight() / slnSize - slnSize);
            ta.setLayoutY(0 + i * paneSolutions.getPrefHeight() / slnSize + 1);

            // Listen for when a key is pressed in the text area. This function listens for the enter key as a 
            // keyboard shortcut.
            ta.setOnKeyPressed(this::onTextAreaKeyPress);

            // Listen for when the text area is focused. When it is focused, set activeText to this text area. This is
            // for knowing which text area to insert the macron characters into when the buttons are chosen.
            ta.focusedProperty().addListener((_l, _o, isFocused) -> activeText = isFocused ? ta : activeText);

            //Add the text area to the pane and to the list of text areas.
            paneSolutions.getChildren().add(ta);
            textAreas.add(ta);
        }
        textAreas.get(0).requestFocus();
    }

    private void handleReturnToCategories(ActionEvent e) {
        refreshSubmissionButtonsState();
        sceneHandler.setActiveScene(GameScene.GAME);
    }

    private void handleNextQuestion(GameQuestion question) {
        refreshSubmissionButtonsState();
        GameQuestion next = gameModel.getNextActiveQuestion(question);
        if (next == null) {
            sceneHandler.setActiveScene(GameScene.GAME);
        } else {
            gameModel.activateQuestion(next);
        }

    }

    private void refreshSubmissionButtonsState() {
        btnSubmit.setOnAction(e -> onSubmitClicked());
        btnPass.setOnAction(e -> onSubmitClicked());
        btnPass.setText("Pass");
        btnSubmit.setText("Submit");
    }

    private void onTextAreaKeyPress(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            if (e.getSource() instanceof TextArea) {
                TextArea ta = (TextArea) e.getSource();
                ta.setText(ta.getText().trim());
                int idx = textAreas.indexOf(ta);
                if (idx + 1 == textAreas.size()) {
                    btnSubmit.fire();
                    btnPass.requestFocus();
                } else {
                    textAreas.get(idx + 1).requestFocus();
                }
            }

        }
    }

}
