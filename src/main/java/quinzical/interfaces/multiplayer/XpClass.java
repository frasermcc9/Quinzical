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

package quinzical.interfaces.multiplayer;

import com.google.common.math.DoubleMath;

import java.math.RoundingMode;

public interface XpClass {
    /**
     * Returns the level that a player with experience `x` would be
     *
     * @param x the cumulative xp of the player
     * @return the level that the player would be
     */
    static int inverseExpFunction(int x) {
        for (int i = 0; ; i++) {
            if (expFunction(i) > x) {
                return Math.max(i - 1, 0);
            }
        }
    }

    /**
     * Returns cumulative xp required to reach a level
     *
     * @param x the level
     * @return the cumulative xp to reach this level
     */
    static int expFunction(int x) {
        return DoubleMath.roundToInt(50000 * Math.pow(1.05, x) - 48000, RoundingMode.HALF_UP);
    }

    int getLevel();
}
