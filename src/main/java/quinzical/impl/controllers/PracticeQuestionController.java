package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;

public class PracticeQuestionController {

    @Inject
    private SceneHandler sceneHandler;

    @Inject
    private GameModel gameModel;
    
    public void onSubmitClicked(ActionEvent actionEvent) {
    }

    public void onPassClicked(ActionEvent actionEvent) {
    }

    public void onReplyClick(MouseEvent mouseEvent) {
    }
}
