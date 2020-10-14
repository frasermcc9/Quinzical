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

/**
 * The names of the scenes that can be switched to on the main stage.
 */
public enum GameScene implements GameSceneFxmlLoader {

    INTRO {
        @Override
        public String getFxmlName () {
            return "intro";
        }
    },

    PRACTICE {
        @Override
        public String getFxmlName () {
            return "practice";
        }
    },

    GAME {
        @Override
        public String getFxmlName () {
            return "game";
        }
    },

    GAME_QUESTION {
        @Override
        public String getFxmlName () {
            return "gamequestion";
        }
    },

    PRACTICE_QUESTION {
        @Override
        public String getFxmlName () {
            return "practicequestion";
        }
    },

    END {
        @Override
        public String getFxmlName () {
            return "end";
        }
    },

    OPTIONS {
        @Override
        public String getFxmlName () {
            return "options";
        }
    }

    ;

}

interface GameSceneFxmlLoader {
    String getFxmlName();
}
