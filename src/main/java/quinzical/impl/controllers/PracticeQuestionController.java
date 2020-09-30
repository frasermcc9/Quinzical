package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;

public class PracticeQuestionController {

    @Inject
    private SceneHandler sceneHandler;

    @Inject
    private GameModel gameModel;
    
    @FXML
    void onSubmitClicked(ActionEvent actionEvent) {
    }

    @FXML
    void onPassClicked(ActionEvent actionEvent) {
    }

    @FXML
    void onReplyClick(MouseEvent mouseEvent) {
    }

    @FXML
    void onBackClicked(ActionEvent actionEvent) {
        sceneHandler.setActiveScene(GameScene.PRACTICE);
    }
}
