package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;

public class GameTypeSelectController extends StandardSceneController {

    @Inject
    private SceneHandler sceneHandler;

    @Inject
    private GameModel gameModel;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Label lblProgress;

    @FXML
    void btnInternationalPress() {
        setProgressVisible(true);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                gameModel.generateInternationalQuestions();
                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> sceneHandler.setActiveScene(GameScene.INTERNATIONAL_GAME));
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
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

    private void setProgressVisible(boolean visible) {
        progressIndicator.setVisible(visible);
        lblProgress.setVisible(visible);
    }

    @Override
    protected void onLoad() {
        setProgressVisible(false);
    }
}
