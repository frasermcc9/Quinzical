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

package quinzical.impl.util.strategies.questiongenerator;

import com.google.inject.Inject;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Strategy for creating the practice set of questions.
 */
public class PracticeQuestionGeneratorStrategy implements QuestionGeneratorStrategy {

    /**
     * Inject the questionCollection into the strategy
     */
    @Inject
    private QuestionCollection questionCollection;

    @Override
    public Map<String, List<GameQuestion>> generateQuestions() {

        Map<String, List<GameQuestion>> questions = new HashMap<>();

        questionCollection.getQuestions().forEach((k, v) -> {
            List<GameQuestion> list = new ArrayList<>();
            v.forEach(q -> {
                GameQuestion question = new GameQuestion(q);
                question.setAnswerable(true);
                list.add(new GameQuestion(question));
            });
            questions.put(k, new ArrayList<>(list));
        });

        return questions;
    }
}
