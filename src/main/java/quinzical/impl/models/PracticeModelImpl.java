package quinzical.impl.models;

import com.google.inject.Singleton;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.impl.util.questionparser.Question;
import quinzical.interfaces.models.PracticeModel;

import java.util.ArrayList;
import java.util.List;


@Singleton
public class PracticeModelImpl extends AbstractGameModel implements PracticeModel {

    @Override
    public Question getRandomQuestion(String category) {
        List<GameQuestion> questions =
            questionGeneratorStrategyFactory.createPracticeQuestionStrategy().generateQuestions().get(category);
        return questions.get((int) (Math.random() * questions.size()));
    }

    @Override
    public List<String> getCategories() {
        return new ArrayList<>(questionGeneratorStrategyFactory.createPracticeQuestionStrategy().generateQuestions().keySet());
    }

    @Override
    public void activateQuestion(Question question) {
        activeQuestion = new GameQuestion(question);
        fireActiveQuestionUpdate();
    }
}
