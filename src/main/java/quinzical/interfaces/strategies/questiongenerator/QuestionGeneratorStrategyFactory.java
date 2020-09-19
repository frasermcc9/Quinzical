package quinzical.interfaces.strategies.questiongenerator;

public interface QuestionGeneratorStrategyFactory {

    QuestionGeneratorStrategy createStrategy();

    QuestionGeneratorStrategy createPracticeQuestionStrategy();
    
}
