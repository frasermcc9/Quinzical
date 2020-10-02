package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import quinzical.Entry;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.interfaces.models.QuinzicalModel;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.models.structures.Speaker;
import quinzical.interfaces.strategies.questionverifier.QuestionVerifierFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractQuestionController {

    @Inject
    protected SceneHandler sceneHandler;

    @Inject
    protected Speaker speaker;

    @Inject
    protected QuestionVerifierFactory questionVerifierFactory;

    @FXML
    protected Pane paneSolutions;

    @FXML
    protected ImageView imgBackground;
    @FXML
    protected Button btnSubmit;
    protected List<TextArea> textAreas;
    protected TextArea activeText = null;
    @FXML
    private ButtonBar macronBar;

    protected abstract QuinzicalModel getGameModel();

    protected abstract void setPrompts(String hint, String prompt);

    /**
     * Optional hook
     */
    protected void adjustQuestionOnLoad() {
    }

    protected void listen() {
        //Listens for theme change
        sceneHandler.onBackgroundChange(this.imgBackground::setImage);
        //Listens for when a new active question is activated
        getGameModel().onActiveQuestionUpdate(this::initialiseQuestion);
    }

    protected void initialiseQuestion() {

        GameQuestion gameQuestion = getGameModel().getActiveQuestion();

        adjustQuestionOnLoad();

        textAreas = new ArrayList<>();
        paneSolutions.getChildren().clear();

        setPrompts(gameQuestion.getHint(), gameQuestion.getPrompt() + ":");
        
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

            // Listen for when the text area is focused. When it is focused, set activeText to this text area. This is
            // for knowing which text area to insert the macron characters into when the buttons are chosen.
            ta.focusedProperty().addListener((_l, _o, isFocused) -> activeText = isFocused ? ta : activeText);

            paneSolutions.getChildren().add(ta);
            textAreas.add(ta);
        }
        textAreas.get(0).requestFocus();
    }

    protected void onEnterPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            if (e.getSource() instanceof TextArea) {
                TextArea ta = (TextArea) e.getSource();
                ta.setText(ta.getText().replace("\n", ""));
                int idx = textAreas.indexOf(ta);
                if (idx + 1 == textAreas.size()) {
                    btnSubmit.fire();
                } else {
                    textAreas.get(idx + 1).requestFocus();
                }
            }

        }
    }

    protected void initMacronButtons() {
        //gets each button in the bar, and programs its handler
        // Gets the text and current cursor location from the selected text area and inserts the selected 
        // macron character after the cursor. Then reselect the text area and set the cursor to after the 
        // inserted character.
        macronBar.getButtons().stream().filter(b -> b instanceof Button).map(b -> (Button) b).forEach(btn -> btn.setOnAction(e -> {
            String text = activeText.getText();
            int currentPos = activeText.getCaretPosition();
            String newText = text.substring(0, currentPos) + btn.getText() + text.substring(currentPos);
            activeText.setText(newText);
            activeText.requestFocus();
            activeText.positionCaret(currentPos + 1);
        }));
    }
}
