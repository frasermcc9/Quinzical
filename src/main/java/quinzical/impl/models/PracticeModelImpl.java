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

import java.util.List;

/**
 * The model for the practice mode, providing the ability to get new questions and set a certain question as the current
 * "active" question.
 */
@Singleton
public class PracticeModelImpl extends AbstractGameModel implements PracticeModel {

    private List<String> categories;

    @Override
    public final void setCategories(final List<String> categories) {
        this.categories = categories;
    }

    /**
     * Gets a random question from the given category
     *
     * @param category - The category that the random question will be got for.
     * @return - A random question from the given category.
     */
    @Override
    public final Question getRandomQuestion(final String category) {
        final List<GameQuestion> questions =
            questionGeneratorStrategyFactory.createPracticeQuestionStrategy().generateQuestions().get(category);
        return questions.get((int) (Math.random() * questions.size()));
    }

    @Override
    public final Question getRandomQuestion() {
        final String selected = categories.get((int) Math.floor(Math.random() * categories.size()));
        return getRandomQuestion(selected);
    }


    /**
     * Sets the given question as the active question, which is the question being currently answered.
     *
     * @param question - The question to be set as the active question.
     */
    @Override
    public final void activateQuestion(final Question question) {
        activeQuestion = new GameQuestion(question);
        fireActiveQuestionUpdate();
    }
}
