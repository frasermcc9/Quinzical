package quinzical.impl.controllers;

import javafx.scene.layout.AnchorPane;

enum Theme {
    DARK, LIGHT
}

/**
 * Abstract class for all main scenes (i.e. those that are loaded onto the
 * application main stage).
 */
public abstract class PrimarySceneController {

    /**
     * Get the background of the scene.
     *
     * @return the background AnchorPane
     */
    protected abstract AnchorPane getBackground();

    /**
     * Adjusts the background for the theme.
     *
     * @param colourHex the hex code of the colour
     */
    public void setBackground(String colourHex) {
        String currentStyle = getBackground().getStyle();
        // Removes the current background color property
        currentStyle = currentStyle.replaceAll("-fx-background-color: #[\\dabcdef]+;?", "");
        getBackground().setStyle(currentStyle + "-fx-background-color: " + colourHex + ";");
    }

    /**
     * Template method to set the theme of the game.
     *
     * @param t
     */
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

    /**
     * Is called on a theme change to set the label text to the input colour from
     * the {@link PrimarySceneController#setTheme(Theme)} template method.
     *
     * @param colourHex the colour that the labels should be changed to.
     */
    public void setLabelTextColour(String colourHex) {
        // Virtual
    }

}
