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

import java.net.URL;
import java.util.*;

/**
 * Controller class for the Game view. This is the view that contains the question board.
 */
public class GameController {

    private final List<Button> buttons = new ArrayList<>();
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
        // Listen for theme changes
        sceneHandler.onBackgroundChange(img -> this.imgBackground.setImage(img));
        // Listen for when the board is updated
        gameModel.onQuestionBoardUpdate(this::refreshBoard);
        // Listen for when the earnings of the user is updated
        gameModel.onValueChange(this::refreshValue);
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

        setOverlay(Overlay.CATEGORY);

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

    // Called when a category is selected. Sets the buttons and handlers to change to the question scene.
    private void loadQuestions(List<GameQuestion> questions) {

        btnMenu.setOnAction(event -> refreshBoard());

        setOverlay(Overlay.QUESTION);

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

    // Handler for question buttons.
    private void activateQuestion(GameQuestion gameQuestion) {
        gameModel.activateQuestion(gameQuestion);
        sceneHandler.setActiveScene(GameScene.GAME_QUESTION);
    }

    // Also fired when any category button is pressed (or the back button). Sets the top text image.
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

    // The two types of overlay.
    private enum Overlay {
        CATEGORY, QUESTION
    }

}
