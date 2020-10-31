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

package quinzical.impl.controllers.game;

import com.google.inject.Inject;
import quinzical.impl.constants.GameScene;
import quinzical.impl.controllers.AbstractSelectorController;
import quinzical.interfaces.models.GameModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the category selector screen, where the user selects what 
 * categories they want questions from
 */
public class CategorySelectorController extends AbstractSelectorController {

    private final List<String> selectedCategories = new ArrayList<>();

    @Inject
    private GameModel gameModel;

    /**
     * Gets the gameModel
     */
    @Override
    protected final GameModel getModel() {
        return this.gameModel;
    }

    /**
     * Generates a set of questions according to the selected categories and then
     * sets the scene to the question board section
     */
    @Override
    protected final void generateQuestionsAndProgress() {
        gameModel.generateGameQuestionSetFromCategories(selectedCategories);
        sceneHandler.setActiveScene(GameScene.GAME);
    }

    /**
     * Updates the amount of categories selected label and sets the ok 
     * button to disabled if the amount is not 5
     */
    @Override
    protected final void modifyCounterLabel() {
        this.lblSelected.setText("Selected: " + selectedCategories.size() + "/5");
        btnOk.setDisable(selectedCategories.size() != 5);
    }

    /**
     * Gets the list of categories that are currently selected
     */
    @Override
    protected final List<String> getSelectedCategories() {
        return this.selectedCategories;
    }
}

