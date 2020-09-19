package quinzical.interfaces.strategies.boardloader;

import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategy;

public interface BoardLoaderStrategyFactory {
    BoardLoaderStrategy createStrategy();
}
