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

import quinzical.impl.models.structures.GameQuestion;

import java.util.List;
import java.util.Map;

public interface UserData {

    void answerQuestion(boolean correct);

    void finishCategory();
    
    int getCorrect();
    int getIncorrect();

    void LoadSavedData(Map<String, List<GameQuestion>> board, int earnings);

    Map<String, List<GameQuestion>> getBoard();

    void createNewBoard(Map<String, List<GameQuestion>> board);

    int getEarnings();

    void incrementEarnings(int earnings);

    boolean isInternationalUnlocked();

    void activateInternationalQuestions();

    void resetUserData();

    int getCoins();

    void setCoins(int coins);

    boolean isGameActive();
}
