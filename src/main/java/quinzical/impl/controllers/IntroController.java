package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;


/**
 * Controller class for the intro screen.
 */
public class IntroController extends PrimarySceneController {

    @Inject
    private SceneHandler sceneHandler;

    @Inject
    private GameModel gameModel;

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
        //not implemented
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
        if (gameModel.getBoardQuestions() == null) {
            btnLoadGame.setDisable(true);
        }
        listen();
    }

    /**
     * Listen to events that this scene needs to react to.
     */
    private void listen() {
        sceneHandler.onBackgroundChange(img -> this.imgBackground.setImage(img));
        gameModel.onQuestionsUpdate(()->{
            if(gameModel.getBoardQuestions()!=null){
                btnLoadGame.setDisable(false);
            }
        });
    }
}
