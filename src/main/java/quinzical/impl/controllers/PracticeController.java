package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;

public class PracticeController {

    @Inject
    private SceneHandler sceneHandler;

    @Inject
    private GameModel gameModel;


    @FXML
    void btnOKPress(ActionEvent actionEvent) {
    }

    @FXML
    void onCategoryChosen(ActionEvent actionEvent) {
    }

    @FXML
    void btnBackPress(ActionEvent actionEvent) {
        sceneHandler.setActiveScene(GameScene.INTRO);
    }
}
