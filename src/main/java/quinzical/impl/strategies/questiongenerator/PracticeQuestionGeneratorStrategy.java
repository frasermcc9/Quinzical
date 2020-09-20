package quinzical.impl.strategies.questiongenerator;

import quinzical.impl.models.structures.GameQuestion;
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Strategy for creating the practice set of questions.
 */
public class PracticeQuestionGeneratorStrategy implements QuestionGeneratorStrategy {

    private QuestionCollection questionCollection;

    public PracticeQuestionGeneratorStrategy(QuestionCollection questionCollection) {
        this.questionCollection = questionCollection;
    }

    @Override
    public Map<String, List<GameQuestion>> generateQuestions() {

        Map<String, List<GameQuestion>> questions = new HashMap<>();

        questionCollection.getQuestions().forEach((k, v) -> {
            List<GameQuestion> list = new ArrayList<>();
            v.forEach(q -> {
                GameQuestion question = new GameQuestion(q);
                question.setAnswerable(true);
                list.add(new GameQuestion(question));
            });
            questions.put(k, new ArrayList<>(list));
        });

        return questions;
    }
}
