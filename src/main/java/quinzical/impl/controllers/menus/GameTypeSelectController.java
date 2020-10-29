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
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import quinzical.impl.constants.GameScene;
import quinzical.impl.controllers.AbstractSceneController;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;

public class GameTypeSelectController extends AbstractSceneController {

    private static final Runnable[] buttonActions = new Runnable[3];

    @Inject
    private SceneHandler sceneHandler;
    @Inject
    private GameModel gameModel;

    @FXML
    private JFXSpinner progressIndicator;
    @FXML
    private Label lblProgress;
    @FXML
    private Button btnInternational;
    @FXML
    private HBox imageContainer;
    @FXML
    private Button btnPlay;
    @FXML
    private Label lblInternational;

    @FXML
    void btnBackPress() {
        sceneHandler.setActiveScene(GameScene.INTRO);
    }

    private void setProgressVisible(boolean visible) {
        progressIndicator.setVisible(visible);
        lblProgress.setVisible(visible);
    }

    @Override
    protected void onLoad() {
        setProgressVisible(false);
        btnPlay.setDisable(true);
        setFunctions();

        Glow glow = new Glow(100);

        final int startPoint = 0;
        final int endPoint;

        if (!gameModel.isInternationalUnlocked()) {
            endPoint = 2;
            StackPane international = (StackPane) imageContainer.getChildren().get(2);
            createScaleAnimation(international, 100, 0.9);
            adjustBrightness(international.getChildren().get(0), 100, -0.8);
        } else {
            lblInternational.setText("International Questions");
            endPoint = 3;
        }

        for (int i = startPoint; i < endPoint; i++) {
            final int idx = i;
            Node image = imageContainer.getChildren().get(idx);
            image.setOnMouseEntered(e -> image.setEffect(glow));
            image.setOnMouseExited(e -> image.setEffect(null));
            image.setOnMouseClicked(e -> disableImagesExcept(idx, startPoint, endPoint));
        }
    }

    private void disableImagesExcept(int i, int start, int end) {
        for (int j = start; j < end; j++) {
            Node image = imageContainer.getChildren().get(j);
            if (j == i) {
                createScaleAnimation(image, 400, 1.1);
                btnPlay.setDisable(false);
                btnPlay.setOnAction(a -> new Thread(buttonActions[i]).start());
            } else {
                createScaleAnimation(image, 400, 0.9);
            }
        }
    }

    private void setFunctions() {
        buttonActions[0] = () -> sceneHandler.setActiveScene(GameScene.PRACTICE);
        buttonActions[1] = () -> sceneHandler.setActiveScene(GameScene.CATEGORY_SELECTOR);
        buttonActions[2] = this::loadInternational;
    }

    private void loadInternational() {
        setProgressVisible(true);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                gameModel.generateInternationalQuestions();
                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> sceneHandler.setActiveScene(GameScene.GAME));
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
