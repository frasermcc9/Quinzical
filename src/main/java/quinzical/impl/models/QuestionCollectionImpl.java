package quinzical.impl.models;


import quinzical.impl.questionparser.Question;
import quinzical.interfaces.models.QuestionCollection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionCollectionImpl implements QuestionCollection {

    public static String questionDataFileUrl = System.getProperty("user.dir") + "/questions/question.qdb";

    private Map<String, List<Question>> questionMap;


    public QuestionCollectionImpl() {
        try {
            FileInputStream fileIn = new FileInputStream(questionDataFileUrl);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object h = in.readObject();
            if (h instanceof HashMap) {
                @SuppressWarnings("unchecked")
                Map<String, List<Question>> map = (Map<String, List<Question>>) in.readObject();
                this.questionMap = map;
            }
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            System.out.println(i.getMessage());
        }
    }

    @Override
    public Map<String, List<Question>> getQuestions() {
        return this.questionMap;
    }
}
