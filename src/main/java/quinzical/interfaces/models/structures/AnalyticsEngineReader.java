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

import javafx.util.Pair;

import java.util.List;

/**
 * Analytics engine reader contains methods for reading data from the analytics engine. This interface should be used if
 * data only needs to be read from the analytics engine, not written.
 *
 * @author Fraser McCallum
 * @see AnalyticsEngineMutator for when data needs to be written.
 * @see quinzical.impl.models.structures.AnalyticsEngineImpl
 * @since 1.1
 */
public interface AnalyticsEngineReader {
    /**
     * Gets the players most answered categories. No more than five categories will be returned.
     *
     * @return the users most answered categories, in order.
     */
    List<String> getMostAnsweredCategories();

    /**
     * Gets the players most challenging categories. No more than five categories will be returned. Challenging
     * categories are determined by the ratio of correct to incorrect answers. The lower this ratio, the more
     * challenging.
     *
     * @return the users most challenging categories, in order.
     */
    List<String> getMostChallengingCategories();

    /**
     * Gets the correct ratios of categories (i.e. the ratio of correct to incorrect answers) for a list of categories.
     *
     * @param categories the categories to get the ratios for
     * @return a list numerical data containing the ratios of correct to incorrect answers for the given categories, in
     * string format. The list will be in the same order as the categories given.
     */
    List<String> getCorrectRatiosOfCategories(List<String> categories);

    /**
     * Gets the number of questions answered for a given list of categories.
     *
     * @param categories the categories to get the data for
     * @return a list containing the number of questions answered for each category, in the same order as the given
     * list.
     */
    List<String> getQuestionsAnsweredByCategory(List<String> categories);

    /**
     * Gets pair data for a number of categories. The pair contains (category name, number of questions wrong). This
     * method will return the pairs with the highest number wrong. The maximum is given by the the input argument.
     *
     * @param points the maximum number of data points to return
     * @return list of the users most wrong categories, and the amount wrong
     * @see #getPairsForIncorrectAnswers()
     */
    List<Pair<String, Integer>> getPairsForIncorrectAnswers(int points);

    /**
     * Gets pair data for a number of categories. The pair contains (category name, number of questions right). This
     * method will return the pairs with the highest number right. The maximum is given by the the input argument.
     *
     * @param points the maximum number of data points to return
     * @return list of pairs that contain the categories with the most correct answers.
     * @see #getPairsForCorrectAnswers()
     */
    List<Pair<String, Integer>> getPairsForCorrectAnswers(int points);

    /**
     * Identical behaviour to {@link #getPairsForIncorrectAnswers(int)}, except will return data for all categories with
     * no limit.
     *
     * @return list of the users most wrong categories, and the amount wrong
     */
    List<Pair<String, Integer>> getPairsForIncorrectAnswers();

    /**
     * Identical behaviour to {@link #getPairsForCorrectAnswers(int)}, except will return data for all categories with
     * no limit.
     *
     * @return list of the users most wrong categories, and the amount wrong
     */
    List<Pair<String, Integer>> getPairsForCorrectAnswers();
}
