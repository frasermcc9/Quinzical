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

package quinzical.interfaces.models.structures;

import java.util.List;

public interface AnalyticsEngineMutator extends AnalyticsEngineReader {
    int getQuestionsAnswered();

    void setQuestionsAnswered(int questionsAnswered);

    int getCategoriesAnswered();

    void setCategoriesAnswered(int categoriesAnswered);

    int getCorrectAnswers();

    void setCorrectAnswers(int correctAnswers);

    void answerQuestion(String category, boolean correct);

    List<String> getMostAnsweredCategories();
}
