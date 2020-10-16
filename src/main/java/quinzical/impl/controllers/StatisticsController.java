package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.FlowPane;
import quinzical.Entry;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;

import java.util.Objects;

import static javafx.collections.FXCollections.observableArrayList;

public class StatisticsController extends StandardSceneController {

    @Inject
    GameModel gameModel;

    @Inject
    SceneHandler sceneHandler;

    @FXML
    private FlowPane pane;

    @Override
    protected void onLoad() {
        ObservableList<PieChart.Data> pieChartData = createData();

        final PieChart chart = new PieChart(pieChartData);
        chart.getStylesheets().add(Objects.requireNonNull(Entry.class.getClassLoader().getResource("css/statistics" +
            ".css")).toExternalForm());
        chart.applyCss();
        chart.setStyle("-fx-effect: dropshadow(three-pass-box, black, 20, 0.1, 3, 3);");
        chart.setLegendVisible(false);
        chart.setTitle("Question Correct Ratio");
        pane.getChildren().add(chart);
    }

    private ObservableList<PieChart.Data> createData() {
        int correct = gameModel.getUserData().getCorrect();
        int incorrect = gameModel.getUserData().getIncorrect();
        return observableArrayList(
            new PieChart.Data("Correct (" + correct + ")", correct),
            new PieChart.Data("Incorrect (" + incorrect + ")", incorrect)
        );
    }

    @FXML
    void btnBackPress() {
        sceneHandler.setActiveScene(GameScene.INTRO);
    }
}
