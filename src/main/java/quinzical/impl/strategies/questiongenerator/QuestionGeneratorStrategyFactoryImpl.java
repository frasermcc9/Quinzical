package quinzical.impl.strategies.questiongenerator;

import com.google.inject.Inject;
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategy;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategyFactory;

public class QuestionGeneratorStrategyFactoryImpl implements QuestionGeneratorStrategyFactory {

    @Inject
    private QuestionCollection questionCollection;
    
    @Override
    public QuestionGeneratorStrategy createStrategy() {
        return new GameQuestionGeneratorStrategy(questionCollection);
    }

    @Override
    public QuestionGeneratorStrategy createPracticeQuestionStrategy() {
        return new PracticeQuestionGeneratorStrategy(questionCollection);
    }
}
