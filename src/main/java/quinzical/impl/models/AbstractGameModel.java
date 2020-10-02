package quinzical.impl.models;

import com.google.inject.Inject;
import javafx.scene.control.TextArea;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.impl.util.questionparser.Question;
import quinzical.interfaces.events.ActiveQuestionObserver;
import quinzical.interfaces.models.QuinzicalModel;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategyFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGameModel implements QuinzicalModel {

    /**
     * List of functions (observers) that are executed when the game board updates.  Adding (subscribing) to the list is
     * done with {@link this#onActiveQuestionUpdate}, which will add the given ActiveQuestionObserver to the list. When
     * {@link this#fireActiveQuestionUpdate} )} is called, all functions will be executed.
     */
    protected final List<ActiveQuestionObserver> activeObservers = new ArrayList<>();

    /**
     * Factory for creating strategies for generating the questions for the board.
     */
    @Inject
    protected QuestionGeneratorStrategyFactory questionGeneratorStrategyFactory;

    /**
     * Most recent question that was active, or null if no question was active. Potential bug: is null when loading game
     * from save.
     */
    protected GameQuestion activeQuestion = null;

    /**
     * Gets the currently active question
     *
     * @return current active question, or null if there isn't one.
     */
    @Override
    public GameQuestion getActiveQuestion() {
        return this.activeQuestion;
    }

    /**
     * Sets the active question in the game.
     */
    @Override
    public void activateQuestion(GameQuestion question) {
        this.activeQuestion = question;
        fireActiveQuestionUpdate();
    }

    @Override
    public void onActiveQuestionUpdate(ActiveQuestionObserver fn) {
        activeObservers.add(fn);
    }

    protected void fireActiveQuestionUpdate() {
        this.activeObservers.forEach(ActiveQuestionObserver::fireActiveQuestion);
    }

    @Override
    public void colourTextAreas(List<TextArea> textAreas, List<Boolean> corrects) {
        for (int i = 0; i < textAreas.size(); i++) {
            if (corrects.get(i)) {
                textAreas.get(i).setStyle("-fx-background-color: #ceffc3; -fx-text-fill: #ceffc3");
            } else {
                textAreas.get(i).setStyle("-fx-background-color: #ff858c; -fx-text-fill: #ffc7ca");
            }
        }
    }
}
