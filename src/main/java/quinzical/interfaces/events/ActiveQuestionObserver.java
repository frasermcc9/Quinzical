package quinzical.interfaces.events;

/**
 * Functional interface with no arguments. Is fired when a question is activated.
 */
@FunctionalInterface
public interface ActiveQuestionObserver {
    void fireActiveQuestion();
}
