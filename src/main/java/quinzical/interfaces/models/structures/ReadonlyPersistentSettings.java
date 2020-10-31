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

package quinzical.interfaces.models.structures;

import quinzical.impl.constants.Theme;

/**
 * Interface for {@link quinzical.impl.models.structures.PersistentSettingsImpl} class. This interface is designed for
 * the class which stores user settings to disks in between play sessions. Use this interface if you only need to read
 * settings.
 *
 * @author Fraser McCallum
 * @see PersistentSettings
 * @see quinzical.interfaces.models.GameModelSaver
 * @see quinzical.impl.models.structures.PersistentSettingsImpl
 * @since 1.1
 */
public interface ReadonlyPersistentSettings {
    /**
     * Get the theme in the persistent settings.
     *
     * @return the theme that has been saved.
     */
    Theme getTheme();

    /**
     * @return the saved gap.
     */
    int getGap();

    /**
     * @return the saved speed.
     */
    int getSpeed();

    /**
     * @return the saved amp.
     */
    int getAmp();

    /**
     * @return the saved pitch.
     */
    int getPitch();

    /**
     * @return the saved timer duration.
     */
    double getTimer();

    /**
     * Applies the settings that are saved in this object to the game.
     */
    void applySettings();
}
