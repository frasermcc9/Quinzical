package quinzical.impl.util.questionparser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
}
