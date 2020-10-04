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

package quinzical.impl.models.structures;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * SaveData manages the saving of the game data, consisting of both
 * the earnings and the board questions being used.
 */
public class SaveData implements Serializable {
    private Map<String, List<GameQuestion>> questionData;
    private int value;

    /**
     * Fetches the questions data that has been previously saved to the SaveData object
     * 
     * @return A map of the categories and questions that were saved.
     */
    public Map<String, List<GameQuestion>> getQuestionData() {
        return questionData;
    }

    /**
     * Saves the question data that is inputted, storing it in the object
     * and then returning a reference to this object.
     * 
     * @param questionData The Map containing the questions to be saved
     * @return The object that the data is being saved to
     */
    public SaveData setQuestionData(Map<String, List<GameQuestion>> questionData) {
        this.questionData = questionData;
        return this;
    }

    /**
     * Fetches the saved value, representing the earnings of the saved game.
     * 
     * @return The earnings that have been saved in this object
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value field, to store the earnings of the game to be saved.
     * 
     * @param value the value to be saved
     * @return The object that the value is being saved to
     */
    public SaveData setValue(int value) {
        this.value = value;
        return this;
    }
}
