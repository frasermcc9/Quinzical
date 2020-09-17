package quinzical.interfaces.models;

import quinzical.impl.questionparser.Question;

import java.util.List;
import java.util.Map;

public interface QuestionCollection {
    Map<String, List<Question>> getQuestions();
}
