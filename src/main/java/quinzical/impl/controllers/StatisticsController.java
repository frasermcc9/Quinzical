package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import quinzical.Entry;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static javafx.collections.FXCollections.observableArrayList;

public class StatisticsController extends StandardSceneController {

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

    @Override
    protected void onLoad() {
        createChart(createAnswerData(), "Correct Answer Ratio");
        createMostAnsweredTable();
        createMostChallengingTable();
    }

    private ObservableList<PieChart.Data> createAnswerData() {
        int correct = gameModel.getUserData().getCorrect();
        int incorrect = gameModel.getUserData().getIncorrect();

        return observableArrayList(
            new PieChart.Data("Correct (" + correct + ")", correct),
            new PieChart.Data("Incorrect (" + incorrect + ")", incorrect)
        );
    }

    private void createMostAnsweredTable() {
        ObservableList<String> categories =
            observableArrayList(gameModel.getUserData().getAnalytics().getMostAnsweredCategories());
        ObservableList<String> answered =
            observableArrayList(gameModel.getUserData().getAnalytics().getQuestionsAnsweredByCategory(categories));

        createNameValueTable(tableMostAnswered, colMostAnsweredName, colMostAnsweredNumber, categories,
            answered);
    }

    private void createMostChallengingTable() {
        ObservableList<String> categories =
            observableArrayList(gameModel.getUserData().getAnalytics().getMostChallengingCategories());
        ObservableList<String> answered =
            observableArrayList(gameModel.getUserData().getAnalytics().getCorrectRatiosOfCategories(categories));

        createNameValueTable(tableMostChallenging, colMostChallengingName, colMostChallengingNumber, categories,
            answered);
    }

    private void createNameValueTable(TableView<NameValuePair> table, TableColumn<NameValuePair, String> names,
                                      TableColumn<NameValuePair, String> values, ObservableList<String> nameList,
                                      ObservableList<String> valueList) {
        names.setCellValueFactory(v -> v.getValue().nameProperty());
        values.setCellValueFactory(v -> v.getValue().valueProperty());
        table.setItems(observableArrayList(
            IntStream
                .range(0, nameList.size())
                .mapToObj(v -> new NameValuePair(nameList.get(v), valueList.get(v)))
                .collect(Collectors.toList())
        ));
    }

    private ScaleTransition createScaleAnimation(Node node) {
        final ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.seconds(0.7));
        scaleTransition.setNode(node);
        scaleTransition.setFromX(0.75);
        scaleTransition.setFromY(0.75);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.setInterpolator(new Interpolator() {
            @Override
            protected double curve(double t) {
                return -1.76 * (Math.pow(t, 3)) + 0.931 * (Math.pow(t, 2)) + 1.785 * t;
            }
        });
        return scaleTransition;
    }

    private void createChart(ObservableList<PieChart.Data> pieChartData, String title) {
        PieChart chart = this.pieRatio;
        pieRatio.setData(pieChartData);
        ScaleTransition scaleTransition = createScaleAnimation(chart);
        chart.getStylesheets().add(Objects.requireNonNull(Entry.class.getClassLoader().getResource("css/statistics" +
            ".css")).toExternalForm());
        chart.applyCss();
        chart.setStyle("-fx-effect: dropshadow(three-pass-box, black, 20, 0.1, 3, 3);");
        chart.setLegendVisible(false);
        chart.setTitle(title);
        chart.setLabelLineLength(15);
        scaleTransition.play();
    }


    @FXML
    void btnBackPress() {
        sceneHandler.setActiveScene(GameScene.INTRO);
    }

    private static class NameValuePair {
        private final SimpleStringProperty name;
        private final SimpleStringProperty value;

        public NameValuePair(String name, double value) {
            this.name = new SimpleStringProperty(name);
            this.value = new SimpleStringProperty(value + "");
        }

        public NameValuePair(String name, String value) {
            this.name = new SimpleStringProperty(name);
            this.value = new SimpleStringProperty(value);
        }

        public String getName() {
            return name.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public String getValue() {
            return value.get();
        }

        public void setValue(int value) {
            this.value.set(value + "");
        }

        public SimpleStringProperty valueProperty() {
            return value;
        }
    }
}
