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

    public final void answerQuestion(final String category, final boolean correct) {
        analyticsEngine.answerQuestion(category, correct);

    }

    public final void finishCategory() {
        this.analyticsEngine.setCategoriesAnswered(this.analyticsEngine.getCategoriesAnswered() + 1);
    }

    @Override
    public final int getCorrect() {
        return analyticsEngine.getCorrectAnswers();
    }

    @Override
    public final int getIncorrect() {
        return analyticsEngine.getQuestionsAnswered() - getCorrect();
    }

    @Override
    public final void LoadSavedData(final Map<String, List<GameQuestion>> board, final int earnings) {
        this.board = board;
        this.earnings = earnings;
    }

    @Override
    public final Map<String, List<GameQuestion>> getBoard() {
        return board;
    }

    @Override
    public final void createNewBoard(final Map<String, List<GameQuestion>> board) {
        this.board = board;
        this.earnings = 0;
    }

    @Override
    public final boolean isInternationalUnlocked() {
        return analyticsEngine.getCategoriesAnswered() > 2;
    }

    @Override
    public final int getEarnings() {
        return earnings;
    }

    @Override
    public final void incrementEarnings(final int earnings) {
        this.earnings += earnings;
    }

    @Override
    public final int getCoins() {
        return coins;
    }

    @Override
    public final void setCoins(final int coins) {
        this.coins = coins;
    }

    /**
     * Resets the user data. Does not reset coins.
     */
    public final void resetUserData() {
        this.board = null;
        this.earnings = 0;
        this.analyticsEngine.resetData();
    }

    @Override
    public final boolean isGameActive() {
        return this.board != null;
    }

    public final AnalyticsEngineReader getAnalytics() {
        return this.analyticsEngine;
    }

    @Override
    public final void incrementCoins(final int value) {
        this.coins += value;
    }

    public final boolean addTheme(final Theme theme) {
        return this.unlockedThemes.add(theme);
    }

    public final Set<Theme> getUnlockedThemes() {
        final Set<Theme> set = DEFAULT_THEMES;
        set.addAll(this.unlockedThemes);
        return new HashSet<>(set);
    }
}
