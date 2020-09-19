package quinzical.impl.models.structures;

import quinzical.impl.questionparser.Question;
import quinzical.interfaces.models.structures.GameQuestion;

public class GameQuestionImpl extends Question implements GameQuestion {

    private boolean answered = false;

    public GameQuestionImpl(Question question) {
        super(question);
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }


}
