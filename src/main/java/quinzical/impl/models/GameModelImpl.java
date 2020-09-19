package quinzical.impl.models;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import quinzical.impl.questionparser.Question;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategyFactory;

import java.util.List;
import java.util.Map;

@Singleton
public class GameModelImpl implements GameModel {

    @Inject
    private QuestionGeneratorStrategyFactory questionGeneratorStrategyFactory;

    private Map<String, List<Question>> boardQuestions;

    public void generateNewGameQuestionSet() {
        this.boardQuestions = questionGeneratorStrategyFactory.createStrategy().generateQuestions();
    }

}
