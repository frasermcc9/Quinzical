package quinzical.interfaces.models;

import javafx.scene.control.TextArea;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.impl.models.structures.SaveData;
import quinzical.interfaces.events.ActiveQuestionObserver;
import quinzical.interfaces.events.QuestionBoardObserver;
import quinzical.interfaces.events.ValueChangeObserver;

import java.util.List;
import java.util.Map;

/**
 * Fully extracted interface of GameModelImpl class. Handles various factors for the game, such as the game questions
 * and the users earnings.
 */
public interface GameModel {

    Map<String, List<GameQuestion>> getQuestionsForPracticeMode();

    Map<String, List<GameQuestion>> getBoardQuestions();

    void generateNewGameQuestionSet();

    GameQuestion getActiveQuestion();

    int numberOfQuestionsRemaining(Map<String, List<GameQuestion>> boardQuestions);

    int numberOfQuestionsRemaining();

    void loadSaveData(SaveData saveData);

    GameQuestion getNextActiveQuestion(GameQuestion question);


    int getValue();


    void fireValueChange();

    void fireQuestionBoardUpdate();

    void answerActive(boolean correct);

    void activateQuestion(GameQuestion question);


    void onActiveQuestionUpdate(ActiveQuestionObserver fn);

    void onQuestionBoardUpdate(QuestionBoardObserver fn);

    void onValueChange(ValueChangeObserver fn);


    void colourTextAreas(List<TextArea> textAreas, List<Boolean> corrects);
}
