package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import quinzical.Entry;
import quinzical.impl.constants.GameScene;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.strategies.boardloader.BoardLoaderStrategyFactory;

import java.net.URL;
import java.util.*;

/**
 * Controller class for the Game view. This is the view that contains the question board.
 */
public class GameController extends PrimarySceneController {

    private final List<Button> buttons = new ArrayList<>();
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
    private ImageView imgBackground;
    @FXML
    private ImageView imgOverlay;

    /**
     * Called when the FXML is finished loading.
     */
    @FXML
    void initialize() {
        assert btnMenu != null : "fx:id=\"btnMenu\" was not injected: check your FXML file 'game.fxml'.";

        for (Node b : paneHeader.getChildren()) {
            if (b instanceof Button) {
                buttons.add((Button) b);
            }
        }

        listen();
    }

    /**
     * Listen for events from the SceneHandler.
     */
    private void listen() {
        // Listen for theme changes.
        sceneHandler.onBackgroundChange(img -> this.imgBackground.setImage(img));
        gameModel.onQuestionsUpdate(this::refreshBoard);
    }

    private void refreshBoard() {

        btnMenu.setOnAction(event -> sceneHandler.setActiveScene(GameScene.INTRO));

        setOverlay(Overlay.CATEGORY);

        Map<String, List<GameQuestion>> model = gameModel.getBoardQuestions();
        List<String> keys = new ArrayList<>(model.keySet());

        for (int i = 0; i < buttons.size(); i++) {
            Button btn = buttons.get(i);

            String category = keys.get(i);
            int answered = model.get(category).stream().reduce(0, (sub, el) -> sub + (el.isAnswered() ? 1 : 0),
                Integer::sum);

            btn.setText(category + " - " + answered + "/5");
            btn.setDisable(false);
            btn.setOnAction((event) -> loadQuestions(model.get(category)));
        }
    }

    private void loadQuestions(List<GameQuestion> questions) {

        btnMenu.setOnAction(event -> refreshBoard());

        setOverlay(Overlay.QUESTION);

        for (int i = 0; i < buttons.size(); i++) {
            Button btn = buttons.get(i);
            GameQuestion q = questions.get(i);

            btn.setText(q.getValue() + "");
            btn.setDisable(!q.isAnswerable());
            btn.setOnAction((event) -> activateQuestion(q));
        }
    }

    private void activateQuestion(GameQuestion gameQuestion) {
        gameModel.activateQuestion(gameQuestion);
        sceneHandler.setActiveScene(GameScene.GAME_QUESTION);
    }

    private void setOverlay(Overlay o) {
        String img = "";
        switch (o) {
            case CATEGORY:
                img = "game";
                break;
            case QUESTION:
                img = "game2";
                break;

        }
        imgOverlay.setImage(new Image(Objects.requireNonNull(Entry.class.getClassLoader().getResourceAsStream("images" +
            "/overlays/" + img + ".png"))));
    }

    private enum Overlay {
        CATEGORY, QUESTION
    }

}
