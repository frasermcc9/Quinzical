package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import quinzical.impl.constants.GameEvent;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.strategies.boardloader.BoardLoaderStrategyFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the Game view. This is the view that contains the
 * question board.
 */
public class GameController extends PrimarySceneController {

    @Inject
    private GameModel gameModel;
    @Inject
    private SceneHandler sceneHandler;
    @Inject
    private BoardLoaderStrategyFactory boardLoaderStrategyFactory;

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
    private Pane paneHeader;
    @FXML
    private Pane paneContent;

    /**
     * Fires when the menu button is clicked. Changes the active scene back to the
     * intro scene.
     *
     * @param event
     */
    @FXML
    void btnMenuClick(ActionEvent event) {
        sceneHandler.setActiveScene(GameScene.INTRO);
    }

    /**
     * Called when the FXML is finished loading.
     */
    @FXML
    void initialize() {
        assert btnMenu != null : "fx:id=\"btnMenu\" was not injected: check your FXML file 'game.fxml'.";

        listen();
    }

    /**
     * Listen for events from the SceneHandler.
     */
    private void listen() {
        // Listen for theme changes.
        sceneHandler.on(GameEvent.LIGHT_THEME_ENABLED, () -> setTheme(Theme.LIGHT));
        sceneHandler.on(GameEvent.DARK_THEME_ENABLED, () -> setTheme(Theme.DARK));

        // Listen for when the board needs to be fully refreshed
        sceneHandler.on(GameEvent.FULL_BOARD_REFRESH, () -> {
            // Regenerate questions
            gameModel.generateNewGameQuestionSet();
            // Creates the layout of all the question buttons and headers.
            boardLoaderStrategyFactory.createStrategy().injectComponents(paneHeader, paneContent).loadBoard();
        });
    }

    /**
     * Implements abstract method. Returns the scene background for theme
     * changing.
     */
    @Override
    protected AnchorPane getBackground() {
        return this.background;
    }

    /**
     * Overrides virtual method (optional hook).
     */
    @Override
    public void setLabelTextColour(String colourHex) {
        labelEarnings.setTextFill(Color.web(colourHex));
        labelEarningsVal.setTextFill(Color.web(colourHex));
    }
}
