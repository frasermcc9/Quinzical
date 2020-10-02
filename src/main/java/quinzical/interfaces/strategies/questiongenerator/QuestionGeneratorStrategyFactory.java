package quinzical.interfaces.strategies.questiongenerator;

public interface QuestionGeneratorStrategyFactory {

    QuestionGeneratorStrategy createGameQuestionStrategy();

    QuestionGeneratorStrategy createPracticeQuestionStrategy();
    
}
