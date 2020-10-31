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

import javafx.util.Pair;
import quinzical.interfaces.models.structures.AnalyticsEngineMutator;
import quinzical.interfaces.models.structures.AnalyticsEngineReader;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyticsEngineImpl implements Serializable, AnalyticsEngineMutator, AnalyticsEngineReader {
    private final Map<String, Integer> correctByCategory;
    private final Map<String, Integer> answeredByCategory;

    private int questionsAnswered = 0;
    private int categoriesAnswered = 0;
    private int correctAnswers = 0;

    public AnalyticsEngineImpl() {
        correctByCategory = new HashMap<>();
        answeredByCategory = new HashMap<>();
    }

    @Override
    public final void resetData() {
        correctByCategory.clear();
        answeredByCategory.clear();
        setCategoriesAnswered(0);
        setQuestionsAnswered(0);
        setCorrectAnswers(0);
    }

    @Override
    public final int getQuestionsAnswered() {
        return questionsAnswered / 2;
    }

    @Override
    public final void setQuestionsAnswered(final int questionsAnswered) {
        this.questionsAnswered = questionsAnswered;
    }

    public final double getCorrectRatioForCategory(final String category) {
        return (double) getQuestionsCorrectForCategory(category) / getQuestionsAnsweredForCategory(category);
    }

    public final int getQuestionsAnsweredForCategory(final String category) {
        final Integer value = this.answeredByCategory.get(category);
        if (value == null) return 0;
        return value / 2;
    }

    public final int getQuestionsCorrectForCategory(final String category) {
        final Integer value = this.correctByCategory.get(category);
        if (value == null) return 0;
        return value;
    }

    public final int getQuestionsWrongForCategory(final String category) {
        return getQuestionsAnsweredForCategory(category) - getQuestionsCorrectForCategory(category);
    }

    @Override
    public final int getCategoriesAnswered() {
        return categoriesAnswered;
    }

    @Override
    public final void setCategoriesAnswered(final int categoriesAnswered) {
        this.categoriesAnswered = categoriesAnswered;
    }

    @Override
    public final int getCorrectAnswers() {
        return correctAnswers;
    }

    @Override
    public final void setCorrectAnswers(final int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    @Override
    public final void answerQuestion(final String category, final boolean correct) {
        final int addToAnswered = 1;
        int addToCorrect = 0;
        if (correct) {
            addToCorrect = 1;
        }
        questionsAnswered += addToAnswered;
        correctAnswers += addToCorrect;

        correctByCategory.putIfAbsent(category, 0);
        correctByCategory.put(category, correctByCategory.get(category) + addToCorrect);
        answeredByCategory.putIfAbsent(category, 0);
        answeredByCategory.put(category, answeredByCategory.get(category) + addToAnswered);
    }

    @Override
    public final List<String> getMostAnsweredCategories() {
        return answeredByCategory.keySet()
            .stream()
            .sorted(Comparator.comparingInt(answeredByCategory::get).reversed())
            .limit(5)
            .collect(Collectors.toList());
    }

    public final List<String> getQuestionsAnsweredByCategory(final List<String> categories) {
        return categories
            .stream()
            .map(this::getQuestionsAnsweredForCategory)
            .map(Object::toString)
            .collect(Collectors.toList());
    }

    @Override
    public final List<String> getMostChallengingCategories() {
        return answeredByCategory.keySet()
            .stream()
            .sorted(Comparator.comparingDouble(this::getCorrectRatioForCategory))
            .limit(5)
            .collect(Collectors.toList());
    }

    @Override
    public final List<String> getCorrectRatiosOfCategories(final List<String> categories) {
        return categories
            .stream()
            .map(this::getCorrectRatioForCategory)
            .map(v -> String.format("%.2g", v))
            .collect(Collectors.toList());
    }

    public final List<Pair<String, Integer>> getPairsForIncorrectAnswers(final int points) {
        return answeredByCategory.keySet()
            .stream()
            .sorted(Comparator.comparingInt(this::getQuestionsWrongForCategory).reversed())
            .map(s -> new Pair<>(s, getQuestionsWrongForCategory(s)))
            .limit(points)
            .collect(Collectors.toList());
    }

    public final List<Pair<String, Integer>> getPairsForIncorrectAnswers() {
        return answeredByCategory.keySet()
            .stream()
            .sorted(Comparator.comparingInt(this::getQuestionsWrongForCategory).reversed())
            .map(s -> new Pair<>(s, getQuestionsWrongForCategory(s)))
            .collect(Collectors.toList());
    }

    @Override
    public final List<Pair<String, Integer>> getPairsForCorrectAnswers(final int points) {
        return answeredByCategory.keySet()
            .stream()
            .sorted(Comparator.comparingInt(this::getQuestionsCorrectForCategory).reversed())
            .map(s -> new Pair<>(s, getQuestionsCorrectForCategory(s)))
            .limit(points)
            .collect(Collectors.toList());
    }

    @Override
    public final List<Pair<String, Integer>> getPairsForCorrectAnswers() {
        return answeredByCategory.keySet()
            .stream()
            .sorted(Comparator.comparingInt(this::getQuestionsCorrectForCategory).reversed())
            .map(s -> new Pair<>(s, getQuestionsCorrectForCategory(s)))
            .collect(Collectors.toList());
    }
}
