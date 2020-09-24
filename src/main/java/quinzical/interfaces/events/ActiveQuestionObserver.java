package quinzical.interfaces.events;

import quinzical.impl.models.structures.GameQuestion;

public interface ActiveQuestionObserver {
    void fireActiveQuestion(GameQuestion gq);
}
