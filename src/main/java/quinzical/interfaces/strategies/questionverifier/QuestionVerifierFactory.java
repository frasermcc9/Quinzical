package quinzical.interfaces.strategies.questionverifier;

import quinzical.impl.util.strategies.questionverifier.VerifierType;

public interface QuestionVerifierFactory {
    QuestionVerifierStrategy getQuestionVerifier(VerifierType type);
}
