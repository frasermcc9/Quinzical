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

package quinzical.impl.controllers.game;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.jfoenix.controls.JFXMasonryPane;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import quinzical.impl.constants.GameScene;
import quinzical.impl.controllers.AbstractSceneController;
import quinzical.impl.controllers.components.TileController;
import quinzical.impl.models.structures.FxmlInfo;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractSelectorController extends AbstractSceneController {

    @Inject
    protected Injector injector;
    @Inject
    protected SceneHandler sceneHandler;

    @FXML
    protected JFXMasonryPane masonryPane;
    @FXML
    protected Label lblSelected;
    @FXML
    protected Button btnOk;

    protected abstract GameModel getModel();

    protected abstract List<String> getSelectedCategories();

    protected abstract void modifyCounterLabel();
    
    protected abstract void generateQuestionsAndProgress();

    protected void buttonToggle(MouseEvent e, String category, boolean added) {
        Node source = (Node) e.getSource();

        if (added) {
            getSelectedCategories().add(category);
            source.setOnMouseClicked((event -> deselectCategory(event, category)));
            createScaleAnimation(source, 250, 0.9);
            adjustBrightness(source, 150, -0.5);
            source.setOnMouseEntered(null);
            source.setOnMouseExited(null);
        } else {
            getSelectedCategories().remove(category);
            source.setOnMouseClicked((event -> selectCategory(event, category)));
            createScaleAnimation(source, 150, 1);
            adjustBrightness(source, 150, 0);
            source.setOnMouseEntered(this::hoverCard);
            source.setOnMouseExited(this::hoverOffCard);
        }
        modifyCounterLabel();
    }
    
    @Override
    protected void onLoad() {
        btnOk.setDisable(true);
        List<String> categories = getModel().getCategories()
            .stream()
            .sorted(String::compareToIgnoreCase)
            .collect(Collectors.toList());

        List<String> data = getModel().getUserData().getAnalytics()
            .getCorrectRatiosOfCategories(categories)
            .stream()
            .map(Double::parseDouble)
            .map(v -> Double.isNaN(v) ? 0 + "%" : Math.round(v * 100) + "%")
            .collect(Collectors.toList());

        List<Parent> children = new ArrayList<>();

        for (int i = 0; i < categories.size(); i++) {
            try {
                FxmlInfo<TileController> fxmlInfo = FxmlInfo.loadFXML("components/category-selection"
                    , injector);
                Parent p = fxmlInfo.getParent();
                children.add(p);

                TileController controller = fxmlInfo
                    .getController()
                    .setContent(categories.get(i), data.get(i));

                p.setOnMouseEntered(this::hoverCard);
                p.setOnMouseExited(this::hoverOffCard);
                p.setOnMouseClicked((event -> selectCategory(event, controller.getHeader())));


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        masonryPane.getChildren().addAll(children);
    }

    protected void selectCategory(MouseEvent e, String category) {
        buttonToggle(e, category, true);
    }


    protected void deselectCategory(MouseEvent e, String category) {
        buttonToggle(e, category, false);
    }

    protected void hoverCard(MouseEvent e) {
        Object source = e.getSource();
        if (source instanceof Node) {
            createScaleAnimation((Node) source, 150, 1.1);
        }
    }

    protected void hoverOffCard(MouseEvent e) {
        Object source = e.getSource();
        if (source instanceof Node) {
            createScaleAnimation((Node) source, 150, 1);
        }
    }

    @FXML
    void btnBackPress() {
        sceneHandler.setActiveScene(GameScene.GAME_TYPE_SELECT);
    }

    @FXML
    void btnOKPress() {
        generateQuestionsAndProgress();
    }
}
