package quinzical.impl.models.structures;

import quinzical.impl.util.questionparser.Question;

/**
 * Extension of question that adds functionality to make them useful for the game. Adds support for checking if the
 * question is answered, answerable, and its value.
 */
public class GameQuestion extends Question {
    private static final long serialVersionUID = 1L;

    private boolean answered = false;
    private boolean answerable = false;
    private int value = 0;
    private boolean correct = false;

    /**
     * Game questions are constructed by passing the normal question into the constructor.
     *
     * @param question the question to transform into a game question.
     */
    public GameQuestion(Question question) {
        super(question);
    }

    public void answer(boolean correct) {
        this.answerable = false;
        this.answered = true;
        this.correct = correct;
    }

    public boolean isCorrect() {
        return this.correct;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isAnswerable() {
        return answerable;
    }

    public void setAnswerable(boolean answerable) {
        this.answerable = answerable;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

}
