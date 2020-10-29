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
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import quinzical.impl.constants.GameScene;
import quinzical.impl.constants.Theme;
import quinzical.impl.controllers.components.StoreItem;
import quinzical.impl.models.structures.FxmlInfo;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.models.structures.UserData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StoreController extends AbstractSceneController {

    @Inject
    private GameModel gameModel;
    @Inject
    private SceneHandler sceneHandler;
    @Inject
    private Injector injector;

    @FXML
    private Label coinLabel;
    @FXML
    private JFXMasonryPane storeContainer;

    /**
     * Return to the main menu
     */
    @FXML
    void btnBackPress() {
        Platform.runLater(() -> sceneHandler.setActiveScene(GameScene.INTRO));
    }

    @Override
    protected void onLoad() {
        this.loadShop();
    }

    /**
     * Loads the shop components.
     */
    private void loadShop() {
        // Setup showing how much currency the user has
        UserData userData = gameModel.getUserData();
        coinLabel.setText(userData.getCoins() + "x Coins");

        // These are the items to be sold
        int[] costs = new int[]{150, 25, 150, 100, 50, 25};
        Theme[] themes = new Theme[]{Theme.LAKE, Theme.MIST, Theme.SNOW, Theme.SHEEP, Theme.CAVE, Theme.DESERT};

        List<Parent> toAdd = new ArrayList<>();

        for (int i = 0; i < costs.length; i++) {
            try {
                FxmlInfo<StoreItem<Theme>> fxmlInfo = FxmlInfo.loadFXML("components/store-item", injector);
                Parent p = fxmlInfo.getParent();
                StoreItem<Theme> storeItem = fxmlInfo.getController();
                storeItem.setData(costs[i], themes[i], "Theme", themes[i].name());
                if (userData.getUnlockedThemes().contains(themes[i])) {
                    storeItem.disableBuy();
                    storeItem.overrideButtonText("OWNED");
                } else {
                    final int themeIdx = i;
                    storeItem.setBuyAction(() -> {
                        userData.addTheme(themes[themeIdx]);
                        sceneHandler.setActiveScene(GameScene.STORE);
                    });
                }
                toAdd.add(p);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        storeContainer.getChildren().addAll(toAdd);
    }
    
}
