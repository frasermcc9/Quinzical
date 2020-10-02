package quinzical.interfaces.models;

import quinzical.impl.util.questionparser.Question;

import java.util.List;

public interface PracticeModel extends QuinzicalModel {
    Question getRandomQuestion(String category);

    void activateQuestion(Question question);

    List<String> getCategories();
}
