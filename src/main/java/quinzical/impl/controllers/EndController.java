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
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;

/**
 * Controls the end game screen, where the total earnings are displayed and the option to go back to the main menu is
 * given.
 */
public class EndController extends StandardSceneController {

    @Inject
    private SceneHandler sceneHandler;

    @Inject
    private GameModel gameModel;

    @FXML
    private Label lblMoney;

    /**
     * Return to the main menu
     */
    @FXML
    void btnDonePress() {
        sceneHandler.setActiveScene(GameScene.INTRO);
    }

    @Override
    protected void onLoad() {
        int earnings = gameModel.getEarnings();
        animateLabel(earnings);
    }


    /**
     * Starts the earnings display animation
     *
     * @param animateUpTo The value to be displayed as the earnings.
     */
    private void animateLabel(final int animateUpTo) {
        final int increment = animateUpTo / 100;
        AnimationTimer timer = new AnimationTimer() {
            private int currentValue = 0;

            /**
             * Called each frame. Update the label with an incremented amount.
             */
            @Override
            public void handle(long timestamp) {
                currentValue += increment;
                lblMoney.setText("$" + currentValue);
                if (currentValue == animateUpTo) {
                    stop();
                }
            }
        };

        timer.start();
    }
}
