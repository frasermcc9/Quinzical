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
 * the class which stores user settings to disks in between play sessions. This interface extends {@link
 * ReadonlyPersistentSettings}, which should be used if the client class needs only to read the settings. Only use this
 * interface if the settings require change.
 * <p>
 * Generally this class is only used at game launch and exit. On game launch, a preferences file is loaded. These
 * settings are then injected into the various objects that depend on them. When the game is exited, the opposite
 * happens. The models create a new PersistentSettings implementation and inject their values into it. This object is
 * then saved to disk.
 *
 * @author Fraser McCallum
 * @see ReadonlyPersistentSettings
 * @see quinzical.impl.models.structures.PersistentSettingsImpl
 * @since 1.1
 */
public interface PersistentSettings extends ReadonlyPersistentSettings {

    /**
     * Change the game theme
     *
     * @param theme theme to change to
     */
    void setTheme(Theme theme);

    /**
     * Voice setting for gap
     *
     * @param gap the new gap
     */
    void setGap(int gap);

    /**
     * Voice setting for speed
     *
     * @param speed the new speed
     */
    void setSpeed(int speed);

    /**
     * Voice setting for amp
     *
     * @param amp the new amp
     */
    void setAmp(int amp);

    /**
     * Voice setting for pitch
     *
     * @param pitch the new pitch
     */
    void setPitch(int pitch);

    /**
     * Setting change for the game question timer duration.
     *
     * @param timer the new timer duration
     */
    void setTimer(double timer);

    /**
     * Loads a preferences file from the disk, from the typical save location. The preferences are imported into this
     * object.
     *
     * @return this
     */
    PersistentSettings loadSettingsFromDisk();

    /**
     * Sets all the preferences in this object to the current settings in game.
     *
     * @return this
     */
    PersistentSettings loadSettingsFromGame();
}
