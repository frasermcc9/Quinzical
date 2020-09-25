package quinzical.interfaces.models;

import quinzical.impl.models.structures.GameQuestion;
import quinzical.interfaces.events.ActiveQuestionObserver;
import quinzical.interfaces.events.QuestionObserver;

import java.util.List;
import java.util.Map;

public interface GameModel {
    Map<String, List<GameQuestion>> getBoardQuestions();

    void generateNewGameQuestionSet();

    void activateQuestion(GameQuestion question);

    GameQuestion getActiveQuestion();

    void onQuestionsUpdate(QuestionObserver fn);

    void fireQuestionsUpdate();

    void onActiveQuestionUpdate(ActiveQuestionObserver fn);

    void answerActive();
}
