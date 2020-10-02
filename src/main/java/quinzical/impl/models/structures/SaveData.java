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

public class SaveData implements Serializable {
    private Map<String, List<GameQuestion>> questionData;
    private int value;

    public Map<String, List<GameQuestion>> getQuestionData() {
        return questionData;
    }

    public SaveData setQuestionData(Map<String, List<GameQuestion>> questionData) {
        this.questionData = questionData;
        return this;
    }

    public int getValue() {
        return value;
    }

    public SaveData setValue(int value) {
        this.value = value;
        return this;
    }
}
