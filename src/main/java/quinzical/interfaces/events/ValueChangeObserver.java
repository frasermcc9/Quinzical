package quinzical.interfaces.events;

/**
 * Functional interface with no arguments. Fired when the users earnings are changed.
 */
@FunctionalInterface
public interface ValueChangeObserver {
    void updateValue();
}
