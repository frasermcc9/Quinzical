package quinzical.interfaces.strategies.questiongenerator;

import quinzical.impl.models.structures.GameQuestion;

import java.util.List;
import java.util.Map;

public interface QuestionGeneratorStrategy {
    Map<String, List<GameQuestion>> generateQuestions();
}
