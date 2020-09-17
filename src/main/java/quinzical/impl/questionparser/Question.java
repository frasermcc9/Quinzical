package quinzical.impl.questionparser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private final String hint;
    private final List<Solution> solutions = new ArrayList<Solution>();
    private final String prompt;

    public Question(String hint, String prompt) {
        this.hint = hint;
        this.prompt = prompt;
    }

    public void addSolution(String[] solutions) {
        Solution s = new Solution(solutions);
        this.solutions.add(s);
    }

}
