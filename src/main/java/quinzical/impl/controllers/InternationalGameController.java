package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import quinzical.impl.constants.GameScene;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InternationalGameController extends StandardSceneController{
    
    private final List<Button> buttons = new ArrayList<>();
    @Inject
    private GameModel gameModel;
    @Inject
    private SceneHandler sceneHandler;

    @FXML
    private Button btnMenu;

    @FXML
    private Label labelEarningsVal;
    @FXML
    private Pane paneHeader;

    @FXML
    private Label txtHeading;

    @Override
    protected void onLoad() {
        for (Node b : paneHeader.getChildren()) {
            if (b instanceof Button) {
                buttons.add((Button) b);
            }
        }
    }

    @Override
    protected void refresh() {
        this.refreshBoard();
        this.refreshValue();
    }

    /**
     * Update the earnings label. Fired when the model's earnings value updates.
     */
    private void refreshValue() {
        labelEarningsVal.setText("$" + gameModel.getValue());
    }

    /**
     * Refreshes the question board. Does this by setting the style of all the buttons and adding the needed handlers.
     * Fired when the state of some question updates.
     */
    private void refreshBoard() {

        btnMenu.setOnAction(event -> sceneHandler.setActiveScene(GameScene.INTRO));

        setOverlay(InternationalGameController.Overlay.CATEGORY);

        Map<String, List<GameQuestion>> model = gameModel.getBoardQuestions();
        List<String> keys = new ArrayList<>(model.keySet());

        for (int i = 0; i < buttons.size(); i++) {
            Button btn = buttons.get(i);

            btn.getStyleClass().clear();
            btn.getStyleClass().add("button");
            btn.getStyleClass().add("transparent");

            String category = keys.get(i);
            int answeredCount = model.get(category).stream().reduce(0, (sub, el) -> sub + (el.isAnswered() ? 1 : 0),
                Integer::sum);

            btn.setText(category + " - " + answeredCount + "/5");
            btn.setDisable(false);
            btn.setOnAction((event) -> loadQuestions(model.get(category)));
        }
    }

    /**
     * Called when a category is selected. Sets the buttons and handlers to change to the question scene.
     */
    private void loadQuestions(List<GameQuestion> questions) {

        btnMenu.setOnAction(event -> refreshBoard());

        setOverlay(InternationalGameController.Overlay.QUESTION);

        for (int i = 0; i < buttons.size(); i++) {
            Button btn = buttons.get(i);
            GameQuestion q = questions.get(i);

            if (q.isAnswered()) {
                if (q.isCorrect()) {
                    btn.getStyleClass().clear();
                    btn.getStyleClass().add("rightButton");
                } else {
                    btn.getStyleClass().clear();
                    btn.getStyleClass().add("wrongButton");
                }
                btn.getStyleClass().add("transparent");
            }

            btn.setText(q.getValue() + "");
            btn.setDisable(!q.isAnswerable());
            btn.setOnAction((event) -> activateQuestion(q));
        }
    }

    /**
     * Sets the inputted question as the active question through the game model.
     *
     * @param gameQuestion the question to be set as the active one.
     */
    private void activateQuestion(GameQuestion gameQuestion) {
        gameModel.activateQuestion(gameQuestion);
        sceneHandler.setActiveScene(GameScene.GAME_QUESTION);
    }

    /**
     * Also fired when any category button is pressed (or the back button). Sets the top text image.
     *
     * @param o the overlay to be set (what top text should be active)
     */
    private void setOverlay(InternationalGameController.Overlay o) {
        switch (o) {
            case CATEGORY:
                txtHeading.setText("Select a Category");
                return;
            case QUESTION:
                txtHeading.setText("Select a Question");
                return;
            default:
                throw new IllegalArgumentException("Invalid overlay passed in GameController");
        }
    }


    /**
     * The two types of overlay
     */
    private enum Overlay {
        CATEGORY, QUESTION
    }
}
