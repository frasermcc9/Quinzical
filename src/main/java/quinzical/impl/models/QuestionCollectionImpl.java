package quinzical.impl.models;


import com.google.inject.Singleton;
import quinzical.impl.questionparser.Question;
import quinzical.interfaces.models.QuestionCollection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class QuestionCollectionImpl implements QuestionCollection {

    private Map<String, List<Question>> questionMap;

    public QuestionCollectionImpl() {
        try {
            FileInputStream fileIn = new FileInputStream(System.getProperty("user.dir") + "/questions/question.qdb");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object obj = in.readObject();
            if (obj instanceof HashMap) {
                @SuppressWarnings("unchecked")
                Map<String, List<Question>> map = (Map<String, List<Question>>) obj;
                this.questionMap = map;
            }
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            System.out.println("Error: " + i.getMessage());
        }
    }

    @Override
    public Map<String, List<Question>> getQuestions() {
        return new HashMap<>(this.questionMap);
    }
}
