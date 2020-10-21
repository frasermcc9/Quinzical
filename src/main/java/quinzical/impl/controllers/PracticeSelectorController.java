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
import com.google.inject.Injector;
import com.jfoenix.controls.JFXMasonryPane;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import quinzical.impl.constants.GameScene;
import quinzical.impl.controllers.components.CategorySelectorPaneController;
import quinzical.impl.models.structures.FxmlInfo;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CategorySelectorController extends StandardSceneController {

    private final List<String> selectedCategories = new ArrayList<>();

    @Inject
    private SceneHandler sceneHandler;
    @Inject
    private GameModel gameModel;
    @Inject
    private Injector injector;

    @FXML
    private VBox container;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private JFXMasonryPane masonryPane;

    @FXML
    private Label lblSelected;
    @FXML
    private Button btnOk;


    @FXML
    void btnBackPress() {
        sceneHandler.setActiveScene(GameScene.GAME_TYPE_SELECT);
    }

    @FXML
    void btnOKPress() {
        gameModel.generateGameQuestionSetFromCategories(selectedCategories);
        sceneHandler.setActiveScene(GameScene.GAME);
    }

    @Override
    protected void onLoad() {
        btnOk.setDisable(true);
        List<String> categories = gameModel.getCategories()
            .stream()
            .sorted(String::compareToIgnoreCase)
            .collect(Collectors.toList());

        List<String> data = gameModel.getUserData().getAnalytics()
            .getCorrectRatiosOfCategories(categories)
            .stream()
            .map(Double::parseDouble)
            .map(v -> Double.isNaN(v) ? 0 + "%" : Math.round(v * 100) + "%")
            .collect(Collectors.toList());

        List<Parent> children = new ArrayList<>();

        for (int i = 0; i < categories.size(); i++) {
            try {
                FxmlInfo<CategorySelectorPaneController> fxmlInfo = FxmlInfo.loadFXML("components/category-selection"
                    , injector);
                Parent p = fxmlInfo.getParent();
                children.add(p);

                CategorySelectorPaneController controller = fxmlInfo
                    .getController()
                    .setContent(categories.get(i), data.get(i));

                p.setOnMouseEntered(this::hoverCard);
                p.setOnMouseExited(this::hoverOffCard);
                p.setOnMouseClicked((event -> selectCategory(event, controller.getCategory())));


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        masonryPane.getChildren().addAll(children);
    }

    private void selectCategory(MouseEvent e, String category) {
        buttonToggle(e, category, true);
    }

    private void deselectCategory(MouseEvent e, String category) {
        buttonToggle(e, category, false);
    }

    private void buttonToggle(MouseEvent e, String category, boolean added) {
        Node source = (Node) e.getSource();

        if (added) {
            selectedCategories.add(category);
            source.setOnMouseClicked((event -> deselectCategory(event, category)));
            createScaleAnimation(source, 250, 0.9);
            adjustBrightness(source, 150, -0.5);
            source.setOnMouseEntered(null);
            source.setOnMouseExited(null);
        } else {
            selectedCategories.remove(category);
            source.setOnMouseClicked((event -> selectCategory(event, category)));
            createScaleAnimation(source, 150, 1);
            adjustBrightness(source, 150, 0);
            source.setOnMouseEntered(this::hoverCard);
            source.setOnMouseExited(this::hoverOffCard);
        }
        this.lblSelected.setText("Selected: " + selectedCategories.size() + "/5");
        btnOk.setDisable(selectedCategories.size() != 5);
    }

    private void hoverCard(MouseEvent e) {
        Object source = e.getSource();
        if (source instanceof Node) {
            createScaleAnimation((Node) source, 150, 1.1);
        }
    }

    private void hoverOffCard(MouseEvent e) {
        Object source = e.getSource();
        if (source instanceof Node) {
            createScaleAnimation((Node) source, 150, 1);
        }
    }
}

