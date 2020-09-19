package quinzical.interfaces.events;

import quinzical.impl.constants.GameEvent;

public interface EventEmitter {
    EventEmitter on(GameEvent event, EventFunction cb);

    boolean emit(GameEvent event);
}
