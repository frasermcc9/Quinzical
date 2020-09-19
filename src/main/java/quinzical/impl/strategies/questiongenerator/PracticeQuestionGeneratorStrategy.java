package quinzical.impl.strategies.questiongenerator;

import quinzical.impl.questionparser.Question;
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PracticeQuestionGeneratorStrategy implements QuestionGeneratorStrategy {

    private QuestionCollection questionCollection;

    public PracticeQuestionGeneratorStrategy(QuestionCollection questionCollection) {
        this.questionCollection = questionCollection;
    }

    @Override
    public Map<String, List<Question>> generateQuestions() {

        Map<String, List<Question>> questions = new HashMap<>();

        questionCollection.getQuestions().forEach((k, v) -> {
            questions.put(k, new ArrayList<>(v));
        });

        return questions;
    }
}
