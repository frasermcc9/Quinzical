package quinzical.interfaces.strategies.questiongenerator;

public interface QuestionGeneratorStrategyFactory {

    QuestionGeneratorStrategy createGameQuestionStratgey();

    QuestionGeneratorStrategy createPracticeQuestionStrategy();
    
}
