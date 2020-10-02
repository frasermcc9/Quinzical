package quinzical.impl.models.structures;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class SaveData implements Serializable {
    private Map<String, List<GameQuestion>> questionData;
    private int value;

    public Map<String, List<GameQuestion>> getQuestionData() {
        return questionData;
    }

    public SaveData setQuestionData(Map<String, List<GameQuestion>> questionData) {
        this.questionData = questionData;
        return this;
    }

    public int getValue() {
        return value;
    }

    public SaveData setValue(int value) {
        this.value = value;
        return this;
    }
}
