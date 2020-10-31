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

package quinzical.impl.util.questionparser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Question represents a singular question to be used in the game, storing the category, hint, and solutions.
 */
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    protected final String hint;
    protected final List<Solution> solutions = new ArrayList<>();
    protected final String prompt;
    protected final String category;

    /**
     * Creates a new Question object with appropriate values 
     * 
     * @param category - the category of the question
     * @param hint - the hint for the question
     * @param prompt - the prompt for the question
     */
    public Question(final String category, final String hint, final String prompt) {
        this.hint = hint;
        this.prompt = prompt;
        this.category = category;
    }

    /**
     * Copy constructor
     *
     * @param q question to copy
     */
    public Question(final Question q) {
        this.hint = q.hint;
        this.solutions.addAll(q.solutions);
        this.prompt = q.prompt;
        this.category = q.category;
    }

    /**
     * gets the category of this question
     */
    public String getCategory() {
        return category;
    }

    /**
     * Adds the inputs solution to the list of solutions for this question
     * 
     * @param solutions - the solution to add to this question
     * @return - the question the solutions are being added to
     */
    public Question addSolution(final String[] solutions) {
        final Solution s = new Solution(solutions);
        this.solutions.add(s);
        return this;
    }

    /**
     * gets the hint for this question
     */
    public String getHint() {
        return hint;
    }

    /**
     * gets the solutions for this question
     */
    public List<Solution> getSolutions() {
        return solutions;
    }

    /**
     * gets a copy of the solutions for this question (so that they cant be changed)
     */
    public List<Solution> getSolutionsCopy() {
        return new ArrayList<>(solutions);
    }

    /**
     * gets the prompt for this question
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * Checks if two questions are the same
     * 
     * @param o - the question that is being compared to this
     * @return - whether or not the inputted question and this are the same
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Question question = (Question) o;
        return Objects.equals(hint, question.hint) &&
            Objects.equals(solutions, question.solutions) &&
            Objects.equals(prompt, question.prompt) &&
            Objects.equals(category, question.category);
    }
}
