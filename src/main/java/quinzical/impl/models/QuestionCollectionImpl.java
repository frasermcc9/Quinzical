// Copyright 2020 Fraser McCallum and Braden Palmer
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//  
//     http://www.apache.org/licenses/LICENSE-2.0
//  
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package quinzical.impl.models;

import com.google.inject.Singleton;
import quinzical.impl.util.questionparser.Question;
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
            FileInputStream fileIn = new FileInputStream(System.getProperty("user.dir") + "/data/question2.qdb");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Object obj = in.readObject();
            if (obj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, List<Question>> map = (Map<String, List<Question>>) obj;
                this.questionMap = new LinkedHashMap<>(map);
            }
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException i) {
            System.out.println("Error: " + i.getMessage());
        }
        assert questionMap != null;
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
