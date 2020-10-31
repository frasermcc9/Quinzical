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

package quinzical.impl.util.strategies.questiongenerator;

import com.google.inject.Inject;
import com.google.inject.Provider;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.impl.util.questionparser.Question;
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategy;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategyFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Factory for generating strategies that pull question sets from the database.
 */
public class QuestionGeneratorStrategyFactoryImpl implements QuestionGeneratorStrategyFactory {

    /**
     * Providers contain their referenced object, created via the injector with their dependencies resolved.
     */
    private final Provider<GameQuestionGeneratorStrategy> gameQuestionGeneratorStrategyProvider;
    private final Provider<PracticeQuestionGeneratorStrategy> practiceQuestionGeneratorStrategyProvider;
    private final Provider<SelectedCategoryGeneratorStrategy> selectedCategoryGeneratorStrategyProvider;
    private final Provider<InternationalQuestionGeneratorStrategy> internationalQuestionGeneratorStrategyProvider;

    @Inject
    public QuestionGeneratorStrategyFactoryImpl(final Provider<GameQuestionGeneratorStrategy> gameQuestionGeneratorStrategyProvider,
                                                final Provider<PracticeQuestionGeneratorStrategy> practiceQuestionGeneratorStrategyProvider,
                                                final Provider<SelectedCategoryGeneratorStrategy> selectedCategoryGeneratorStrategyProvider,
                                                final Provider<InternationalQuestionGeneratorStrategy> internationalQuestionGeneratorStrategyProvider) {
        this.gameQuestionGeneratorStrategyProvider = gameQuestionGeneratorStrategyProvider;
        this.practiceQuestionGeneratorStrategyProvider = practiceQuestionGeneratorStrategyProvider;
        this.selectedCategoryGeneratorStrategyProvider = selectedCategoryGeneratorStrategyProvider;
        this.internationalQuestionGeneratorStrategyProvider = internationalQuestionGeneratorStrategyProvider;
    }

    /**
     * Creates the strategy for loading questions for a regular game - i.e. 5 categories with 5 questions each.
     */
    @Override
    public QuestionGeneratorStrategy createGameQuestionStrategy() {
        return gameQuestionGeneratorStrategyProvider.get();
    }

    /**
     * Creates the strategy for loading questions for a practice game.
     */
    @Override
    public QuestionGeneratorStrategy createPracticeQuestionStrategy() {
        return practiceQuestionGeneratorStrategyProvider.get();

    }

    /**
     * Creates the strategy for creating questions for the specified question.
     *
     * @param categories the string array of categories
     * @return the QuestionGeneratorStrategy that will generate the questions.
     */
    @Override
    public QuestionGeneratorStrategy createSelectedCategoryStrategy(String[] categories) {
        return createSelectedCategoryStrategy(List.of(categories));
    }

    /**
     * Equivalent to {@link #createSelectedCategoryStrategy(String[])} but with a list input.
     *
     * @param categories list of string containing the category names.
     * @return the QuestionGeneratorStrategy that will generate the questions.
     * @see #createGameQuestionStrategy()
     */
    @Override
    public QuestionGeneratorStrategy createSelectedCategoryStrategy(List<String> categories) {
        SelectedCategoryGeneratorStrategy strategy = selectedCategoryGeneratorStrategyProvider.get();
        strategy.setCategories(categories);
        return strategy;
    }

    /**
     * Returns the strategy that can create the international questions.
     *
     * @return the strategy, which when executed, will return international questions and categories.
     */
    @Override
    public QuestionGeneratorStrategy createInternationalQuestionStrategy() {
        return internationalQuestionGeneratorStrategyProvider.get();
    }
}

class PracticeQuestionGeneratorStrategy implements QuestionGeneratorStrategy {

    /**
     * Inject the questionCollection into the strategy
     */
    @Inject
    private QuestionCollection questionCollection;

    /**
     * Generates questions for practice mode
     */
    @Override
    public Map<String, List<GameQuestion>> generateQuestions() {

        Map<String, List<GameQuestion>> questions = new HashMap<>();

        questionCollection.getQuestions().forEach((k, v) -> {
            List<GameQuestion> list = new ArrayList<>();
            v.forEach(q -> {
                GameQuestion question = new GameQuestion(q);
                question.setAnswerable(true);
                list.add(new GameQuestion(question));
            });
            questions.put(k, new ArrayList<>(list));
        });

        return questions;
    }
}

class GameQuestionGeneratorStrategy implements QuestionGeneratorStrategy {

    /**
     * This strategy uses another strategy of this type, so injects the factory to acquire it.
     */
    @Inject
    QuestionGeneratorStrategyFactory factory;

    /**
     * Inject the questionCollection into the strategy
     */
    @Inject
    private QuestionCollection questionCollection;

    /**
     * Generates a question set for the game.
     */
    @Override
    public Map<String, List<GameQuestion>> generateQuestions() {
        Map<String, List<Question>> questions = questionCollection.getQuestions();

        // Shuffle the list of categories and pick 5.
        List<String> allCategories = new ArrayList<>(questions.keySet());
        Collections.shuffle(allCategories);
        List<String> chosen = allCategories.subList(0, 5);

        return factory.createSelectedCategoryStrategy(chosen).generateQuestions();
    }
}

class SelectedCategoryGeneratorStrategy implements QuestionGeneratorStrategy {

    /**
     * Inject the questionCollection into the strategy
     */
    @Inject
    private QuestionCollection questionCollection;

    private List<String> categories;

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    @Override
    public Map<String, List<GameQuestion>> generateQuestions() {
        Map<String, List<Question>> questions = questionCollection.getQuestions();

        Map<String, List<GameQuestion>> questionBoard = new LinkedHashMap<>();

        for (String category : categories) {
            List<Question> availableQuestions = new ArrayList<>(questions.get(category));
            List<GameQuestion> categoryQuestions = new ArrayList<>();

            Collections.shuffle(availableQuestions);

            for (int i = 0; i < 5; i++) {
                GameQuestion q = new GameQuestion(availableQuestions.get(i));
                q.setValue((i + 1) * 100);
                if (i == 0) {
                    q.setAnswerable(true);
                }
                categoryQuestions.add(q);
            }
            questionBoard.put(category, categoryQuestions);
        }

        return questionBoard;

    }
}

/**
 * Generates the international question set.
 */
class InternationalQuestionGeneratorStrategy implements QuestionGeneratorStrategy {

    OkHttpClient client = new OkHttpClient();

    @Override
    public Map<String, List<GameQuestion>> generateQuestions() {

        Map<String, List<GameQuestion>> questions = new LinkedHashMap<>();

        List<CategoryNumberBinding> bindings = new ArrayList<>(5);

        try {
            // Get 5 categories
            int offset = (int) (Math.random() * 10000);
            ResponseBody response = run("https://jservice.io/api/categories?count=5&offset=" + offset);
            String responseString = response.string();
            JSONArray array = new JSONArray(responseString);

            // Convert the api response into a useful format
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = (JSONObject) array.get(i);
                bindings.add(new CategoryNumberBinding() {
                    @Override
                    public String name() {
                        try {
                            return object.get("title").toString();
                        } catch (JSONException jsonException) {
                            return null;
                        }
                    }

                    @Override
                    public String id() {
                        try {
                            return object.get("id").toString();
                        } catch (JSONException jsonException) {
                            return null;
                        }
                    }
                });
            }

            //Get 5 categories from API
            List<String> data = bindings.stream().map(b -> {
                try {
                    return requestQuestions(b.id()).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList());

            // For each category
            for (int j = 0; j < bindings.size(); j++) {
                CategoryNumberBinding binding = bindings.get(j);
                String clues = data.get(j);

                List<GameQuestion> gameQuestions = new ArrayList<>(5);
                questions.put(binding.name(), gameQuestions);

                // Convert json array to normal java list
                JSONArray clueArray = new JSONArray(clues);
                List<JSONObject> jsonObjects = new ArrayList<>();
                for (int i = 0; i < clueArray.length(); i++) {
                    jsonObjects.add(clueArray.getJSONObject(i));
                }
                Collections.shuffle(jsonObjects);

                // Add five clues to the question map
                for (int i = 0; i < 5; i++) {
                    String sln = removeArtifacts(jsonObjects.get(i).getString("answer"));
                    String question = removeArtifacts(jsonObjects.get(i).getString("question"));

                    GameQuestion gameQuestion = new GameQuestion(
                        new Question(
                            binding.name(),
                            question,
                            "What is"
                        ).addSolution(new String[]{sln})
                    );
                    gameQuestion.setValue((i + 1) * 100);
                    if (i == 0) {
                        gameQuestion.setAnswerable(true);
                    }
                    gameQuestions.add(gameQuestion);
                }
            }
            return questions;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Executes the http request
     *
     * @param url the url to GET
     * @return Response body from the http request
     * @throws IOException when the http request fails to execute.
     */
    private ResponseBody run(String url) throws IOException {
        Request request = new Request.Builder()
            .url(url)
            .build();

        Response response = client.newCall(request).execute();
        return response.body();
    }

    private CompletableFuture<String> requestQuestions(String id) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return run("https://jservice.io/api/clues?category=" + id).string();
            } catch (IOException e) {
                return null;
            }
        });
    }

    private String removeArtifacts(String fromApi) {
        fromApi = fromApi.replaceAll("\\\\", ""); //remove backslashes
        fromApi = fromApi.replaceAll("<.>", ""); //remove html opening tags
        fromApi = fromApi.replaceAll("<..>", ""); //remove html closing tags
        return fromApi;
    }

    /**
     * Data interface for pairs of category names to their id in the api.
     */
    private interface CategoryNumberBinding {
        String name();

        String id();
    }
}
