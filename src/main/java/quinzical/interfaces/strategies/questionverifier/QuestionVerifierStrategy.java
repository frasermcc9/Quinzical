package quinzical.interfaces.strategies.questionverifier;

import javafx.scene.control.TextArea;
import quinzical.impl.util.questionparser.Solution;

import java.util.List;

public interface QuestionVerifierStrategy {
    List<Boolean> verifySolutions(List<Solution> solutions, List<TextArea> textAreas);
}

