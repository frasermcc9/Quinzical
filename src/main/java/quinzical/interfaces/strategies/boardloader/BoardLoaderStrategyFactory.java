package quinzical.interfaces.strategies.boardloader;

public interface BoardLoaderStrategyFactory {

    /**
     * Create the strategy for loading the board.
     */
    BoardLoaderStrategy createStrategy();
}
