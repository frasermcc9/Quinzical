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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import quinzical.impl.constants.GameScene;
import quinzical.impl.util.questionparser.Question;
import quinzical.interfaces.models.PracticeModel;
import quinzical.interfaces.models.SceneHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for the practice menu scene
 */
public class PracticeController extends StandardSceneController {
    
    private String selectedCategory;
    
    private Button selectedButton;
    
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

    @FXML
    private Button btnOk;
    
    @FXML
    private ScrollPane scrollPane;
    
    /**
     * Fired when the ok button is pressed, gets a random question from the currently selected category and then
     * switches to the practice question scene.
     */
    @FXML
    void btnOKPress(ActionEvent actionEvent) {
        if (selectedCategory != null) {
            Question question = gameModel.getRandomQuestion(selectedCategory);
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
        //populateList();

        btnOk.setDisable(true);

        List<String> categories = gameModel.getCategories()
            .stream()
            .sorted(String::compareToIgnoreCase)
            .collect(Collectors.toList());

        int size = categories.size();

        GridPane grid = new GridPane();

        for (int rowIndex = 0; rowIndex < size / 2 + 1; rowIndex++) {
            RowConstraints rc = new RowConstraints();
            rc.setVgrow(Priority.SOMETIMES);
            rc.setFillHeight(true);
            grid.getRowConstraints().add(rc);
        }
        for (int colIndex = 0; colIndex < 2; colIndex++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.SOMETIMES);
            cc.setFillWidth(true);
            grid.getColumnConstraints().add(cc);
        }
        for (int i = 0; i < size; i++) {
            Button btn = new Button();
            btn.setText(categories.get(i));
            btn.setMinSize(scrollPane.getWidth() / 2, 70);
            btn.setPrefSize(scrollPane.getWidth() / 2, 70);
            btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            btn.getStyleClass().add("button-unselected");
            
            btn.setOnAction(this::selectCategory);

            int colIdx = i % 2;
            grid.add(btn, colIdx, i / 2);
        }

        scrollPane.setContent(grid);
         
    }

    private void selectCategory(ActionEvent e) {
        buttonToggle(e, true);
    }

    private void deselectCategory(ActionEvent e) {
        buttonToggle(e, false);
    }

    private void buttonToggle(ActionEvent e, boolean added) {
        Button source = (Button) e.getSource();
        String category = source.getText();

        source.getStyleClass().clear();
        if (added) {
            //selectedCategories.add(category);
            selectedCategory=category;
            source.getStyleClass().add("button-selected");
            source.setOnAction(this::deselectCategory);
            
            if(selectedButton != null){
                selectedButton.getStyleClass().clear();
                selectedButton.getStyleClass().add("button-unselected");
                selectedButton.setOnAction(this::selectCategory);
            }
            
            selectedButton = source;
            
        } else {
            //selectedCategories.remove(category);
            selectedCategory=null;
            source.getStyleClass().add("button-unselected");
            source.setOnAction(this::selectCategory);
        }
        //this.lblSelected.setText("Selected: " + selectedCategories.size() + "/5");
        //btnOk.setDisable(selectedCategories.size() != 5);
        btnOk.setDisable(selectedCategory==null);
    }
}
