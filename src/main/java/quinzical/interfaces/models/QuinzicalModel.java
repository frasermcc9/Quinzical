package quinzical.interfaces.models;

import javafx.scene.control.TextArea;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.impl.util.questionparser.Question;
import quinzical.interfaces.events.ActiveQuestionObserver;

import java.util.List;

public interface QuinzicalModel {
    GameQuestion getActiveQuestion();

    void activateQuestion(GameQuestion question);

    void onActiveQuestionUpdate(ActiveQuestionObserver fn);

    void colourTextAreas(List<TextArea> textAreas, List<Boolean> corrects);
    
}
