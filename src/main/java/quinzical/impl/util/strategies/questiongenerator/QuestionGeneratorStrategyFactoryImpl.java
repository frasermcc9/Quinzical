package quinzical.impl.strategies.questiongenerator;

import com.google.inject.Inject;
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategy;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategyFactory;

/**
 * Factory for generating strategies that pull question sets from the database.
 */
public class QuestionGeneratorStrategyFactoryImpl implements QuestionGeneratorStrategyFactory {

    @Inject
    private QuestionCollection questionCollection;

    /**
     * Creates the strategy for loading questions for a regular game - i.e. 5
     * categories with 5 questions each.
     */
    @Override
    public QuestionGeneratorStrategy createGameQuestionStratgey() {
        return new GameQuestionGeneratorStrategy(questionCollection);
    }

    /**
     * Creates the strategy for loading questions for a practice game.
     */
    @Override
    public QuestionGeneratorStrategy createPracticeQuestionStrategy() {
        return new PracticeQuestionGeneratorStrategy(questionCollection);
    }
}
