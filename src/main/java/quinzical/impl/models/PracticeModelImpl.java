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

package quinzical.impl.models;

import com.google.inject.Singleton;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.impl.util.questionparser.Question;
import quinzical.interfaces.models.PracticeModel;

import java.util.ArrayList;
import java.util.List;


@Singleton
public class PracticeModelImpl extends AbstractGameModel implements PracticeModel {

    @Override
    public Question getRandomQuestion(String category) {
        List<GameQuestion> questions =
            questionGeneratorStrategyFactory.createPracticeQuestionStrategy().generateQuestions().get(category);
        return questions.get((int) (Math.random() * questions.size()));
    }

    @Override
    public List<String> getCategories() {
        return new ArrayList<>(questionGeneratorStrategyFactory.createPracticeQuestionStrategy().generateQuestions().keySet());
    }

    @Override
    public void activateQuestion(Question question) {
        activeQuestion = new GameQuestion(question);
        fireActiveQuestionUpdate();
    }
}
