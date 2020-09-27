package quinzical.interfaces.models;

import quinzical.impl.models.structures.GameQuestion;
import quinzical.interfaces.events.ActiveQuestionObserver;
import quinzical.interfaces.events.QuestionObserver;
import quinzical.interfaces.events.ValueChangeObserver;

import java.util.List;
import java.util.Map;

public interface GameModel {

    Map<String, List<GameQuestion>> getQuestionsForPracticeMode();

    Map<String, List<GameQuestion>> getBoardQuestions();

    void generateNewGameQuestionSet();

    GameQuestion getActiveQuestion();

    GameQuestion getNextActiveQuestion(GameQuestion question);

    
    int getValue();


    void fireValueChange();

    void fireQuestionsUpdate();

    void answerActive(boolean correct);

    void activateQuestion(GameQuestion question);


    void onActiveQuestionUpdate(ActiveQuestionObserver fn);

    void onQuestionsUpdate(QuestionObserver fn);

    void onValueChange(ValueChangeObserver fn);


}
