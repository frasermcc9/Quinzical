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

package quinzical.interfaces.strategies.questiongenerator;

import java.util.List;

/**
 * Interface for QuestionGeneratorStrategyFactoryImpl. Handles the generation of QuestionGeneratorStrategy and
 * PracticeQuestionGeneratorStrategy objects.
 */
public interface QuestionGeneratorStrategyFactory {

    /**
     * Create a question set for a normal local game, from installed questions
     *
     * @return a question generator strategy conforming to the above behaviour.
     */
    QuestionGeneratorStrategy createGameQuestionStrategy();

    /**
     * Create a question set for a practice game, from installed questions.
     *
     * @return a question generator strategy conforming to the above behaviour.
     */
    QuestionGeneratorStrategy createPracticeQuestionStrategy();

    /**
     * Create a question set for a normal local game, from an array of given categories.
     *
     * @return a question generator strategy conforming to the above behaviour.
     */
    QuestionGeneratorStrategy createSelectedCategoryStrategy(String[] categories);

    /**
     * Create a question set for a normal local game, from a list of given categories.
     *
     * @return a question generator strategy conforming to the above behaviour.
     */
    QuestionGeneratorStrategy createSelectedCategoryStrategy(List<String> categories);

    /**
     * Create a question set for an international game. Slow method should be called asynchronously (not on main
     * thread).
     *
     * @return a question generator strategy conforming to the above behaviour.
     */
    QuestionGeneratorStrategy createInternationalQuestionStrategy();
}
