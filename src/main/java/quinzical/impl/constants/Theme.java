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

/**
 * provides the different themes that the game can have, as well as the getImage() method to get the associated image
 * for the theme.
 */
public enum Theme {
    MOUNTAINS {
    },

    BOULDERS {
    },

    FIELDS {
    },

    AUCKLAND {
    },

    OCEAN {
    },

    HOBBIT {
    },

    VOLCANO {
    },

    LAKE {
    },

    MIST {
    },

    SNOW {
    },

    SHEEP {
    },

    CAVE {
    },

    DESERT {
    }


}

/**
 * @deprecated this is no longer used since background loading is now handled by stylesheets.
 */
@Deprecated(since = "25/10")
interface ImageProvider {
    Image getImage();
}
