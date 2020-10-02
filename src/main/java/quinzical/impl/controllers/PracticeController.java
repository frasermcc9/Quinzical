package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import quinzical.impl.constants.GameScene;
import quinzical.impl.util.questionparser.Question;
import quinzical.interfaces.models.PracticeModel;
import quinzical.interfaces.models.SceneHandler;

import java.util.List;

public class PracticeController {

    @Inject
    private SceneHandler sceneHandler;

    @Inject
    private PracticeModel gameModel;

    @FXML
    private ComboBox<String> comboCategories;

    @FXML
    private ImageView imgBackground;

    @FXML
    void btnOKPress(ActionEvent actionEvent) {
        if (comboCategories.getValue() != null) {
            Question question = gameModel.getRandomQuestion(comboCategories.getValue());
            gameModel.activateQuestion(question);
            sceneHandler.setActiveScene(GameScene.PRACTICE_QUESTION);
        }
    }

    @FXML
    void onCategoryChosen(ActionEvent actionEvent) {
    }

    @FXML
    void btnBackPress(ActionEvent actionEvent) {
        sceneHandler.setActiveScene(GameScene.INTRO);
    }

    @FXML
    void initialize() {

        List<String> categories = gameModel.getCategories();

        comboCategories.getItems().addAll(categories);
        comboCategories.setValue(categories.get(0));

        listen();

    }

    private void listen() {
        sceneHandler.onBackgroundChange(img -> this.imgBackground.setImage(img));
    }
}
