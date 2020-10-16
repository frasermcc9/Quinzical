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

package quinzical.interfaces.models;

import quinzical.impl.models.structures.GameQuestion;
import quinzical.interfaces.models.structures.UserData;

import java.util.List;
import java.util.Map;

/**
 * Interface of GameModelImpl class. Handles various factors for the game, such as the game questions and the users
 * earnings.
 */
public interface GameModel extends QuinzicalModel {

    Map<String, List<GameQuestion>> getBoardQuestions();

    void generateNewGameQuestionSet();

    void generateInternationalQuestions();

    void generateGameQuestionSetFromCategories(String[] categories);

    void generateGameQuestionSetFromCategories(List<String> categories);

    int numberOfQuestionsRemaining(Map<String, List<GameQuestion>> boardQuestions);

    int numberOfQuestionsRemaining();

    void loadSaveData(UserData userData);

    GameQuestion getNextActiveQuestion(GameQuestion question);

    void answerActive(boolean correct);

    int getEarnings();

    boolean isGameActive();

    boolean isInternationalUnlocked();

    UserData getUserData();
}
