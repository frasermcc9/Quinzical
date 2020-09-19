package quinzical.impl.strategies.boardloader;

import com.google.inject.Inject;
import quinzical.interfaces.factories.BoardComponentFactory;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.strategies.boardloader.BoardLoaderStrategy;
import quinzical.interfaces.strategies.boardloader.BoardLoaderStrategyFactory;

public class BoardLoaderStrategyFactoryImpl implements BoardLoaderStrategyFactory {

    @Inject
    BoardComponentFactory boardComponentFactory;

    @Inject
    private GameModel gameModel;
    
    @Override
    public BoardLoaderStrategy createStrategy() {
        return new DefaultBoardLoaderStrategy(boardComponentFactory, gameModel);
    }
}
