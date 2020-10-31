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

package quinzical.impl.controllers.menus;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import quinzical.impl.constants.GameScene;
import quinzical.impl.controllers.AbstractSceneController;
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

    /**
     * Sets the active scene to the main game scene where you select categories and questions from.
     */
    @FXML
    void btnLoadGamePress() {
        new Thread(() -> sceneHandler.setActiveScene(GameScene.GAME)).start();
    }

    /**
     * When the play button is clicked, change the scene to the main game board and fire the refresh board event.
     */
    @FXML
    void btnNewGamePress() {
        new Thread(() -> sceneHandler.setActiveScene(GameScene.GAME_TYPE_SELECT)).start();
    }

    /**
     * When the options button is clicked, change the scene to the options screen
     */
    @FXML
    void btnOptionsPress() {
        new Thread(() -> sceneHandler.setActiveScene(GameScene.OPTIONS)).start();
    }

    /**
     * Sets the current scene to the store page
     */
    @FXML
    void btnAchievementsClick() {
        new Thread(() -> sceneHandler.setActiveScene(GameScene.STORE)).start();

    }

    /**
     * Sets the current scene to the statistics view
     */
    @FXML
    void btnStatisticsClick() {
        new Thread(() -> sceneHandler.setActiveScene(GameScene.STATISTICS)).start();
    }

    /**
     * Sets the current scene to the online view
     */
    @FXML
    void btnOnlineClick() {
        new Thread(() -> sceneHandler.setActiveScene(GameScene.MULTI_INTRO)).start();
    }

    @Override
    protected void onLoad() {
        handleLoadGameButton();
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
}
