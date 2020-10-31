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
import quinzical.impl.models.structures.GameQuestion;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface to represent the user's save data. Information in this class represents the local game state (i.e. current
 * game questions, earnings), as well as more permanent items, such as coins, themes that have been unlocked, and an
 * analytics engine.
 *
 * @author Fraser McCallum
 * @see quinzical.interfaces.models.GameModel
 * @see quinzical.interfaces.models.GameModelSaver
 * @see quinzical.impl.models.GameModelImpl
 * @see quinzical.impl.models.structures.UserDataImpl
 * @see quinzical.impl.models.structures.AnalyticsEngineImpl
 * @see AnalyticsEngineMutator
 * @see AnalyticsEngineReader
 * @since 1.0
 */
public interface UserData {

    /**
     * Answer a question.
     *
     * @param category the category of the question
     * @param correct  if the answer was correct.
     */
    void answerQuestion(String category, boolean correct);

    /**
     * Indicate that a category has been completed.
     */
    void finishCategory();

    /**
     * @return the number of correct answers that have been given.
     */
    int getCorrect();

    /**
     * @return the number of incorrect answers that have been given.
     */
    int getIncorrect();

    /**
     * Loads a game into the save data.
     *
     * @param board    the question board.
     * @param earnings the earnings.
     */
    void LoadSavedData(Map<String, List<GameQuestion>> board, int earnings);

    /**
     * Gets the saved question board (questions and which were correct, incorrect or unanswered).
     *
     * @return the question board saved in this UserData.
     */
    Map<String, List<GameQuestion>> getBoard();

    /**
     * Sets a board into the user data.
     *
     * @param board the board to set.
     */
    void createNewBoard(Map<String, List<GameQuestion>> board);

    /**
     * Gets the user's current earnings in a game
     *
     * @return the user's current earnings.
     */
    int getEarnings();

    /**
     * Increments the users earnings
     *
     * @param earnings the amount to increase the user's earnings by.
     */
    void incrementEarnings(int earnings);

    /**
     * Checks if the user has unlocked international questions.
     *
     * @return true if they are unlocked
     */
    boolean isInternationalUnlocked();

    /**
     * Resets the local data. First confirms the user wishes to delete the data, and then executes the reset data on the
     * user data object.
     */
    void resetUserData();

    /**
     * @return the number of coins the user has.
     */
    int getCoins();

    /**
     * @param coins what to set the users new coins as
     */
    void setCoins(int coins);

    /**
     * Checks if a game is currently active (saved in this userdata)
     *
     * @return true if a game is active
     */
    boolean isGameActive();

    /**
     * Increments the users coins by a given amount
     *
     * @param value how much to increment the users coins by
     */
    void incrementCoins(int value);

    /**
     * Gets a readonly analytics engine
     *
     * @return the analytics engine for this userdata
     */
    AnalyticsEngineReader getAnalytics();

    /**
     * Adds a theme to this users collection. Duplicate themes will return false.
     *
     * @param theme the theme to add
     * @return true if this theme wasn't already owned.
     */
    boolean addTheme(Theme theme);

    /**
     * Gets a collection of the users unlocked themes.
     *
     * @return a set of unlocked themes.
     */
    Set<Theme> getUnlockedThemes();
}
