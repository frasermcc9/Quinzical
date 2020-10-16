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
import javafx.scene.control.Button;
import quinzical.impl.constants.GameScene;
import quinzical.impl.util.questionparser.Serializer;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.GameModelSaver;
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.models.structures.UserData;
import quinzical.interfaces.strategies.objectreader.ObjectReaderStrategyFactory;

import java.io.IOException;


/**
 * Controller class for the intro screen.
 */
public class IntroController extends StandardSceneController {

    @Inject
    private SceneHandler sceneHandler;

    @Inject
    private GameModelSaver gameModel;

    @Inject
    private ObjectReaderStrategyFactory objectReader;

    @Inject
    private QuestionCollection questionCollection;

    @FXML
    private Button btnLoadGame;


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

    /**
     * When the practice button is clicked, change the scene to the main practice board.
     */
    @FXML
    void btnPracticeModePress() {
        sceneHandler.setActiveScene(GameScene.PRACTICE);
    }

    /**
     * When the Load New Question Set button is pressed, run the question file loading method.
     */
    @FXML
    void btnLoadSetPress() {
        Serializer.main(null);
        questionCollection.regenerateQuestionsFromDisk();
    }
    
    @FXML
    void btnStatisticsClick(){
        sceneHandler.setActiveScene(GameScene.STATISTICS);
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


    @FXML
    void btnOnlineClick() {
        sceneHandler.setActiveScene(GameScene.MULTI_INTRO);
    }


}
