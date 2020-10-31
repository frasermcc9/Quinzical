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

package quinzical.impl.controllers.menus;

import com.google.inject.Inject;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.Glow;
import javafx.util.Duration;
import javafx.util.Pair;
import quinzical.Entry;
import quinzical.impl.constants.GameScene;
import quinzical.impl.controllers.AbstractSceneController;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * Controller for the statistics scene
 */
public class StatisticsController extends AbstractSceneController {

    @Inject
    GameModel gameModel;

    @Inject
    SceneHandler sceneHandler;

    @FXML
    private PieChart pieRatio;

    @FXML
    private TableView<NameValuePair> tableMostAnswered;

    @FXML
    private TableColumn<NameValuePair, String> colMostAnsweredName;

    @FXML
    private TableColumn<NameValuePair, String> colMostAnsweredNumber;

    @FXML
    private TableView<NameValuePair> tableMostChallenging;

    @FXML
    private TableColumn<NameValuePair, String> colMostChallengingName;

    @FXML
    private TableColumn<NameValuePair, String> colMostChallengingNumber;

    @FXML
    private Button btnChartReset;

    /**
     * Creates the the statistics visualisation when the scene is loaded
     */
    @Override
    protected void onLoad() {
        Platform.runLater(() -> {
            createChart();
            createMostAnsweredTable();
            createMostChallengingTable();
        });
    }

    /**
     * Creates a list of data to be used in the pie chart
     */
    private ObservableList<PieChart.Data> createAnswerData() {
        final int correct = gameModel.getUserData().getCorrect();
        final int incorrect = gameModel.getUserData().getIncorrect();

        final PieChart.Data correctData = new PieChart.Data("Correct (" + correct + ")", correct);
        final PieChart.Data incorrectData = new PieChart.Data("Incorrect (" + incorrect + ")", incorrect);

        return observableArrayList(correctData, incorrectData);
    }

    /**
     * Sets up the table for the most answered question data
     */
    private void createMostAnsweredTable() {
        final ObservableList<String> categories =
            observableArrayList(gameModel.getUserData().getAnalytics().getMostAnsweredCategories());
        final ObservableList<String> answered =
            observableArrayList(gameModel.getUserData().getAnalytics().getQuestionsAnsweredByCategory(categories));

        createNameValueTable(tableMostAnswered, colMostAnsweredName, colMostAnsweredNumber, categories,
            answered);
    }

    /**
     * Sets up the table for the most challenging questions data
     */
    private void createMostChallengingTable() {
        final ObservableList<String> categories =
            observableArrayList(gameModel.getUserData().getAnalytics().getMostChallengingCategories());
        final ObservableList<String> answered =
            observableArrayList(gameModel.getUserData().getAnalytics().getCorrectRatiosOfCategories(categories));

        createNameValueTable(tableMostChallenging, colMostChallengingName, colMostChallengingNumber, categories,
            answered);
    }

    /**
     * Creates a table using the inputted data
     */
    private void createNameValueTable(final TableView<NameValuePair> table, final TableColumn<NameValuePair, String> names,
                                      final TableColumn<NameValuePair, String> values, final ObservableList<String> nameList,
                                      final ObservableList<String> valueList) {
        names.setCellValueFactory(v -> v.getValue().nameProperty());
        values.setCellValueFactory(v -> v.getValue().valueProperty());
        table.setItems(observableArrayList(
            IntStream
                .range(0, nameList.size())
                .mapToObj(v -> new NameValuePair(nameList.get(v), valueList.get(v)))
                .collect(Collectors.toList())
        ));
    }

    /**
     * Creates a scale animation for the given node
     * 
     * @param node The node that the animation is for
     * @return The animation for the given node
     */
    private ScaleTransition createScaleAnimation(final Node node) {
        final ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.seconds(0.7));
        scaleTransition.setNode(node);
        scaleTransition.setFromX(0.75);
        scaleTransition.setFromY(0.75);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.setInterpolator(new Interpolator() {
            @Override
            protected double curve(final double t) {
                return -1.76 * (Math.pow(t, 3)) + 0.931 * (Math.pow(t, 2)) + 1.785 * t;
            }
        });
        return scaleTransition;
    }

    /**
     * Creates a pie chart from data got from createAnswerData
     */
    private void createChart() {
        pieRatio.setData(createAnswerData());
        final ScaleTransition scaleTransition = createScaleAnimation(pieRatio);
        pieRatio.getStylesheets().add(Objects.requireNonNull(Entry.class.getClassLoader().getResource("css/statistics" +
            ".css")).toExternalForm());
        pieRatio.applyCss();
        pieRatio.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.4, 3, 3);");
        pieRatio.setLegendVisible(false);
        pieRatio.setTitle("Correct Answer Ratio");
        pieRatio.setLabelLineLength(15);
        scaleTransition.play();
        setChartStateOne(false);
    }
    
    private void setChartStateOne(final boolean reloadData) {
        if (reloadData)
            pieRatio.setData(createAnswerData());
        final Node correct = pieRatio.getData().get(0).getNode();
        final Node wrong = pieRatio.getData().get(1).getNode();
        correct.setOnMouseClicked(a -> setChartStateCorrect());
        wrong.setOnMouseClicked(a -> setChartStateIncorrect());

        pieRatio.getData().forEach(data -> {
            final Node node = data.getNode();
            node.setCursor(Cursor.HAND);
            node.setOnMouseEntered(a -> {
                node.setEffect(new Glow());
                final ScaleTransition innerST = new ScaleTransition();
                innerST.setDuration(Duration.seconds(0.4));
                innerST.setNode(node);
                innerST.setFromX(node.getScaleX());
                innerST.setFromY(node.getScaleY());
                innerST.setToX(1.1);
                innerST.setToY(1.1);
                innerST.playFromStart();
            });
            node.setOnMouseExited(a -> {
                node.setEffect(null);
                final ScaleTransition innerST = new ScaleTransition();
                innerST.setDuration(Duration.seconds(0.4));
                innerST.setNode(node);
                innerST.setFromX(node.getScaleX());
                innerST.setFromY(node.getScaleY());
                innerST.setToX(1);
                innerST.setToY(1);
                innerST.playFromStart();
            });
        });
    }

    private void setChartStateOne() {
        setChartStateOne(true);
    }

    private void setChartStateCorrect() {
        btnChartReset.setDisable(false);
        pieRatio.setData(rightChartData());
    }

    private void setChartStateIncorrect() {
        btnChartReset.setDisable(false);
        pieRatio.setData(wrongChartData());
    }

    @FXML
    void resetChartClick() {
        btnChartReset.setDisable(true);
        setChartStateOne();
    }

    private ObservableList<PieChart.Data> wrongChartData() {
        final List<Pair<String, Integer>> data = gameModel.getUserData().getAnalytics().getPairsForIncorrectAnswers();
        return createSecondaryChartData(data);
    }

    private ObservableList<PieChart.Data> rightChartData() {
        final List<Pair<String, Integer>> data = gameModel.getUserData().getAnalytics().getPairsForCorrectAnswers();
        return createSecondaryChartData(data);
    }

    private ObservableList<PieChart.Data> createSecondaryChartData(final List<Pair<String, Integer>> data) {
        final var list = observableArrayList(
            data.stream()
                .limit(8)
                .map(d -> new PieChart.Data(d.getKey() + " (" + d.getValue() + ")", d.getValue()))
                .collect(Collectors.toList()));

        final var other = data.stream()
            .skip(8)
            .reduce(0, (total, current) -> total + current.getValue(), Integer::sum);

        list.add(new PieChart.Data("Other" + "(" + other + ")", other));
        return list;
    }

    /**
     * Returns the scene back to the main menu
     */
    @FXML
    void btnBackPress() {
        sceneHandler.setActiveScene(GameScene.INTRO);
    }

    public static class NameValuePair {
        private final SimpleStringProperty name;
        private final SimpleStringProperty value;

        public NameValuePair(final String name, final double value) {
            this.name = new SimpleStringProperty(name);
            this.value = new SimpleStringProperty(value + "");
        }

        public NameValuePair(final String name, final String value) {
            this.name = new SimpleStringProperty(name);
            this.value = new SimpleStringProperty(value);
        }

        public String getName() {
            return name.get();
        }

        public void setName(final String name) {
            this.name.set(name);
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public String getValue() {
            return value.get();
        }

        public void setValue(final int value) {
            this.value.set(value + "");
        }

        public SimpleStringProperty valueProperty() {
            return value;
        }
    }
}
