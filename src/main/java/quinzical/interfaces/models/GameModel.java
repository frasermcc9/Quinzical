package quinzical.interfaces.models;

import quinzical.impl.models.structures.GameQuestion;

import java.util.List;
import java.util.Map;

public interface GameModel {
    Map<String, List<GameQuestion>> getBoardQuestions();

    void generateNewGameQuestionSet();

    void activateQuestion(String category, int questionIdx);
}
