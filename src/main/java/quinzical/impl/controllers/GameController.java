package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import quinzical.impl.constants.GameEvent;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController extends PrimarySceneController {

    @Inject
    private GameModel gameModel;
    @Inject
    private SceneHandler sceneHandler;
    
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private AnchorPane background;
    @FXML
    private Button btnMenu;
    @FXML
    private Label labelEarnings;
    @FXML
    private Label labelEarningsVal;

    @FXML
    void btnMenuClick(ActionEvent event) {
        sceneHandler.setActiveScene(GameScene.INTRO);
    }

    @FXML
    void initialize() {
        assert btnMenu != null : "fx:id=\"btnMenu\" was not injected: check your FXML file 'game.fxml'.";
        
        sceneHandler.on(GameEvent.LIGHT_THEME_ENABLED, () -> setTheme(Theme.LIGHT));
        sceneHandler.on(GameEvent.DARK_THEME_ENABLED, () -> setTheme(Theme.DARK));
        
        gameModel.generateNewGameQuestionSet();
    }

    @Override
    protected AnchorPane getBackground() {
        return this.background;
    }

    @Override
    public void setLabelTextColour(String colourHex) {
        labelEarnings.setTextFill(Color.web(colourHex));
        labelEarningsVal.setTextFill(Color.web(colourHex));
    }
}
