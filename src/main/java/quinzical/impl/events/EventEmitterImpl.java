package quinzical.impl.events;

import quinzical.impl.constants.GameEvent;
import quinzical.interfaces.events.EventEmitter;
import quinzical.interfaces.events.EventFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base event emitter class. Classes that extend or delegate to this class can
 * emit events, and other classes can observe those events and run code when
 * they occur.
 */
public class EventEmitterImpl implements EventEmitter {
    /**
     * Event map that associates a list of listener functions with named events.
     */
    private final Map<GameEvent, List<EventFunction>> listeners;

    /**
     * creates a new event emitter
     */
    public EventEmitterImpl() {
        listeners = new HashMap<>();
    }

    /**
     * Listens to an event for this object.
     *
     * Essentially adds a function to the named event. When this event is
     * emitted, then the function will run.
     *
     * @param event the event to listen for
     * @param cb    the function to call when this event occurs
     * @return this
     */
    public EventEmitterImpl on(GameEvent event, EventFunction cb) {
        List<EventFunction> listenerList = listeners.computeIfAbsent(event, k -> new ArrayList<>());
        listenerList.add(cb);

        return this;
    }

    /**
     * Emits an event, which will execute all functions that were bound to the event
     * with the on method.
     *
     * Essentially, when emit is called, every function stored in the map bound to
     * the emitted event will run.
     *
     * @param event the event to emit
     * @return whether there was any event that was emitted
     */
    public boolean emit(GameEvent event) {
        List<EventFunction> listenerList = listeners.get(event);
        if (listenerList == null || listenerList.size() == 0) {
            return false;
        }
        for (EventFunction f : listenerList) {
            f.callbackFunction();
        }
        return true;
    }
}
