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

package quinzical.impl.models.structures;

import com.google.inject.Inject;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.structures.UserScore;

/**
 * Manages the current score (earnings) of the game
 */
public class UserScoreImpl implements UserScore {

    /**
     * GameModel singleton to alert value changes to
     */
    @Inject
    private GameModel gameModel;

    /**
     * The users earnings
     */
    private int value = 0;

    /**
     * @return gets the user's current earnings
     */
    @Override
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of the users earnings. Fires the value change event to the GameModel.
     *
     * @param value the value to set the earnings to.
     */
    @Override
    public void setValue(int value) {
        this.value = value;
        gameModel.fireValueChange();
    }
}
