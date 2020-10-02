package quinzical.interfaces.events;

/**
 * Functional interface with no arguments. Fired when the question board is updated.
 */
@FunctionalInterface
public interface QuestionBoardObserver {
    void updateQuestionDisplay();
}
