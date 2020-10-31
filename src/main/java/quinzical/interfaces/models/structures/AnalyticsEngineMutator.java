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

/**
 * Analytics engine mutator has full access to mutating methods to the analytics engine class. Use this for when data
 * needs to be changed inside the analytics engine class. If you simply need to read data from the analytics engine,
 * then the {@link AnalyticsEngineReader} should be used instead.
 * <p>
 * This interface extends {@link AnalyticsEngineReader}.
 *
 * @author Fraser McCallum
 * @see AnalyticsEngineReader for when data needs to only be read.
 * @see quinzical.impl.models.structures.AnalyticsEngineImpl
 * @since 1.1
 */
public interface AnalyticsEngineMutator extends AnalyticsEngineReader {
    /**
     * Gets the number of questions that have been answered
     *
     * @return the number of questions answered.
     */
    int getQuestionsAnswered();

    /**
     * Sets the number of questions that have been answered
     *
     * @param questionsAnswered what to set the number of questions that have been answered to.
     */
    void setQuestionsAnswered(int questionsAnswered);

    /**
     * Returns the total number of categories that have been answered
     *
     * @return the total number of categories that have been answered
     */
    int getCategoriesAnswered();

    /**
     * Sets the number of categories that have been answered.
     *
     * @param categoriesAnswered what to set the number of categories that have been completely answered to.
     */
    void setCategoriesAnswered(int categoriesAnswered);

    /**
     * Gets the number of correct answers that have been given in total.
     *
     * @return the number of correct answers that the player has answered.
     */
    int getCorrectAnswers();

    /**
     * Gets the number of correct answers that have been given in total.
     *
     * @param correctAnswers the number of answers to set the internal correct answer tracker to.
     */
    void setCorrectAnswers(int correctAnswers);

    /**
     * Indicate that a question has been answered.
     *
     * @param category the category that the question has come from.
     * @param correct  whether the question was correct.
     * @implNote Note this method has non-intuitive usage. This method should be called twice per question answered. The
     * first time should be called false, then the second time should be called with whether it was actually right or
     * wrong.
     */
    void answerQuestion(String category, boolean correct);

    /**
     * Resets all data stored in the analytics engine.
     */
    void resetData();
}
