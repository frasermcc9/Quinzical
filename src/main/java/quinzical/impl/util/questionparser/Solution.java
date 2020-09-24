package quinzical.impl.util.questionparser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Solution implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final List<String> variants = new ArrayList<>();

    public Solution(String[] solutions) {
        for (String s : solutions) {
            s = s.trim();
            variants.add(s);
        }
    }

    public Solution(List<String> solutions) {
        for (String s : solutions) {
            s = s.trim();
            variants.add(s);
        }
    }

    public List<String> getVariants() {
        return new ArrayList<>(variants);
    }
}
