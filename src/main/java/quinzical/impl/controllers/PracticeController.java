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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import quinzical.impl.constants.GameScene;
import quinzical.impl.util.questionparser.Question;
import quinzical.interfaces.models.PracticeModel;
import quinzical.interfaces.models.SceneHandler;

import java.util.List;

/**
 * Controller for the practice menu scene
 */
public class PracticeController extends StandardSceneController {

    @Inject
    private SceneHandler sceneHandler;

    @Inject
    private PracticeModel gameModel;

    @FXML
    private ComboBox<String> comboCategories;

    @FXML
    private ListView<String> listCategories;

    @FXML
    private ImageView imgBackground;

    /**
     * Fired when the ok button is pressed, gets a random question from the currently selected category and then
     * switches to the practice question scene.
     */
    @FXML
    void btnOKPress(ActionEvent actionEvent) {
        if (listCategories.getSelectionModel().getSelectedItem() != null) {
            Question question = gameModel.getRandomQuestion(listCategories.getSelectionModel().getSelectedItem());
            gameModel.activateQuestion(question);
            sceneHandler.setActiveScene(GameScene.PRACTICE_QUESTION);
        }
    }

    /**
     * Sets the scene to the intro scene when the back button is pressed.
     */
    @FXML
    void btnBackPress(ActionEvent actionEvent) {
        sceneHandler.setActiveScene(GameScene.INTRO);
    }
    

    @Override
    protected void onLoad() {
        populateList();
    }
    

    private void populateList() {
        List<String> categories = gameModel.getCategories();
        listCategories.getItems().clear();

        ObservableList<String> list = javafx.collections.FXCollections.observableList(categories);

        listCategories.setItems(list);
    }
}
