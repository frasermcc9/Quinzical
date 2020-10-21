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
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.GameModelSaver;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.models.structures.UserData;
import quinzical.interfaces.strategies.objectreader.ObjectReaderStrategyFactory;

import java.io.IOException;


/**
 * Controller class for the intro screen.
 */
public class IntroController extends AbstractSceneController {

    @Inject
    private SceneHandler sceneHandler;
    @Inject
    private GameModelSaver gameModel;
    @Inject
    private ObjectReaderStrategyFactory objectReader;

    @FXML
    private Button btnLoadGame;
    @FXML
    private Label lblfunny;


    /**
     * Sets the active scene to the main game scene where you select categories and questions from.
     */
    @FXML
    void btnLoadGamePress() {
        sceneHandler.setActiveScene(GameScene.GAME);
    }

    /**
     * When the play button is clicked, change the scene to the main game board and fire the refresh board event.
     */
    @FXML
    void btnNewGamePress() {
        sceneHandler.setActiveScene(GameScene.GAME_TYPE_SELECT);
    }

    @FXML
    void btnOptionsPress() {
        sceneHandler.setActiveScene(GameScene.OPTIONS);
    }

    @FXML
    void btnAchievementsClick() {

    }

    @FXML
    void btnStatisticsClick() {
        sceneHandler.setActiveScene(GameScene.STATISTICS);
    }


    @Override
    protected void onLoad() {
        handleLoadGameButton();
        var st = new ScaleTransition(Duration.seconds(0.5));
        st.setNode(lblfunny);
        st.setFromX(0.7);
        st.setFromY(0.7);
        st.setToX(0.85);
        st.setToY(0.85);
        st.setAutoReverse(true);
        st.setCycleCount(Animation.INDEFINITE);
        st.playFromStart();
    }

    /**
     * Sets up the loadGameButton, checking to see if there is save data to fetch and disabling the button if there
     * isn't any yet.
     * <p>
     * Method execution order: Check if there is a game in the current instance going, and load that. Check if there is
     * a save file, then: Check if that save file has a game running. If yes, load that. Otherwise disable the button,
     * and load only total coins and international mode.
     */
    private void handleLoadGameButton() {
        if (gameModel.isGameActive()) {
            btnLoadGame.setDisable(false);
            return;
        }

        UserData saveData;
        try {
            saveData = objectReader.<UserData>createObjectReader().readObject(System.getProperty("user.dir") + "/data" +
                "/save.qdb");

            gameModel.loadSaveData(saveData);
            if (saveData.isGameActive()) {
                btnLoadGame.setOnAction(e -> btnLoadGame.setOnAction(action -> sceneHandler.setActiveScene(GameScene.GAME)));
                btnLoadGame.fire();
            } else {
                btnLoadGame.setDisable(true);
            }

        } catch (IOException | ClassNotFoundException e) {
            //if no game data is found
            btnLoadGame.setDisable(true);
        }
    }


    @FXML
    void btnOnlineClick() {
        sceneHandler.setActiveScene(GameScene.MULTI_INTRO);
    }


}
