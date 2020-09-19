package quinzical.impl.strategies.questiongenerator;

import quinzical.impl.models.structures.GameQuestion;
import quinzical.impl.questionparser.Question;
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategy;

import java.util.*;

public class GameQuestionGeneratorStrategy implements QuestionGeneratorStrategy {

    private final QuestionCollection questionCollection;

    public GameQuestionGeneratorStrategy(QuestionCollection questionCollection) {
        this.questionCollection = questionCollection;
    }

    @Override
    public Map<String, List<GameQuestion>> generateQuestions() {
        Map<String, List<Question>> questions = questionCollection.getQuestions();

        ArrayList<String> allCategories = new ArrayList<>(questions.keySet());
        Collections.shuffle(allCategories);

        List<String> chosen = allCategories.subList(0, 5);

        Map<String, List<GameQuestion>> boardQuestions = new LinkedHashMap<>();
        chosen.forEach(e -> boardQuestions.put(e, new ArrayList<>()));
        boardQuestions.forEach((k, v) -> {
            List<Question> availableQuestions = new ArrayList<>(questions.get(k));
            Collections.shuffle(availableQuestions);

            for (int i = 0; i < 5; i++) {
                GameQuestion q = new GameQuestion(availableQuestions.get(i));
                q.setValue((i + 1) * 100);
                if (i == 0) {
                    q.setAnswerable(true);
                }

                v.add(q);
            }

        });

        return boardQuestions;
    }
}
