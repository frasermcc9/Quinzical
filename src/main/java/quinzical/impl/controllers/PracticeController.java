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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import quinzical.impl.constants.GameScene;
import quinzical.impl.util.questionparser.Question;
import quinzical.interfaces.models.PracticeModel;
import quinzical.interfaces.models.SceneHandler;

import java.util.List;

public class PracticeController {

    @Inject
    private SceneHandler sceneHandler;

    @Inject
    private PracticeModel gameModel;

    @FXML
    private ComboBox<String> comboCategories;

    @FXML
    private ImageView imgBackground;

    @FXML
    void btnOKPress(ActionEvent actionEvent) {
        if (comboCategories.getValue() != null) {
            Question question = gameModel.getRandomQuestion(comboCategories.getValue());
            gameModel.activateQuestion(question);
            sceneHandler.setActiveScene(GameScene.PRACTICE_QUESTION);
        }
    }

    @FXML
    void btnBackPress(ActionEvent actionEvent) {
        sceneHandler.setActiveScene(GameScene.INTRO);
    }

    @FXML
    void initialize() {

        List<String> categories = gameModel.getCategories();

        comboCategories.getItems().addAll(categories);
        comboCategories.setValue(categories.get(0));

        listen();

    }

    private void listen() {
        sceneHandler.onBackgroundChange(img -> this.imgBackground.setImage(img));
    }
}
