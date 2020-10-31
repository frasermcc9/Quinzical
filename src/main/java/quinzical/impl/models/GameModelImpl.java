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

package quinzical.impl.models;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.GameModelSaver;
import quinzical.interfaces.models.QuinzicalModel;
import quinzical.interfaces.models.structures.PersistentSettings;
import quinzical.interfaces.models.structures.UserData;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

/**
 * The model for the main game of the application, controlling the saving, loading, and question and score saving and
 * getting of the game.
 */
@Singleton
public class GameModelImpl extends AbstractGameModel implements GameModel, GameModelSaver, QuinzicalModel {

    @Inject
    private UserData userData;
    @Inject
    private PersistentSettings persistentSettings;

    private double timerValue = 25;

    /**
     * @return Gets the user value.
     */
    @Override
    public int getEarnings() {
        return userData.getEarnings();
    }

    /**
     * Increases the value in the model,
     *
     * @param number the amount to increase by
     */
    public void increaseValueBy(int number) {
        this.userData.incrementEarnings(number);
    }


    // #region Active Question

    /**
     * Give a game question, returns the next in the category, or null if it is the final question
     *
     * @param question the current game question
     * @return the next game question in category, or null.
     */
    @Override
    public GameQuestion getNextActiveQuestion(GameQuestion question) {
        String category = question.getCategory();
        int index = this.userData.getBoard().get(category).indexOf(question);
        if (index == 4) {
            return null;
        } else {
            return this.userData.getBoard().get(category).get(index + 1);
        }

    }

    /**
     * Answers whatever the active question is.
     * <p>
     * Removes the active question, sets it as answered and no longer answerable, and sets the next question in the
     * category as answerable.
     */
    @Override
    public void answerActive(boolean correct) {
        GameQuestion question = this.activeQuestion;
        this.activeQuestion.answer(correct);
        // this.activeQuestion = null;

        GameQuestion next = getNextActiveQuestion(question);
        if (next != null) {
            next.setAnswerable(true);
        } else {
            userData.finishCategory();
        }
        userData.answerQuestion(question.getCategory(), correct);

        if (correct) {
            increaseValueBy(question.getValue());
        }

    }

    // #endregion

    // #region Game Board and State

    /**
     * Generates a new set of questions.
     */
    @Override
    public void generateNewGameQuestionSet() {
        Map<String, List<GameQuestion>> board = questionGeneratorStrategyFactory.createGameQuestionStrategy()
            .generateQuestions();
        this.userData.createNewBoard(board);
    }

    /**
     * Generates a set of International Questions. This method should be run on another thread as it makes API calls
     * that can be somewhat slow to execute.
     */
    @Override
    public void generateInternationalQuestions() {
        Map<String, List<GameQuestion>> board = questionGeneratorStrategyFactory.createInternationalQuestionStrategy()
            .generateQuestions();
        this.userData.createNewBoard(board);
    }

    /**
     * Generates questions from the given array of categories.
     *
     * @param categories String array of categories to generate questions from. Must be of length 5.
     */
    public void generateGameQuestionSetFromCategories(String[] categories) {
        generateGameQuestionSetFromCategories(List.of(categories));
    }

    /**
     * Generates questions from the given list of categories.
     *
     * @param categories String list of categories. Must be at least size 5.
     */
    public void generateGameQuestionSetFromCategories(List<String> categories) {
        if (categories.size() != 5)
            throw new IllegalArgumentException("Generating game questions from category must be given 5 categories.");

        Map<String, List<GameQuestion>> board = questionGeneratorStrategyFactory
            .createSelectedCategoryStrategy(categories).generateQuestions();
        this.userData.createNewBoard(board);
    }

    public double getTimerValue() {
        return timerValue;
    }

    public void setTimerValue(double value) {
        timerValue = value;
    }

    /**
     * Returns map containing the questions for the current game.
     */
    @Override
    public Map<String, List<GameQuestion>> getBoardQuestions() {
        return userData.getBoard();
    }

    /**
     * @return the number of questions that are unanswered
     */
    @Override
    public int numberOfQuestionsRemaining(Map<String, List<GameQuestion>> boardQuestions) {
        return boardQuestions.values().stream().reduce(0,
            (sub, el) -> sub
                + el.stream().reduce(0, (acc, curr) -> acc + (curr.isAnswered() ? 0 : 1), Integer::sum),
            Integer::sum);
    }

    /**
     * @return the number of questions that are unanswered
     */
    @Override
    public int numberOfQuestionsRemaining() {
        return numberOfQuestionsRemaining(this.userData.getBoard());
    }

    /**
     * Loads a game state into the model.
     *
     * @param userData the game state that has been saved.
     */
    @Override
    public void loadSaveData(UserData userData) {
        this.userData = userData;
    }

    /**
     * Saves the current game state to disk as a SaveData object.
     *
     * @throws IOException if the folder hierarchy is broken.
     */
    @Override
    public void saveGame() throws IOException {
        FileOutputStream save = new FileOutputStream(System.getProperty("user.dir") + "/data/save.qdb");
        ObjectOutputStream saveOut = new ObjectOutputStream(save);
        saveOut.writeObject(userData);
        saveOut.close();
        save.close();

        FileOutputStream settings = new FileOutputStream(System.getProperty("user.dir") + "/data/preferences.qdb");
        ObjectOutputStream settingsOut = new ObjectOutputStream(settings);
        settingsOut.writeObject(persistentSettings.loadSettingsFromGame());
        settingsOut.close();
        settings.close();
    }

    @Override // delegate
    public boolean isGameActive() {
        return userData.isGameActive();
    }

    @Override // delegate
    public boolean isInternationalUnlocked() {
        return userData.isInternationalUnlocked();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserData getUserData() {
        return this.userData;
    }

    // #endregion

}
