package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;

public class GameTypeSelectController extends StandardSceneController{

    @Inject
    private SceneHandler sceneHandler;
    
    @Inject 
    private GameModel gameModel;
    
    @FXML
    void btnInternationalPress() {
        gameModel.generateInternationalQuestions();
        sceneHandler.setActiveScene(GameScene.INTERNATIONAL_GAME);
    }

    @FXML
    void btnNZPress() {
        gameModel.generateNewGameQuestionSet();
        sceneHandler.setActiveScene(GameScene.GAME);
    }

    @FXML
    void btnBackPress() {
        sceneHandler.setActiveScene(GameScene.INTRO);
    }

    @Override
    protected void onLoad() {
    }
}
