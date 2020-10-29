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

package quinzical.impl.controllers;

import com.google.inject.Inject;
import quinzical.impl.constants.GameScene;
import quinzical.impl.util.questionparser.Question;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.PracticeModel;

import java.util.ArrayList;
import java.util.List;


public class PracticeSelectorController extends AbstractSelectorController {

    private final List<String> selectedCategories = new ArrayList<>();

    @Inject
    private GameModel gameModel;

    @Inject
    private PracticeModel practiceModel;

    @Override
    protected void generateQuestionsAndProgress() {
        practiceModel.setCategories(selectedCategories);
        Question question = practiceModel.getRandomQuestion();
        practiceModel.activateQuestion(question);
        sceneHandler.setActiveScene(GameScene.PRACTICE_QUESTION);
    }

    /**
     * Gets the list of currently selected categories
     */
    @Override
    protected List<String> getSelectedCategories() {
        return this.selectedCategories;
    }

    @Override
    protected GameModel getModel() {
        return this.gameModel;
    }

    /**
     * Updates the counter for the total selected categories
     */
    @Override
    protected void modifyCounterLabel() {
        this.lblSelected.setText("Selected: " + selectedCategories.size());
        btnOk.setDisable(selectedCategories.size() < 1);
    }


}

