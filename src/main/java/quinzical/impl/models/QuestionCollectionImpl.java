package quinzical.impl.models;

import com.google.inject.Singleton;
import quinzical.impl.questionparser.Question;
import quinzical.interfaces.models.QuestionCollection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton class that contains all questions stored in the question database.
 */
@Singleton
public class QuestionCollectionImpl implements QuestionCollection {

    /**
     * Contains all categories, with a list of their questions.
     */
    private Map<String, List<Question>> questionMap;

    /**
     * On construction, deserialize the question database.
     */
    public QuestionCollectionImpl() {
        try {
            FileInputStream fileIn = new FileInputStream(System.getProperty("user.dir") + "/questions/question.qdb");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object obj = in.readObject();
            if (obj instanceof HashMap) {
                @SuppressWarnings("unchecked")
                Map<String, List<Question>> map = (Map<String, List<Question>>) obj;
                this.questionMap = new LinkedHashMap<>(map);
            }
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            System.out.println("Error: " + i.getMessage());
        }
    }

    /**
     * @deprecated not implemented.
     */
    @Deprecated
    public void SerializeQuestionDatabase() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Method not implemented");
    }

    /**
     * @deprecated not implemented.
     */
    @Deprecated
    public void AddQuestionToDatabase() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Method not implemented");
    }

    /**
     * @deprecated not implemented.
     */
    @Deprecated
    public void RemoveQuestionFromDatabase() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Method not implemented");
    }

    /**
     * Returns a copy of the question map.
     */
    @Override
    public Map<String, List<Question>> getQuestions() {
        return new LinkedHashMap<>(this.questionMap);
    }
}
