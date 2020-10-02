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
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategy;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategyFactory;

/**
 * Factory for generating strategies that pull question sets from the database.
 */
public class QuestionGeneratorStrategyFactoryImpl implements QuestionGeneratorStrategyFactory {

    @Inject
    private QuestionCollection questionCollection;

    /**
     * Creates the strategy for loading questions for a regular game - i.e. 5
     * categories with 5 questions each.
     */
    @Override
    public QuestionGeneratorStrategy createGameQuestionStrategy() {
        return new GameQuestionGeneratorStrategy(questionCollection);
    }

    /**
     * Creates the strategy for loading questions for a practice game.
     */
    @Override
    public QuestionGeneratorStrategy createPracticeQuestionStrategy() {
        return new PracticeQuestionGeneratorStrategy(questionCollection);
    }
}
