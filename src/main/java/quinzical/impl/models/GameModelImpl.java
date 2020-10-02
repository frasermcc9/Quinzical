package quinzical.impl.models;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.control.TextArea;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.impl.models.structures.SaveData;
import quinzical.interfaces.events.ActiveQuestionObserver;
import quinzical.interfaces.events.QuestionBoardObserver;
import quinzical.interfaces.events.ValueChangeObserver;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.GameModelSaver;
import quinzical.interfaces.models.structures.UserScore;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategyFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Singleton
public class GameModelImpl implements GameModel, GameModelSaver {

    //#region Fields

    /**
     * List of functions (observers) that are executed when the game board updates. Adding (subscribing) to the list is
     * done with {@link this#onQuestionBoardUpdate}, which will add the given QuestionBoardObserver to the list. When
     * {@link this#fireQuestionBoardUpdate)} is called, all functions will be executed.
     */
    private final List<QuestionBoardObserver> questionBoardObservers = new ArrayList<>();

    /**
     * List of functions (observers) that are executed when the game board updates.  Adding (subscribing) to the list is
     * done with {@link this#onActiveQuestionUpdate}, which will add the given ActiveQuestionObserver to the list. When
     * {@link this#fireActiveQuestionUpdate} )} is called, all functions will be executed.
     */
    private final List<ActiveQuestionObserver> activeObservers = new ArrayList<>();

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
     * Factory for creating strategies for generating the questions for the board.
     */
    @Inject
    private QuestionGeneratorStrategyFactory questionGeneratorStrategyFactory;
    /**
     * Map containing the board questions.
     */
    private Map<String, List<GameQuestion>> boardQuestions;
    /**
     * Most recent question that was active, or null if no question was active. Potential bug: is null when loading game
     * from save.
     */
    private GameQuestion activeQuestion = null;

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
     * Gets the currently active question
     *
     * @return current active question, or null if there isn't one.
     */
    @Override
    public GameQuestion getActiveQuestion() {
        return this.activeQuestion;
    }

    /**
     * Sets the active question in the game.
     */
    @Override
    public void activateQuestion(GameQuestion question) {
        this.activeQuestion = question;
        fireActiveQuestionUpdate();
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

    @Override
    public Map<String, List<GameQuestion>> getQuestionsForPracticeMode() {
        return questionGeneratorStrategyFactory.createPracticeQuestionStrategy().generateQuestions();
    }

    /**
     * Generates a new set of questions.
     */
    @Override
    public void generateNewGameQuestionSet() {
        this.boardQuestions = questionGeneratorStrategyFactory.createGameQuestionStratgey().generateQuestions();
        this.userScore.setValue(0);
        fireQuestionBoardUpdate();
    }

    @Override
    public void colourTextAreas(List<TextArea> textAreas, List<Boolean> corrects) {
        for(int i=0; i<textAreas.size(); i++ ){
            if(corrects.get(i)) {
                textAreas.get(i).setStyle("-fx-background-color: #ceffc3; -fx-text-fill: #ceffc3");
            }
            else {
                textAreas.get(i).setStyle("-fx-background-color: #ff858c; -fx-text-fill: #ffc7ca");
            }
        }
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
