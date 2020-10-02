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
public interface GameModel extends QuinzicalModel {

    Map<String, List<GameQuestion>> getBoardQuestions();

    void generateNewGameQuestionSet();

    int numberOfQuestionsRemaining(Map<String, List<GameQuestion>> boardQuestions);

    int numberOfQuestionsRemaining();

    void loadSaveData(SaveData saveData);

    GameQuestion getNextActiveQuestion(GameQuestion question);

    void answerActive(boolean correct);
    

    int getValue();


    void fireValueChange();

    void fireQuestionBoardUpdate();

    void onQuestionBoardUpdate(QuestionBoardObserver fn);

    void onValueChange(ValueChangeObserver fn);
    
    
}
