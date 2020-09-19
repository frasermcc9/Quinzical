package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.scene.layout.AnchorPane;
import quinzical.impl.constants.GameEvent;
import quinzical.interfaces.models.SceneHandler;

enum Theme {
    DARK, LIGHT
}

public abstract class PrimarySceneController {

    protected abstract AnchorPane getBackground();

    public void setBackground(String colourHex) {
        getBackground().setStyle("-fx-background-color: " + colourHex);
    }

    public void setTheme(Theme t) {
        switch (t) {
            case DARK:
                setBackground("#232323");
                setLabelTextColour("#c9c9c9");
                break;
            case LIGHT:
                setBackground("#c9c9c9");
                setLabelTextColour("#232323");
                break;
        }
    }

    public void setLabelTextColour(String colourHex) {
        //Virtual
    }

}
