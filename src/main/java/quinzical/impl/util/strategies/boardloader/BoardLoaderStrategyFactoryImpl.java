// Copyright 2020 Fraser McCallum and Braden Palmer
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//  
//     http://www.apache.org/licenses/LICENSE-2.0
//  
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
