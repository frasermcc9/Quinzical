package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController {

    @Inject
    private SceneHandler sceneHandler;
    @Inject
    private GameModel gameModel;

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Button btnMenu;

    @FXML
    void btnMenuClick(ActionEvent event) {
        sceneHandler.setActiveScene(GameScene.INTRO);
    }

    @FXML
    void initialize() {
        assert btnMenu != null : "fx:id=\"btnMenu\" was not injected: check your FXML file 'game.fxml'.";
        gameModel.generateNewGameQuestionSet();
    }
}
