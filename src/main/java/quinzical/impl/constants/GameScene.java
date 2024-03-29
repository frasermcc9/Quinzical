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
        public String getFxmlName() {
            return "intro";
        }
    },

    GAME_TYPE_SELECT {
        @Override
        public String getFxmlName() {
            return "gametypeselect";
        }
    },

    PRACTICE {
        @Override
        public String getFxmlName() {
            return "practice-selector";
        }
    },

    GAME {
        @Override
        public String getFxmlName() {
            return "game";
        }
    },

    GAME_QUESTION {
        @Override
        public String getFxmlName() {
            return "gamequestion";
        }
    },

    PRACTICE_QUESTION {
        @Override
        public String getFxmlName() {
            return "practice-question";
        }
    },

    END {
        @Override
        public String getFxmlName() {
            return "end";
        }
    },

    OPTIONS {
        @Override
        public String getFxmlName() {
            return "options";
        }
    },

    STATISTICS {
        @Override
        public String getFxmlName() {
            return "statistics";
        }
    },
    STORE {
        @Override
        public String getFxmlName() {
            return "store";
        }
    },
    CATEGORY_SELECTOR {
        @Override
        public String getFxmlName() {
            return "category-selector";
        }
    },

    MULTI_INTRO {
        @Override
        public String getFxmlName() {
            return "multiplayer/entry";
        }
    },

    MULTI_GAME {
        @Override
        public String getFxmlName() {
            return "multiplayer/game";
        }
    },

    MULTI_GAME_END {
        @Override
        public String getFxmlName() {
            return "multiplayer/game-end";
        }
    },

    MULTI_HOST {
        @Override
        public String getFxmlName() {
            return "multiplayer/host";
        }
    },

    MULTI_HOST_WAIT {
        @Override
        public String getFxmlName() {
            return "multiplayer/host-wait";
        }
    },

    MULTI_MENU {
        @Override
        public String getFxmlName() {
            return "multiplayer/menu";
        }
    },

    MULTI_PLAYER_WAIT {
        @Override
        public String getFxmlName() {
            return "multiplayer/player-wait";
        }
    },

    MULTI_ROUND_END {
        @Override
        public String getFxmlName() {
            return "multiplayer/round-end";
        }
    },

    MULTI_BROWSE {
        @Override
        public String getFxmlName() {
            return "multiplayer/browse";
        }
    },
    MULTI_ACCOUNT {
        @Override
        public String getFxmlName() {
            return "multiplayer/create-account";
        }
    },
    MULTI_LEADERBOARD {
        @Override
        public String getFxmlName() {
            return "multiplayer/leaderboard";
        }
    },
    MULTI_PROFILE {
        @Override
        public String getFxmlName() {
            return "multiplayer/profile";
        }
    }
}

interface GameSceneFxmlLoader {
    String getFxmlName();
}
