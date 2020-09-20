package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import quinzical.impl.constants.GameEvent;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.SceneHandler;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the intro screen.
 */
public class IntroController extends PrimarySceneController {

    @Inject
    private SceneHandler sceneHandler;

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private ImageView imgTitle;
    @FXML
    private Button btnPractice;
    @FXML
    private Button btnPlay;
    @FXML
    private AnchorPane background;
    @FXML
    private ImageView btnSun;
    @FXML
    private ImageView btnNight;

    /**
     * When the play button is clicked, change the scene to the main game board and
     * fire the refresh board event.
     *
     * @param event
     */
    @FXML
    void btnPlayClick(ActionEvent event) {
        sceneHandler.setActiveScene(GameScene.GAME);
        sceneHandler.emit(GameEvent.FULL_BOARD_REFRESH);
    }

    /**
     * When the practice button is clicked, change the scene to the main practice
     * board.
     *
     * @param event
     */
    @FXML
    void btnPracticeClick(ActionEvent event) {
        sceneHandler.setActiveScene(GameScene.PRACTICE);
    }

    /**
     * Fire the dark theme enabled event when the moon button is clicked.
     *
     * @param event
     */
    @FXML
    void btnMoonClicked(MouseEvent event) {
        sceneHandler.emit(GameEvent.DARK_THEME_ENABLED);
    }

    /**
     * Fire the light theme enabled event when the sun button is clicked.
     *
     * @param event
     */
    @FXML
    void btnSunClicked(MouseEvent event) {
        sceneHandler.emit(GameEvent.LIGHT_THEME_ENABLED);
    }

    /**
     * Fired when the FXML is loaded.
     */
    @FXML
    void initialize() {
        assert imgTitle != null : "fx:id=\"imgTitle\" was not injected: check your FXML file 'intro.fxml'.";
        assert btnPractice != null : "fx:id=\"btnPractice\" was not injected: check your FXML file 'intro.fxml'.";
        assert btnPlay != null : "fx:id=\"btnPlay\" was not injected: check your FXML file 'intro.fxml'.";

        listen();
    }

    /**
     * Listen to events that this scene needs to react to.
     */
    private void listen() {
        // Change theme to light mode when light theme enabled event is fired
        sceneHandler.on(GameEvent.LIGHT_THEME_ENABLED, () -> setTheme(Theme.LIGHT));
        // Change theme to dark mode when dark theme enabled event is fired
        sceneHandler.on(GameEvent.DARK_THEME_ENABLED, () -> setTheme(Theme.DARK));
    }

    /**
     * Gets the background anchor pane. Overrides abstract from superclass.
     */
    @Override
    protected AnchorPane getBackground() {
        return this.background;
    }
}
