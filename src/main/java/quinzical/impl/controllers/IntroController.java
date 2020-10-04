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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import quinzical.impl.constants.GameScene;
import quinzical.impl.models.structures.SaveData;
import quinzical.impl.util.questionparser.Serializer;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.strategies.objectreader.ObjectReaderStrategyFactory;

import java.io.IOException;


/**
 * Controller class for the intro screen.
 */
public class IntroController {

    @Inject
    private SceneHandler sceneHandler;

    @Inject
    private GameModel gameModel;

    @Inject
    private ObjectReaderStrategyFactory objectReader;

    @Inject
    private QuestionCollection questionCollection;

    @FXML
    private AnchorPane background;

    @FXML
    private Button btnLoadGame;

    @FXML
    private ImageView imgBackground;

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
        gameModel.generateNewGameQuestionSet();
        sceneHandler.setActiveScene(GameScene.GAME);
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


    /**
     * Fired when the FXML is loaded.
     */
    @FXML
    void initialize() {
        handleLoadGameButton();
        listen();
    }

    /**
     * Sets up the loadGameButton, checking to see if there is save data to fetch
     * and disabling the button if there isn't any yet.
     */
    private void handleLoadGameButton() {
        SaveData saveData = null;
        try {
            saveData = objectReader.<SaveData>createObjectReader().readObject(System.getProperty("user.dir") + "/data" +
                "/save.qdb");
        } catch (IOException | ClassNotFoundException e) {
            //dont really care if file isn't found
        }

        // There is a save file
        if (saveData != null && saveData.getQuestionData() != null) {
            SaveData finalSaveData = saveData;
            btnLoadGame.setOnAction(e -> {
                gameModel.loadSaveData(finalSaveData);
                btnLoadGame.setOnAction(action -> sceneHandler.setActiveScene(GameScene.GAME));
                btnLoadGame.fire();
            });
        }
        // There is no save
        else {
            btnLoadGame.setDisable(true);
        }
    }


    /**
     * Listen to events that this scene needs to react to.
     */
    private void listen() {
        // Listen for theme changes
        sceneHandler.onBackgroundChange(img -> this.imgBackground.setImage(img));

        // If the QuestionBoard updates, then check if its not null. If it's not, then we can enable the load game btn.
        gameModel.onQuestionBoardUpdate(() -> {
            if (gameModel.getBoardQuestions() != null) {
                btnLoadGame.setDisable(false);
            }
        });
    }

}
