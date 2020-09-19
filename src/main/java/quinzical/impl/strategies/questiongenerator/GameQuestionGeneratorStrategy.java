package quinzical.impl.strategies.questiongenerator;

import quinzical.impl.questionparser.Question;
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategy;

import java.util.*;

public class GameQuestionGeneratorStrategy implements QuestionGeneratorStrategy {

    private QuestionCollection questionCollection;

    public GameQuestionGeneratorStrategy(QuestionCollection questionCollection) {
        this.questionCollection = questionCollection;
    }
    
    @Override
    public Map<String, List<Question>> generateQuestions() {
        Map<String, List<Question>> questions = questionCollection.getQuestions();

        ArrayList<String> allCategories = new ArrayList<>(questions.keySet());
        Collections.shuffle(new ArrayList<>(allCategories));

        List<String> chosen = allCategories.subList(0, 5);

        Map<String, List<Question>> boardQuestions = new HashMap<>();
        chosen.forEach(e -> boardQuestions.put(e, new ArrayList<>()));
        boardQuestions.forEach((k, v) -> {
            List<Question> availableQuestions = new ArrayList<>(questions.get(k));
            Collections.shuffle(availableQuestions);
            v.addAll(availableQuestions.subList(0, 5));
        });

        return boardQuestions;
    }
}
