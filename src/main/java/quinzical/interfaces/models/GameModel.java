package quinzical.interfaces.models;

import quinzical.impl.models.structures.GameQuestion;
import quinzical.impl.models.structures.SaveData;
import quinzical.interfaces.events.ActiveQuestionObserver;
import quinzical.interfaces.events.QuestionBoardObserver;
import quinzical.interfaces.events.ValueChangeObserver;

import java.util.List;
import java.util.Map;

public interface GameModel {

    Map<String, List<GameQuestion>> getQuestionsForPracticeMode();

    Map<String, List<GameQuestion>> getBoardQuestions();

    void generateNewGameQuestionSet();

    GameQuestion getActiveQuestion();

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


}
