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

import quinzical.impl.models.structures.GameQuestion;
import quinzical.impl.util.questionparser.Question;
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategy;

import java.util.*;

/**
 * Strategy for creating the question set for a game,
 */
public class GameQuestionGeneratorStrategy implements QuestionGeneratorStrategy {

    private final QuestionCollection questionCollection;

    /**
     * Constructs the strategy.
     *
     * @param questionCollection the full question database
     */
    public GameQuestionGeneratorStrategy(QuestionCollection questionCollection) {
        this.questionCollection = questionCollection;
    }

    /**
     * Generates a question set for the game.
     */
    @Override
    public Map<String, List<GameQuestion>> generateQuestions() {
        Map<String, List<Question>> questions = questionCollection.getQuestions();

        // Shuffle the list of categories and pick 5.
        ArrayList<String> allCategories = new ArrayList<>(questions.keySet());
        Collections.shuffle(allCategories);
        List<String> chosen = allCategories.subList(0, 5);

        // Add the 5 chosen categories to a new map.
        Map<String, List<GameQuestion>> boardQuestions = new LinkedHashMap<>();
        chosen.forEach(e -> boardQuestions.put(e, new ArrayList<>()));

        // For each of the categories, pick 5 questions.
        boardQuestions.forEach((k, v) -> {
            // Get all questions for category and shuffle.
            List<Question> availableQuestions = new ArrayList<>(questions.get(k));
            Collections.shuffle(availableQuestions);

            // Pick the first 5 questions in the list and assign it a value. If
            // its the first question of the category, set it as answerable.
            for (int i = 0; i < 5; i++) {
                GameQuestion q = new GameQuestion(availableQuestions.get(i));
                q.setValue((i + 1) * 100);
                if (i == 0) {
                    q.setAnswerable(true);
                }

                v.add(q);
            }

        });

        return boardQuestions;
    }
}
