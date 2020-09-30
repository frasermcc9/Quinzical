package quinzical.interfaces.events;

@FunctionalInterface
public interface ActiveQuestionObserver {
    void fireActiveQuestion();
}
