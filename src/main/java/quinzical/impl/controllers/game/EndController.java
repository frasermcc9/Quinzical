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
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import quinzical.impl.constants.GameScene;
import quinzical.impl.controllers.AbstractSceneController;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;

/**
 * Controls the end game screen, where the total earnings are displayed and the
 * option to go back to the main menu is given.
 */
public class EndController extends AbstractSceneController {

    @Inject
    private SceneHandler sceneHandler;

    @Inject
    private GameModel gameModel;

    @FXML
    private Label lblMoney;

    @FXML
    private HBox coinBox;

    /**
     * Return to the main menu
     */
    @FXML
    void btnDonePress() {
        sceneHandler.setActiveScene(GameScene.INTRO);
    }

    @Override
    protected void onLoad() {
        coinBox.setVisible(false);
        final int earnings = gameModel.getEarnings();
        animateLabel(earnings);
        gameModel.getUserData().incrementCoins(earnings / 100);
        ((Label) coinBox.getChildren().get(0)).setText("You Made " + earnings / 100 + " Coins!");
    }

    /**
     * Starts the earnings display animation
     *
     * @param animateUpTo The value to be displayed as the earnings.
     */
    private void animateLabel(final int animateUpTo) {
        final int increment = animateUpTo / 100;
        final AnimationTimer timer = new AnimationTimer() {
            private int currentValue = 0;

            /**
             * Called each frame. Update the label with an incremented amount.
             */
            @Override
            public void handle(final long timestamp) {
                currentValue += increment;
                lblMoney.setText(currentValue + "");
                if (currentValue == animateUpTo) {
                    stop();
                    runCoinAnimation();
                }
            }

            private void runCoinAnimation() {
                final ScaleTransition st = new ScaleTransition(Duration.seconds(1));
                st.setFromX(0);
                st.setFromY(0);
                st.setToX(0.9);
                st.setToY(0.9);
                st.setNode(coinBox);

                st.setInterpolator(new Interpolator() {
                    @Override
                    protected double curve(final double t) {
                        return -1.76 * (Math.pow(t, 3)) + 0.931 * (Math.pow(t, 2)) + 1.785 * t;
                    }
                });
                st.playFromStart();
                Platform.runLater(() -> coinBox.setVisible(true));

            }
        };

        timer.start();
    }
}
