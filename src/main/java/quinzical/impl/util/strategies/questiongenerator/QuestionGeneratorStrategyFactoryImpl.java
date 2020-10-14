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
import com.google.inject.Provider;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.impl.util.questionparser.Question;
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategy;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategyFactory;

import java.util.*;

/**
 * Factory for generating strategies that pull question sets from the database.
 */
public class QuestionGeneratorStrategyFactoryImpl implements QuestionGeneratorStrategyFactory {

    private final Provider<GameQuestionGeneratorStrategy> gameQuestionGeneratorStrategyProvider;
    private final Provider<PracticeQuestionGeneratorStrategy> practiceQuestionGeneratorStrategyProvider;
    private final Provider<SelectedCategoryGeneratorStrategy> selectedCategoryGeneratorStrategyProvider;

    @Inject
    public QuestionGeneratorStrategyFactoryImpl(final Provider<GameQuestionGeneratorStrategy> gameQuestionGeneratorStrategyProvider,
                                                final Provider<PracticeQuestionGeneratorStrategy> practiceQuestionGeneratorStrategyProvider,
                                                final Provider<SelectedCategoryGeneratorStrategy> selectedCategoryGeneratorStrategyProvider) {
        this.gameQuestionGeneratorStrategyProvider = gameQuestionGeneratorStrategyProvider;
        this.practiceQuestionGeneratorStrategyProvider = practiceQuestionGeneratorStrategyProvider;
        this.selectedCategoryGeneratorStrategyProvider = selectedCategoryGeneratorStrategyProvider;
    }

    /**
     * Creates the strategy for loading questions for a regular game - i.e. 5 categories with 5 questions each.
     */
    @Override
    public QuestionGeneratorStrategy createGameQuestionStrategy() {
        return gameQuestionGeneratorStrategyProvider.get();
    }

    /**
     * Creates the strategy for loading questions for a practice game.
     */
    @Override
    public QuestionGeneratorStrategy createPracticeQuestionStrategy() {
        return practiceQuestionGeneratorStrategyProvider.get();

    }

    @Override
    public QuestionGeneratorStrategy createSelectedCategorySelection(String[] categories) {
        SelectedCategoryGeneratorStrategy strategy = selectedCategoryGeneratorStrategyProvider.get();
        strategy.setCategories(List.of(categories));
        return strategy;
    }

    @Override
    public QuestionGeneratorStrategy createSelectedCategorySelection(List<String> categories) {
        SelectedCategoryGeneratorStrategy strategy = selectedCategoryGeneratorStrategyProvider.get();
        strategy.setCategories(categories);
        return strategy;
    }
}

class PracticeQuestionGeneratorStrategy implements QuestionGeneratorStrategy {

    /**
     * Inject the questionCollection into the strategy
     */
    @Inject
    private QuestionCollection questionCollection;

    /**
     * Generates questions for practice mode
     */
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

class GameQuestionGeneratorStrategy implements QuestionGeneratorStrategy {

    /**
     * Inject the questionCollection into the strategy
     */
    @Inject
    private QuestionCollection questionCollection;

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

class SelectedCategoryGeneratorStrategy implements QuestionGeneratorStrategy {

    /**
     * Inject the questionCollection into the strategy
     */
    @Inject
    private QuestionCollection questionCollection;

    private List<String> categories;

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    @Override
    public Map<String, List<GameQuestion>> generateQuestions() {
        Map<String, List<Question>> questions = questionCollection.getQuestions();

        Map<String, List<GameQuestion>> questionBoard = new LinkedHashMap<>();

        for (String category : categories) {
            List<Question> availableQuestions = new ArrayList<>(questions.get(category));
            List<GameQuestion> categoryQuestions = new ArrayList<>();

            Collections.shuffle(availableQuestions);

            for (int i = 0; i < 5; i++) {
                GameQuestion q = new GameQuestion(availableQuestions.get(i));
                q.setValue((i + 1) * 100);
                if (i == 0) {
                    q.setAnswerable(true);
                }
                categoryQuestions.add(q);
            }
            questionBoard.put(category, categoryQuestions);
        }

        return questionBoard;

    }
}
