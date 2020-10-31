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
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import quinzical.impl.controllers.game.GameQuestionController;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.interfaces.models.QuinzicalModel;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.models.structures.Speaker;
import quinzical.interfaces.strategies.questionverifier.QuestionVerifierFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class used by all of the question controllers
 */
public abstract class AbstractQuestionController extends AbstractSceneController {

    @Inject
    protected SceneHandler sceneHandler;
    @Inject
    protected Speaker speaker;
    @Inject
    protected QuestionVerifierFactory questionVerifierFactory;

    @FXML
    protected VBox solutionContainer;
    @FXML
    protected HBox progressButtons;
    @FXML
    protected Button btnSubmit;
    @FXML
    protected Button btnNextQuestion;
    @FXML
    protected HBox macronBar;
    @FXML
    protected Label lblPrompt;

    protected List<JFXTextArea> textAreas;
    protected JFXTextArea activeText = null;

    /**
     * Sets the colour of the text areas to either red or green, depending on if
     * they were incorrect or correct as per the corrects list.
     *
     * @param textAreas - The list of textAreas to be coloured
     * @param corrects  - The list of Boolean values showing which text areas are
     *                  right and wrong.
     */
    protected static void colourTextAreas(final List<JFXTextArea> textAreas, final List<Boolean> corrects) {
        for (int i = 0; i < textAreas.size(); i++) {
            if (corrects.get(i)) {
                textAreas.get(i).getStyleClass().add("answer-field-right");
            } else {
                textAreas.get(i).getStyleClass().add("answer-field-wrong");
            }
        }
    }

    protected abstract QuinzicalModel getGameModel();

    protected abstract void setPrompts(String hint, String prompt);

    protected abstract void onSubmitClicked();

    protected abstract void onPassClicked();

    /**
     * Checks if all the text fields are empty when a button is pressed, setting the
     * submit button to pass if so and submit otherwise
     */
    protected final void keyPressed(final KeyCode keyCode) {
        if (!textAreas.get(0).isEditable()) return;
        if (keyCode == KeyCode.ENTER) return;
        
        if (keyCode == KeyCode.BACK_SPACE) {
            if (textAreas.stream().allMatch(t -> t.getText().isEmpty())) {
                setSubmitButtonType(GameQuestionController.ButtonType.PASS);
            }
        } else {
            Platform.runLater(() -> {
                if (activeText.getText().length() > 0) {
                    setSubmitButtonType(GameQuestionController.ButtonType.SUBMIT);
                }
            });
        }
    }

    @Override
    protected void refresh() {
        initialiseQuestion();
        focusRequester(textAreas.get(0), 10);
    }

    /**
     * Ensures that the macron buttons will be initialised when this scene is loaded
     */
    @Override
    protected final void onLoad() {
        initMacronButtons();
    }

    /**
     * Optional hook
     */
    protected void onQuestionLoad() {
    }

    /**
     * Hook with default behaviour
     */
    protected void initialSpeak(final String question) {
        speaker.speak(question);
    }

    /*
     * Sets up the current question from the game model, setting all of the
     * relevant text fields and calls the speaking of the question
     */
    protected final void initialiseQuestion() {

        final GameQuestion gameQuestion = getGameModel().getActiveQuestion();

        onQuestionLoad();

        textAreas = new ArrayList<>();
        solutionContainer.getChildren().clear();

        setPrompts(gameQuestion.getHint(), gameQuestion.getPrompt() + ":");

        initialSpeak(gameQuestion.getHint());

        final int slnSize = gameQuestion.getSolutions().size();

        for (int i = 0; i < slnSize; i++) {
            final JFXTextArea field = new JFXTextArea();
            field.getStyleClass().add("answer-field-default");
            field.setPromptText("Enter your solution here...");

            final ColorAdjust ca = new ColorAdjust();
            ca.setBrightness(1);
            field.setEffect(ca);

            // Listen for when the text area is focused. When it is focused, set activeText
            // to this text area. This is
            // for knowing which text area to insert the macron characters into when the
            // buttons are chosen.
            field.focusedProperty().addListener((_l, _o, isFocused) -> focusFixer(isFocused, field));
            field.setOnKeyPressed(this::onKeyPress);
            field.setPrefHeight(0);

            solutionContainer.getChildren().add(field);
            textAreas.add(field);
        }
    }

    private void focusFixer(final Boolean isFocused, final JFXTextArea field) {
        activeText = isFocused ? field : activeText;
        if (isFocused || !textAreas.get(0).isEditable()) {
            field.setEffect(null);
        } else {
            final ColorAdjust colorFixer = new ColorAdjust();
            colorFixer.setBrightness(1);
            field.setEffect(colorFixer);
        }
    }

    /**
     * Checks for if the enter key is pressed, and attempts to press the submit
     * button if valid
     */
    protected final void onKeyPress(final KeyEvent e) {
        keyPressed(e.getCode());
        if (e.getCode() == KeyCode.ENTER) {
            if (e.getSource() instanceof JFXTextArea) {
                final JFXTextArea ta = (JFXTextArea) e.getSource();
                final String text = ta.getText().trim();
                ta.setText(text);
                final int idx = textAreas.indexOf(ta);
                if (idx + 1 == textAreas.size()) {
                    if (text.isBlank()) {
                        onPassClicked();
                    } else {
                        onSubmitClicked();
                    }
                } else {
                    textAreas.get(idx + 1).requestFocus();
                }
            }

        }
    }

    /**
     * gets each button in the bar, and programs its handler Gets the text and
     * current cursor location from the selected text area and inserts the selected
     * macron character after the cursor. Then reselect the text area and set the
     * cursor to after the inserted character.
     */
    protected final void initMacronButtons() {
        macronBar.getChildren().stream().filter(b -> b instanceof Button).map(b -> (Button) b)
            .forEach(btn -> btn.setOnAction(e -> {
                if (!textAreas.get(0).isEditable())
                    return;
                activeText.insertText(activeText.getCaretPosition(), btn.getText());
                activeText.requestFocus();
                activeText.positionCaret(activeText.getCaretPosition());

                onKeyPress(new KeyEvent(null, null, null, null, false, false, false, false));
            }));
    }

    /**
     * Called when the replay button is clicked, repeats the question hint using the
     * speaker.
     */
    @FXML
    protected final void onReplayClick() {
        speaker.speak(getGameModel().getActiveQuestion().getHint());
    }

    /**
     * Sets the submit button to either pass or submit, depending on what buttonType
     * was passed in
     *
     * @param buttonType The type of button to set the submit button to
     */
    protected final void setSubmitButtonType(final ButtonType buttonType) {
        btnSubmit.getStyleClass().clear();
        btnSubmit.getStyleClass().addAll("material-button");
        switch (buttonType) {
            case PASS:
                btnSubmit.getStyleClass().add("material-button-red");
                btnSubmit.setText("Pass");
                btnSubmit.setOnAction(e -> onPassClicked());
                break;
            case SUBMIT:
                btnSubmit.getStyleClass().add("material-button-green");
                btnSubmit.setText("Submit");
                btnSubmit.setOnAction(e -> onSubmitClicked());
                break;
        }
    }

    /**
     * refreshes the buttons onAction calls, back to the normal functions, as they
     * change when a question is just answered.
     */
    protected final void refreshButtonState() {
        progressButtons.getChildren().subList(1, progressButtons.getChildren().size()).clear();
        setSubmitButtonType(ButtonType.PASS);
    }

    /**
     * The available types of buttons used for the submit button
     */
    protected enum ButtonType {
        PASS, SUBMIT
    }

}
