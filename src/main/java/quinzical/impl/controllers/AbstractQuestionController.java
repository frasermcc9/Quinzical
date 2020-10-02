package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.models.structures.Speaker;
import quinzical.interfaces.strategies.questionverifier.QuestionVerifierFactory;

import java.util.List;

public abstract class AbstractQuestionClass {

    @Inject
    protected SceneHandler sceneHandler;
    
    @Inject
    protected Speaker speaker;
    
    @Inject
    protected QuestionVerifierFactory questionVerifierFactory;
    
    @FXML
    protected ImageView imgBackground;

    @FXML
    private ButtonBar macronBar;
    
    @FXML
    protected Button btnSubmit;
    
    protected List<TextArea> textAreas;
    
    protected TextArea activeText = null;
    


    protected void onEnterPressed(KeyEvent e) {
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
