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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import quinzical.impl.util.questionparser.Question;
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.strategies.objectreader.ObjectReaderStrategy;
import quinzical.interfaces.strategies.objectreader.ObjectReaderStrategyFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton class that contains all questions stored in the question database.
 */
@Singleton
public class QuestionCollectionImpl implements QuestionCollection {

    private final ObjectReaderStrategyFactory objectReaderStrategyFactory;
    /**
     * Contains all categories, with a list of their questions.
     */
    private Map<String, List<Question>> questionMap = new LinkedHashMap<>();

    /**
     * On construction, deserialize the question database.
     */
    @Inject
    public QuestionCollectionImpl(final ObjectReaderStrategyFactory objectReaderStrategyFactory) {
        this.objectReaderStrategyFactory = objectReaderStrategyFactory;
        regenerateQuestionsFromDisk(true);
    }

    /**
     * Returns a copy of the question map.
     */
    @Override
    public final Map<String, List<Question>> getQuestions() {
        return new LinkedHashMap<>(this.questionMap);
    }

    @Override
    public final void regenerateQuestionsFromDisk() {
        regenerateQuestionsFromDisk(false);
    }

    @Override
    public final void regenerateQuestionsFromDisk(final boolean silent) {
        try {
            final ObjectReaderStrategy<Map<String, List<Question>>> strategy =
                objectReaderStrategyFactory.createObjectReader();
            final Map<String, List<Question>> tempMap = strategy.readObject(System.getProperty("user.dir") + "/data" +
                "/question.qdb");

            final Map<String, List<Question>> filteredMap = new LinkedHashMap<>();

            tempMap
                .keySet()
                .stream()
                .filter(k -> tempMap.get(k).size() >= 5)
                .forEach(k -> filteredMap.put(k, tempMap.get(k)));

            if (filteredMap.size() < 5 && !silent) {
                final boolean result = this.showAlert();
                if (!result) return;
            }

            questionMap = filteredMap;

        } catch (final IOException | ClassNotFoundException i) {
            System.out.println("Error: " + i.getMessage());
        }
        assert questionMap != null;
    }

    private boolean showAlert() {
        final Alert alert = new Alert(Alert.AlertType.WARNING, "Pressing cancel will abort all question loading. Press ok " +
            "to load the current question set.", ButtonType.OK,
            ButtonType.CANCEL);
        alert.setTitle("Warning: Invalid Question Set Loaded");
        alert.setHeaderText("Warning: There are insufficient categories with more than five questions. New games will" +
            " not load properly.");
        alert.showAndWait();

        return alert.getResult() == ButtonType.OK;
    }


}
