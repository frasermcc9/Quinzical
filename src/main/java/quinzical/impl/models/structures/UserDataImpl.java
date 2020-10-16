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

import quinzical.interfaces.models.structures.UserData;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class UserDataImpl implements Serializable, UserData {

    private Map<String, List<GameQuestion>> board;
    private int earnings = 0;

    private boolean internationalUnlocked = false;
    private int coins = 0;

    private int questionsAnswered = 0;
    private int categoriesAnswered = 0;
    private int correctAnswers = 0;

    public void answerQuestion(boolean correct) {
        questionsAnswered++;
        correctAnswers += correct ? 1 : 0;
    }

    public void finishCategory() {
        this.categoriesAnswered += 1;
    }

    @Override
    public int getCorrect() {
        return correctAnswers;
    }

    @Override
    public int getIncorrect() {
        return questionsAnswered / 2 - getCorrect();
    }

    @Override
    public void LoadSavedData(Map<String, List<GameQuestion>> board, int earnings) {
        this.board = board;
        this.earnings = earnings;
    }

    @Override
    public Map<String, List<GameQuestion>> getBoard() {
        return board;
    }

    @Override
    public void createNewBoard(Map<String, List<GameQuestion>> board) {
        this.board = board;
        this.earnings = 0;
    }

    public void activateInternationalQuestions() {
        internationalUnlocked = true;
    }

    @Override
    public boolean isInternationalUnlocked() {
        return categoriesAnswered > 2;
    }

    @Override
    public int getEarnings() {
        return earnings;
    }

    @Override
    public void incrementEarnings(int earnings) {
        this.earnings += earnings;
    }

    @Override
    public int getCoins() {
        return coins;
    }

    @Override
    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void resetUserData() {
        this.board = null;
        this.earnings = 0;
        this.internationalUnlocked = false;
        this.coins = 0;
    }

    @Override
    public boolean isGameActive() {
        return this.board != null;
    }
}
