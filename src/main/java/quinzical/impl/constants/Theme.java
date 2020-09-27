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
    },
    
    FIELDS {
        public Image getImage() {
            return new Image(Objects.requireNonNull(Entry.class.getClassLoader().getResourceAsStream("images" +
                "/landscapes/fields.jpg")));
        }
    },
    
    AUCKLAND {
        public Image getImage() {
            return new Image(Objects.requireNonNull(Entry.class.getClassLoader().getResourceAsStream("images" +
                "/landscapes/auckland.jpg")));
        }
    },

    OCEAN {
        public Image getImage() {
            return new Image(Objects.requireNonNull(Entry.class.getClassLoader().getResourceAsStream("images" +
                "/landscapes/ocean.jpg")));
        }
    },
    
    HOBBIT {
        public Image getImage() {
            return new Image(Objects.requireNonNull(Entry.class.getClassLoader().getResourceAsStream("images" +
                "/landscapes/hobbit.jpg")));
        }
    },
    
    VOLCANO {
        public Image getImage() {
            return new Image(Objects.requireNonNull(Entry.class.getClassLoader().getResourceAsStream("images" +
                "/landscapes/volcano.jpg")));
        }
    }
}

interface ImageProvider{
    Image getImage();
}
