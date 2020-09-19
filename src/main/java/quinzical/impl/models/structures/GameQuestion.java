package quinzical.impl.models.structures;

import quinzical.impl.questionparser.Question;

public class GameQuestion extends Question {

    private boolean answered = false;
    private boolean answerable = false;
    private int value = 0;

    public GameQuestion(Question question) {
        super(question);
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
