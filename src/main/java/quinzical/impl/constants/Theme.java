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

package quinzical.impl.constants;

import javafx.scene.image.Image;
import quinzical.Entry;

import java.util.Objects;

/**
 * provides the different themes that the game can have, as well
 * as the getImage() method to get the associated image for the theme.
 */
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
