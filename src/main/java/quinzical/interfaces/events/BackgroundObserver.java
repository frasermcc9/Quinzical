package quinzical.interfaces.events;

import javafx.scene.image.Image;

@FunctionalInterface
public interface BackgroundObserver {
    void updateBackground(Image img);
}
