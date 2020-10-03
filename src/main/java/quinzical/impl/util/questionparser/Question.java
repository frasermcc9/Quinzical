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

public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    protected final String hint;
    protected final List<Solution> solutions = new ArrayList<>();
    protected final String prompt;
    protected final String category;

    public Question(String category, String hint, String prompt) {
        this.hint = hint;
        this.prompt = prompt;
        this.category = category;
    }

    /**
     * Copy constructor
     *
     * @param q question to copy
     */
    public Question(Question q) {
        this.hint = q.hint;
        this.solutions.addAll(q.solutions);
        this.prompt = q.prompt;
        this.category = q.category;
    }

    public String getCategory() {
        return category;
    }

    public Question addSolution(String[] solutions) {
        Solution s = new Solution(solutions);
        this.solutions.add(s);
        return this;
    }

    public String getHint() {
        return hint;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public List<Solution> getSolutionsCopy() {
        return new ArrayList<>(solutions);
    }

    public String getPrompt() {
        return prompt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(hint, question.hint) &&
            Objects.equals(solutions, question.solutions) &&
            Objects.equals(prompt, question.prompt) &&
            Objects.equals(category, question.category);
    }
}
