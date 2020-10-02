package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;

public class EndController {

    @Inject
    private SceneHandler sceneHandler;

    @Inject
    private GameModel gameModel;

    @FXML
    private ImageView imgBackground;

    @FXML
    private Label lblMoney;

    /**
     * Return to the main menu
     */
    @FXML
    void btnDonePress() {
        sceneHandler.setActiveScene(GameScene.INTRO);
    }

    @FXML
    void initialize() {
        assert imgBackground != null : "fx:id=\"imgBackground\" was not injected: check your FXML file 'end.fxml'.";
        assert lblMoney != null : "fx:id=\"lblMoney\" was not injected: check your FXML file 'end.fxml'.";

        listen();
    }

    /**
     *
     */
    private void listen() {
        //Listen for scene change, when this scene is selected, run the animation.
        sceneHandler.onSceneChange(s -> {
            if (s.equals(GameScene.END)) {
                int earnings = gameModel.getValue();
                animateLabel(earnings);
            }
        });

        //Listen for theme change
        sceneHandler.onBackgroundChange(img -> this.imgBackground.setImage(img));
    }

    //Begin the money animation.
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
