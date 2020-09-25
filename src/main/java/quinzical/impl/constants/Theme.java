package quinzical.impl.constants;

import javafx.scene.image.Image;
import quinzical.Entry;

import java.util.Objects;

public enum Theme implements ImageProvider {
    MOUNTAINS {
        public Image getImage() {
            return new Image(Objects.requireNonNull(Entry.class.getClassLoader().getResourceAsStream("images" +
                "/landscapes/mountains.jpg")));
        }
    },

    BOULDERS {
        public Image getImage() {
            return new Image(Objects.requireNonNull(Entry.class.getClassLoader().getResourceAsStream("images" +
                "/landscapes/boulders.jpg")));
        }
    }
}

interface ImageProvider{
    Image getImage();
}
