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
import javafx.scene.control.TextArea;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.impl.models.structures.SaveData;
import quinzical.impl.util.questionparser.Question;
import quinzical.interfaces.events.ActiveQuestionObserver;
import quinzical.interfaces.events.QuestionBoardObserver;
import quinzical.interfaces.events.ValueChangeObserver;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.GameModelSaver;
import quinzical.interfaces.models.QuinzicalModel;
import quinzical.interfaces.models.structures.UserScore;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Singleton
public class GameModelImpl extends AbstractGameModel implements GameModel, GameModelSaver, QuinzicalModel {

    //#region Fields

    /**
     * List of functions (observers) that are executed when the game board updates. Adding (subscribing) to the list is
     * done with {@link this#onQuestionBoardUpdate}, which will add the given QuestionBoardObserver to the list. When
     * {@link this#fireQuestionBoardUpdate)} is called, all functions will be executed.
     */
    private final List<QuestionBoardObserver> questionBoardObservers = new ArrayList<>();


    /**
     * List of functions (observers) that are executed when the game board updates.  Adding (subscribing) to the list is
     * done with {@link this#onValueChange}, which will add the given ActiveQuestionObserver to the list. When {@link
     * this#fireValueChange} )} is called, all functions will be executed.
     */
    private final List<ValueChangeObserver> valueChangeObservers = new ArrayList<>();
    /**
     * Stores the user's current earnings within the game.
     */
    @Inject
    private UserScore userScore;

    /**
     * Map containing the board questions.
     */
    private Map<String, List<GameQuestion>> boardQuestions;

    //#endregion

    //#region Observer Methods

    /**
     * Binds a function to the question update event. This event is fired when the question board is updated.
     *
     * @param fn the function to call when the event is fired.
     */
    @Override
    public void onQuestionBoardUpdate(QuestionBoardObserver fn) {
        questionBoardObservers.add(fn);
    }

    /**
     * Binds a function to the event that is fired when there is a new active question.
     *
     * @param fn the function to call when the event is fired.
     */
    @Override
    public void onActiveQuestionUpdate(ActiveQuestionObserver fn) {
        activeObservers.add(fn);
    }


    /**
     * Binds a function to the event that is fired when there is a new active question.
     *
     * @param fn the function to call when the event is fired.
     */
    @Override
    public void onValueChange(ValueChangeObserver fn) {
        valueChangeObservers.add(fn);
    }

    /**
     * Fire the questions update event.
     */
    @Override
    public void fireQuestionBoardUpdate() {
        questionBoardObservers.forEach(QuestionBoardObserver::updateQuestionDisplay);
    }

    /**
     * Fire the questions update event.
     */
    @Override
    public void fireValueChange() {
        valueChangeObservers.forEach(ValueChangeObserver::updateValue);
    }

    /**
     * Alerts all observers that a new game question has been set as the active question.
     */
    public void fireActiveQuestionUpdate() {
        activeObservers.forEach(ActiveQuestionObserver::fireActiveQuestion);
    }

    //#endregion

    //#region User earnings methods

    /**
     * @return Gets the user value.
     */
    @Override
    public int getValue() {
        return userScore.getValue();
    }

    /**
     * Increases the value in the model,
     *
     * @param number the amount to increase by
     */
    public void increaseValueBy(int number) {
        this.userScore.setValue(this.userScore.getValue() + number);
    }

    //#endregion

    //#region Active Question

    /**
     * Give a game question, returns the next in the category, or null if it is the final question
     *
     * @param question the current game question
     * @return the next game question in category, or null.
     */
    @Override
    public GameQuestion getNextActiveQuestion(GameQuestion question) {
        String category = question.getCategory();
        int index = this.boardQuestions.get(category).indexOf(question);
        if (index == 4) {
            return null;
        } else {
            return this.boardQuestions.get(category).get(index + 1);
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
        //this.activeQuestion = null;

        GameQuestion next = getNextActiveQuestion(question);
        if (next != null) {
            next.setAnswerable(true);
        }

        if (correct) {
            increaseValueBy(question.getValue());
        }

        fireQuestionBoardUpdate();
    }

    //#endregion

    //#region Game Board and State

    /**
     * Generates a new set of questions.
     */
    @Override
    public void generateNewGameQuestionSet() {
        this.boardQuestions = questionGeneratorStrategyFactory.createGameQuestionStrategy().generateQuestions();
        this.userScore.setValue(0);
        fireQuestionBoardUpdate();
    }

    /**
     * Returns map containing the questions for the current game.
     */
    @Override
    public Map<String, List<GameQuestion>> getBoardQuestions() {
        return boardQuestions;
    }

    /**
     * @return the number of questions that are unanswered
     */
    @Override
    public int numberOfQuestionsRemaining(Map<String, List<GameQuestion>> boardQuestions) {
        return boardQuestions.values().stream().reduce(0, (sub, el) -> sub + el.stream().reduce(0,
            (acc, curr) -> acc + (curr.isAnswered() ? 0 : 1), Integer::sum), Integer::sum);
    }

    /**
     * @return the number of questions that are unanswered
     */
    @Override
    public int numberOfQuestionsRemaining() {
        return numberOfQuestionsRemaining(this.boardQuestions);
    }

    /**
     * Loads a game state into the model.
     *
     * @param saveData the game state that has been saved.
     */
    @Override
    public void loadSaveData(SaveData saveData) {
        this.boardQuestions = saveData.getQuestionData();
        this.userScore.setValue(saveData.getValue());

        fireQuestionBoardUpdate();
    }

    /**
     * Saves the current game state to disk as a SaveData object.
     *
     * @throws IOException if the folder hierarchy is broken.
     */
    @Override
    public void saveQuestionsToDisk() throws IOException {
        FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.dir") + "/data/save.qdb");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);

        SaveData sd = new SaveData().setQuestionData(this.boardQuestions).setValue(this.userScore.getValue());
        out.writeObject(sd);

        out.close();
        fileOut.close();
    }

    //#endregion

}
