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

package quinzical.interfaces.models;

import javafx.scene.control.TextArea;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.interfaces.events.ActiveQuestionObserver;

import java.util.List;

/**
 * Interface used for the PracticeModel and GameModel interfaces.
 * handles the shared uses in both the gameModel and the practiceModel
 * such as the activation of questions and the colouring of text areas.
 */
public interface QuinzicalModel {
    GameQuestion getActiveQuestion();

    void activateQuestion(GameQuestion question);

    void onActiveQuestionUpdate(ActiveQuestionObserver fn);

    void colourTextAreas(List<TextArea> textAreas, List<Boolean> corrects);
    
}
