package quinzical.impl.util.strategies.questionverifier;

import com.google.inject.Inject;
import com.google.inject.Provider;
import javafx.scene.control.TextArea;
import quinzical.impl.util.questionparser.Solution;
import quinzical.interfaces.strategies.questionverifier.QuestionVerifierFactory;
import quinzical.interfaces.strategies.questionverifier.QuestionVerifierStrategy;
import quinzical.interfaces.strategies.textnormaliser.TextNormaliserFactory;
import quinzical.interfaces.strategies.textnormaliser.TextNormaliserStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class QuestionVerifierFactoryImpl implements QuestionVerifierFactory {

    private final Provider<DefaultQuestionVerifier> questionVerifierStrategyProvider;
    private final Provider<PracticeQuestionVerifier> practiceQuestionVerifierProvider;
    private final Provider<HintQuestionVerifier> hintQuestionVerifierProvider;

    @Inject
    QuestionVerifierFactoryImpl(final Provider<DefaultQuestionVerifier> questionVerifierStrategyProvider,
                                final Provider<PracticeQuestionVerifier> practiceQuestionVerifierProvider,
                                final Provider<HintQuestionVerifier> hintQuestionVerifierProvider) {
        this.questionVerifierStrategyProvider = questionVerifierStrategyProvider;
        this.practiceQuestionVerifierProvider = practiceQuestionVerifierProvider;
        this.hintQuestionVerifierProvider = hintQuestionVerifierProvider;
    }

    static List<Boolean> checkCorrectness(List<Solution> solutions, List<TextArea> textAreas,
                                          TextNormaliserFactory textNormaliserFactory) {

        TextNormaliserStrategy textNormaliserStrategy = textNormaliserFactory.getTextNormalizer();

        List<Boolean> corrects = new ArrayList<>();

        for (TextArea textArea : textAreas) {
            String submission = textNormaliserStrategy.normaliseText(textArea.getText());
            boolean solutionFound = false;
            for (Solution solution : solutions) {
                List<String> variants = solution.getVariants();
                Optional<String> found =
                    variants.stream().filter(v -> textNormaliserStrategy.normaliseText(v).equals(submission)).findAny();
                if (found.isPresent()) {
                    solutions.remove(solution);
                    corrects.add(true);
                    solutionFound = true;
                    textArea.setStyle("-fx-background-color: #ceffc3; -fx-text-fill: #ceffc3");
                    //textArea.setText(found.get());
                    break;
                }
            }
            if (!solutionFound) {
                corrects.add(false);
            }
        }

        return corrects;
    }

    @Override
    public QuestionVerifierStrategy getQuestionVerifier(VerifierType type) {
        switch (type) {
            case FILL_SOLUTION:
                return questionVerifierStrategyProvider.get();
            case HIDE_SOLUTION:
                return practiceQuestionVerifierProvider.get();
            case HINT_SOLUTION:
                return hintQuestionVerifierProvider.get();

            default:
                throw new IllegalArgumentException("Invalid type for factory QuestionVerifierFactoryImpl");
        }

    }


}

class DefaultQuestionVerifier implements QuestionVerifierStrategy {

    @Inject
    private TextNormaliserFactory textNormaliserFactory;

    @Override
    public List<Boolean> verifySolutions(List<Solution> solutions, List<TextArea> textAreas) {

        List<Solution> solutionCopy = new ArrayList<>(solutions);

        List<Boolean> corrects = QuestionVerifierFactoryImpl.checkCorrectness(solutionCopy, textAreas,
            textNormaliserFactory);

        for (int i = 0; i < corrects.size(); i++) {
            if (!corrects.get(i)) {
                TextArea textArea = textAreas.get(i);
                textArea.setStyle("-fx-background-color: #ff858c; -fx-text-fill: #ffc7ca");
                String sln = solutionCopy.remove(0).getVariants().get(0);
                if (sln != null) {
                    textArea.setText(sln);
                    textArea.positionCaret(textArea.getText().length() + 1);
                }
            }
        }

        return corrects;

    }
}

class PracticeQuestionVerifier implements QuestionVerifierStrategy {
    @Inject
    private TextNormaliserFactory textNormaliserFactory;

    @Override
    public List<Boolean> verifySolutions(List<Solution> solutions, List<TextArea> textAreas) {

        List<Boolean> corrects = QuestionVerifierFactoryImpl.checkCorrectness(solutions, textAreas,
            textNormaliserFactory);

        for (int i = 0; i < corrects.size(); i++) {
            if (!corrects.get(i)) {
                TextArea textArea = textAreas.get(i);
                textArea.setStyle("-fx-background-color: #ff858c; -fx-text-fill: #ffc7ca");
                textArea.positionCaret(textArea.getText().length());
            }
        }

        return corrects;
    }
}

class HintQuestionVerifier implements QuestionVerifierStrategy {
    @Inject
    private TextNormaliserFactory textNormaliserFactory;

    @Override
    public List<Boolean> verifySolutions(List<Solution> solutions, List<TextArea> textAreas) {

        List<Solution> solutionCopy = new ArrayList<>(solutions);

        List<Boolean> corrects = QuestionVerifierFactoryImpl.checkCorrectness(solutions, textAreas,
            textNormaliserFactory);

        for (int i = 0; i < corrects.size(); i++) {
            if (!corrects.get(i)) {
                TextArea textArea = textAreas.get(i);
                textArea.setStyle("-fx-background-color: #ff858c; -fx-text-fill: #ffc7ca");
                String sln = solutionCopy.remove(0).getVariants().get(0);
                if (sln != null) {
                    textArea.setText(sln.substring(0, 1));
                    textArea.positionCaret(1);
                }
            }
        }

        return corrects;
    }
}
