package quinzical.impl.models;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.interfaces.events.ActiveQuestionObserver;
import quinzical.interfaces.events.QuestionObserver;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategyFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Singleton
public class GameModelImpl implements GameModel {


    private final List<QuestionObserver> questionObservers = new ArrayList<>();

    private final List<ActiveQuestionObserver> activeObservers = new ArrayList<>();

    @Inject
    private QuestionGeneratorStrategyFactory questionGeneratorStrategyFactory;

    private Map<String, List<GameQuestion>> boardQuestions;

    private GameQuestion activeQuestion = null;

    /**
     * Returns map containing the questions for the current game.
     */
    @Override
    public Map<String, List<GameQuestion>> getBoardQuestions() {
        return boardQuestions;
    }

    /**
     * Generates a new set of questions.
     */
    @Override
    public void generateNewGameQuestionSet() {
        this.boardQuestions = questionGeneratorStrategyFactory.createGameQuestionStratgey().generateQuestions();
        fireQuestionsUpdate();
    }

    /**
     * Sets the active question in the game.
     */
    @Override
    public void activateQuestion(GameQuestion question) {
        this.activeQuestion = question;
        activeObservers.forEach(o -> o.fireActiveQuestion(question));
    }

    @Override
    public GameQuestion getActiveQuestion() {
        return this.activeQuestion;
    }

    @Override
    public void onQuestionsUpdate(QuestionObserver fn) {
        questionObservers.add(fn);
    }

    @Override
    public void fireQuestionsUpdate() {
        questionObservers.forEach(QuestionObserver::updateQuestionDisplay);
    }

    @Override
    public void onActiveQuestionUpdate(ActiveQuestionObserver fn) {
        activeObservers.add(fn);
    }

    @Override
    public void answerActive() {
        GameQuestion question = this.activeQuestion;
        this.activeQuestion.answer();
        this.activeQuestion = null;

        boolean marked;
        for (List<GameQuestion> entry : this.boardQuestions.values()) {
            marked = false;
            for (GameQuestion q : entry) {
                if (marked) {
                    q.setAnswerable(true);
                    marked = false;
                }
                if (q.equals(question)) {
                    marked = true;
                }
            }
        }
        fireQuestionsUpdate();
    }


}
