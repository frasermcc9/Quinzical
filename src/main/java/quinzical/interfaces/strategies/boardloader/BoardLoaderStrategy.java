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

package quinzical.interfaces.strategies.boardloader;

import javafx.scene.layout.Pane;

public interface BoardLoaderStrategy {

    /**
     * Inject the components necessary to create the board.
     *
     * @param header  The header pane that will contain the category headings
     * @param content The content pane that will contain the buttons
     * @return this
     */
    BoardLoaderStrategy injectComponents(Pane header, Pane content);

    /**
     * Loads the components into the injected components. The
     * {@link BoardLoaderStrategy#injectComponents(Pane, Pane)} method is
     * required in order to use this method.
     */
    void loadBoard();

}
