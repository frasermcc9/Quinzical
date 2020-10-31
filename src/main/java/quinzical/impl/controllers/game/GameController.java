// Copyright 2020 Fraser McCallum and Braden Palmer
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//  
//     http://www.apache.org/licenses/LICENSE-2.0
//  
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package quinzical.impl.controllers.game;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import quinzical.impl.constants.GameScene;
import quinzical.impl.controllers.AbstractSceneController;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller class for the Game view. This is the view that contains the question board.
 */
public class GameController extends AbstractSceneController {

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
    protected final void onLoad() {
        paneHeader.getChildren().stream().filter(b -> b instanceof Button).map(b -> (Button) b).forEach(buttons::add);
    }

    @Override
    protected final void refresh() {
        this.refreshBoard();
        this.refreshValue();
    }

    /**
     * Update the earnings label. Fired when the model's earnings value updates.
     */
    private void refreshValue() {
        labelEarningsVal.setText("$" + gameModel.getEarnings());
    }

    /**
     * Refreshes the question board. Does this by setting the style of all the buttons and adding the needed handlers.
     * Fired when the state of some question updates.
     */ 
    private void refreshBoard() {

        btnMenu.setOnAction(event -> sceneHandler.setActiveScene(GameScene.INTRO));

        setOverlay(Overlay.CATEGORY);

        final Map<String, List<GameQuestion>> model = gameModel.getBoardQuestions();
        final List<String> keys = new ArrayList<>(model.keySet());

        for (int i = 0; i < buttons.size(); i++) {
            final Button btn = buttons.get(i);

            btn.getStyleClass().clear();
            btn.getStyleClass().add("material-button");

            final String category = keys.get(i);
            final int answeredCount = model.get(category).stream().reduce(0, (sub, el) -> sub + (el.isAnswered() ? 1 : 0),
                Integer::sum);

            btn.setText(category + " - " + answeredCount + "/5");
            btn.setDisable(false);
            btn.setOnAction((event) -> loadQuestions(model.get(category)));
        }
    }

    /**
     * Called when a category is selected. Sets the buttons and handlers to change to the question scene.
     */
    private void loadQuestions(final List<GameQuestion> questions) {

        btnMenu.setOnAction(event -> refreshBoard());

        setOverlay(Overlay.QUESTION);

        for (int i = 0; i < buttons.size(); i++) {
            final Button btn = buttons.get(i);
            final GameQuestion q = questions.get(i);

            if (q.isAnswered()) {
                btn.getStyleClass().clear();
                btn.getStyleClass().add("material-button");
                if (q.isCorrect()) {
                    btn.getStyleClass().add("material-button-green");
                } else {
                    btn.getStyleClass().add("material-button-red");
                }
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
    private void activateQuestion(final GameQuestion gameQuestion) {
        gameModel.activateQuestion(gameQuestion);
        new Thread(() -> sceneHandler.setActiveScene(GameScene.GAME_QUESTION)).start();
    }

    /**
     * Also fired when any category button is pressed (or the back button). Sets the top text image.
     *
     * @param o the overlay to be set (what top text should be active)
     */
    private void setOverlay(final Overlay o) {
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
