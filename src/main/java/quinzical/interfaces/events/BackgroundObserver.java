package quinzical.interfaces.events;

import javafx.scene.image.Image;

/**
 * Functional interface with image argument. Is fired when the background theme is changed. The new background image is
 * passed as the argument.
 */
@FunctionalInterface
public interface BackgroundObserver {
    void updateBackground(Image img);
}
