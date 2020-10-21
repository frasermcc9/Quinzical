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

package quinzical.impl.util.strategies.questionverifier;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.jfoenix.controls.JFXTextArea;
import quinzical.impl.util.questionparser.Solution;
import quinzical.interfaces.strategies.questionverifier.QuestionVerifierFactory;
import quinzical.interfaces.strategies.questionverifier.QuestionVerifierStrategy;
import quinzical.interfaces.strategies.textnormaliser.TextNormaliserFactory;
import quinzical.interfaces.strategies.textnormaliser.TextNormaliserStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages the question verifying for both the practice mode and the main game.
 */
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

    /**
     * Checks the text areas given against the solutions and creates a list of boolean values for each text area,
     * showing if they were correct or not.
     *
     * @param solutions             - A list of solutions to the questions
     * @param textAreas             - A list of text areas were the user put their answers in
     * @param textNormaliserFactory - A text normaliser to trim any unnecessary info from the text
     * @return - the list of corrects, showing which text areas are right and which are wrong.
     */
    static List<Boolean> checkCorrectness(List<Solution> solutions, List<JFXTextArea> textAreas,
                                          TextNormaliserFactory textNormaliserFactory) {

        TextNormaliserStrategy textNormaliserStrategy = textNormaliserFactory.getTextNormalizer();

        List<Boolean> corrects = new ArrayList<>();

        for (JFXTextArea textArea : textAreas) {
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
                    System.out.println(textArea.getStyleClass());
                    textArea.getStyleClass().removeAll("answer-field-wrong");
                    textArea.getStyleClass().add("answer-field-right");
                    break;
                }
            }
            if (!solutionFound) {
                corrects.add(false);
            }
        }

        return corrects;
    }

    /**
     * Gets the questionVerifier of the type that is requested
     *
     * @param type - the VerifierType that is being requested
     * @return - A verifier of the requested type.
     */
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

/**
 * Verifies if the text areas contain correct answers, and for any that are not correct, insert the correct answer into
 * the text area.
 */
class DefaultQuestionVerifier implements QuestionVerifierStrategy {

    @Inject
    private TextNormaliserFactory textNormaliserFactory;

    /**
     * Verify that the text in the given textAreas are consistent with the solution list and put the correct answer into
     * any incorrect textAreas.
     *
     * @param solutions - A list of the solutions to the question
     * @param textAreas - The text areas that are being verified
     * @return - A list of whether or not each text area is correct
     */
    @Override
    public List<Boolean> verifySolutions(List<Solution> solutions, List<JFXTextArea> textAreas) {

        List<Solution> solutionCopy = new ArrayList<>(solutions);

        List<Boolean> corrects = QuestionVerifierFactoryImpl.checkCorrectness(solutionCopy, textAreas,
            textNormaliserFactory);

        for (int i = 0; i < corrects.size(); i++) {
            if (!corrects.get(i)) {
                JFXTextArea textArea = textAreas.get(i);
                textArea.getStyleClass().add("answer-field-wrong");
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

/**
 * Verifies if the text areas for practice mode contain correct answers.
 */
class PracticeQuestionVerifier implements QuestionVerifierStrategy {
    @Inject
    private TextNormaliserFactory textNormaliserFactory;

    /**
     * Verify that the text in the given textAreas are consistent with the solution list
     *
     * @param solutions - A list of the solutions to the question
     * @param textAreas - The text areas that are being verified
     * @return - A list of whether or not each text area is correct
     */
    @Override
    public List<Boolean> verifySolutions(List<Solution> solutions, List<JFXTextArea> textAreas) {

        List<Boolean> corrects = QuestionVerifierFactoryImpl.checkCorrectness(solutions, textAreas,
            textNormaliserFactory);

        for (int i = 0; i < corrects.size(); i++) {
            if (!corrects.get(i)) {
                JFXTextArea textArea = textAreas.get(i);
                textArea.getStyleClass().add("answer-field-wrong");
                textArea.positionCaret(textArea.getText().length());
            }
        }

        return corrects;
    }
}

/**
 * Verifies if the text areas contain correct answers, and if they do not all, set the first letter of each incorrect
 * text area as the first letter of one of the solutions, ensuring not to put the same solution first letter into 2 text
 * areas.
 */
class HintQuestionVerifier implements QuestionVerifierStrategy {
    @Inject
    private TextNormaliserFactory textNormaliserFactory;

    /**
     * Verify that the text in the given textAreas are consistent with the solution list and add hints for incorrect
     * textAreas
     *
     * @param solutions - A list of the solutions to the question
     * @param textAreas - The text areas that are being verified
     * @return - A list of whether or not each text area is correct
     */
    @Override
    public List<Boolean> verifySolutions(List<Solution> solutions, List<JFXTextArea> textAreas) {

        List<Solution> solutionCopy = new ArrayList<>(solutions);

        List<Boolean> corrects = QuestionVerifierFactoryImpl.checkCorrectness(solutions, textAreas,
            textNormaliserFactory);

        for (int i = 0; i < corrects.size(); i++) {
            if (!corrects.get(i)) {
                JFXTextArea textArea = textAreas.get(i);
                textArea.getStyleClass().set(1, "answer-field-wrong");
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
