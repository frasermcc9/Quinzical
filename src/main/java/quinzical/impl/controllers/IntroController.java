package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import quinzical.impl.constants.GameScene;
import quinzical.impl.models.structures.SaveData;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.strategies.objectreader.ObjectReaderStrategyFactory;

import java.io.IOException;


/**
 * Controller class for the intro screen.
 */
public class IntroController extends PrimarySceneController {

    @Inject
    private SceneHandler sceneHandler;

    @Inject
    private GameModel gameModel;

    @Inject
    private ObjectReaderStrategyFactory objectReader;

    @FXML
    private AnchorPane background;

    @FXML
    private Button btnLoadGame;

    @FXML
    private ImageView imgBackground;

    @FXML
    void btnLoadGamePress() {
        sceneHandler.setActiveScene(GameScene.GAME);
    }

    /**
     * When the play button is clicked, change the scene to the main game board and fire the refresh board event.
     */
    @FXML
    void btnNewGamePress() {
        gameModel.generateNewGameQuestionSet();
        sceneHandler.setActiveScene(GameScene.GAME);
    }

    @FXML
    void btnOptionsPress() {
        sceneHandler.setActiveScene(GameScene.OPTIONS);
    }

    /**
     * When the practice button is clicked, change the scene to the main practice board.
     */
    @FXML
    void btnPracticeModePress() {
        sceneHandler.setActiveScene(GameScene.PRACTICE);
    }


    /**
     * Fired when the FXML is loaded.
     */
    @FXML
    void initialize() {
        SaveData saveData = null;
        try {
            saveData = objectReader.<SaveData>createObjectReader().readObject(System.getProperty("user.dir") + "/data" +
                "/save.qdb");
        } catch (IOException | ClassNotFoundException e) {
            //dont really care if file isn't found
        }

        if (gameModel.getBoardQuestions() == null && saveData == null) {
            btnLoadGame.setDisable(true);
        } else if (saveData != null) {
            SaveData finalSaveData = saveData;
            btnLoadGame.setOnAction(e -> {
                gameModel.loadSaveData(finalSaveData);
                sceneHandler.setActiveScene(GameScene.GAME);
            });
        } else {
            btnLoadGame.setOnAction(e -> sceneHandler.setActiveScene(GameScene.GAME));
        }

        listen();
    }

    /**
     * Listen to events that this scene needs to react to.
     */
    private void listen() {
        sceneHandler.onBackgroundChange(img -> this.imgBackground.setImage(img));
        gameModel.onQuestionsUpdate(() -> {
            if (gameModel.getBoardQuestions() != null) {
                btnLoadGame.setDisable(false);
            }
        });
    }
}
