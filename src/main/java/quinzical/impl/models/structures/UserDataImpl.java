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
import quinzical.impl.constants.Theme;
import quinzical.interfaces.models.structures.AnalyticsEngineMutator;
import quinzical.interfaces.models.structures.AnalyticsEngineReader;
import quinzical.interfaces.models.structures.UserData;

import java.io.Serializable;
import java.util.*;

public class UserDataImpl implements Serializable, UserData {

    private final static Set<Theme> DEFAULT_THEMES;

    static {
        DEFAULT_THEMES = new HashSet<>();
        DEFAULT_THEMES.addAll(Arrays.asList(Theme.AUCKLAND, Theme.BOULDERS, Theme.MOUNTAINS, Theme.FIELDS, Theme.HOBBIT,
            Theme.OCEAN, Theme.VOLCANO));
    }

    private final Set<Theme> unlockedThemes = new HashSet<>();
    @Inject
    private AnalyticsEngineMutator analyticsEngine;
    private Map<String, List<GameQuestion>> board;
    private int earnings = 0;
    private int coins = 0;

    public void answerQuestion(String category, boolean correct) {
        analyticsEngine.answerQuestion(category, correct);

    }

    public void finishCategory() {
        this.analyticsEngine.setCategoriesAnswered(this.analyticsEngine.getCategoriesAnswered() + 1);
    }

    @Override
    public int getCorrect() {
        return analyticsEngine.getCorrectAnswers();
    }

    @Override
    public int getIncorrect() {
        return analyticsEngine.getQuestionsAnswered() - getCorrect();
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

    @Override
    public boolean isInternationalUnlocked() {
        return analyticsEngine.getCategoriesAnswered() > 2;
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

    /**
     * Resets the user data. Does not reset coins.
     */
    public void resetUserData() {
        this.board = null;
        this.earnings = 0;
        this.analyticsEngine.resetData();
    }

    @Override
    public boolean isGameActive() {
        return this.board != null;
    }

    public AnalyticsEngineReader getAnalytics() {
        return this.analyticsEngine;
    }

    @Override
    public void incrementCoins(int value) {
        this.coins += value;
    }

    public boolean addTheme(Theme theme) {
        return this.unlockedThemes.add(theme);
    }

    public Set<Theme> getUnlockedThemes() {
        Set<Theme> set = DEFAULT_THEMES;
        set.addAll(this.unlockedThemes);
        return new HashSet<>(set);
    }
}
