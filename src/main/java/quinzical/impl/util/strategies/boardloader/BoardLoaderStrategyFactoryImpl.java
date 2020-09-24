package quinzical.impl.util.strategies.boardloader;

import com.google.inject.Inject;

import quinzical.interfaces.factories.BoardComponentFactory;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.strategies.boardloader.BoardLoaderStrategy;
import quinzical.interfaces.strategies.boardloader.BoardLoaderStrategyFactory;

/**
 * Factory for creating strategies to load the board components.
 */
public class BoardLoaderStrategyFactoryImpl implements BoardLoaderStrategyFactory {

    @Inject
    BoardComponentFactory boardComponentFactory;

    @Inject
    private GameModel gameModel;

    /**
     * Create the strategy
     */
    @Override
    public BoardLoaderStrategy createStrategy() {

        return new DefaultBoardLoaderStrategy(boardComponentFactory, gameModel);
    }
}
