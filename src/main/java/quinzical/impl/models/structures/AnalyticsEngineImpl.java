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
    public int getQuestionsAnswered() {
        return questionsAnswered / 2;
    }

    @Override
    public void setQuestionsAnswered(int questionsAnswered) {
        this.questionsAnswered = questionsAnswered;
    }

    public double getCorrectRatioForCategory(String category) {
        return (double) getQuestionsCorrectForCategory(category) / getQuestionsAnsweredForCategory(category);
    }

    public int getQuestionsAnsweredForCategory(String category) {
        return this.answeredByCategory.get(category) / 2;
    }

    public int getQuestionsCorrectForCategory(String category) {
        return this.correctByCategory.get(category);
    }

    @Override
    public int getCategoriesAnswered() {
        return categoriesAnswered;
    }

    @Override
    public void setCategoriesAnswered(int categoriesAnswered) {
        this.categoriesAnswered = categoriesAnswered;
    }

    @Override
    public int getCorrectAnswers() {
        return correctAnswers;
    }

    @Override
    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    @Override
    public void answerQuestion(String category, boolean correct) {
        int addToAnswered = 1;
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
    public List<String> getMostAnsweredCategories() {
        return answeredByCategory.keySet()
            .stream()
            .sorted(Comparator.comparingInt(answeredByCategory::get).reversed())
            .limit(5)
            .collect(Collectors.toList());
    }

    public List<String> getQuestionsAnsweredByCategory(List<String> categories) {
        return categories
            .stream()
            .map(this::getQuestionsAnsweredForCategory)
            .map(Object::toString)
            .collect(Collectors.toList());
    }

    @Override
    public List<String> getMostChallengingCategories() {
        return answeredByCategory.keySet()
            .stream()
            .sorted(Comparator.comparingDouble(this::getCorrectRatioForCategory))
            .limit(5)
            .collect(Collectors.toList());
    }

    @Override
    public List<String> getCorrectRatiosOfCategories(List<String> categories) {
        return categories
            .stream()
            .map(this::getCorrectRatioForCategory)
            .map(v -> String.format("%.2g", v))
            .collect(Collectors.toList());
    }
}