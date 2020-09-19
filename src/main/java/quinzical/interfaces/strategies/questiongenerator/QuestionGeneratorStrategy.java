package quinzical.interfaces.strategies.questiongenerator;

import quinzical.impl.questionparser.Question;

import java.util.List;
import java.util.Map;

public interface QuestionGeneratorStrategy {
    Map<String, List<Question>> generateQuestions();
}
