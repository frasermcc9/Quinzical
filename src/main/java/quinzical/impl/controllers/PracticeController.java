package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import quinzical.impl.constants.GameScene;
import quinzical.impl.constants.Theme;
import quinzical.impl.models.QuestionCollectionImpl;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.models.SceneHandler;

import java.util.*;

public class PracticeController {

    @Inject
    private SceneHandler sceneHandler;

    @Inject
    private GameModel gameModel;

    @FXML
    private ComboBox<String> comboCategories;

    @FXML
    void btnOKPress(ActionEvent actionEvent) {
        if (comboCategories.getValue() != null) {
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
    void initialize(){
        List<String> keys = new ArrayList<>(gameModel.getQuestionsForPracticeMode().keySet());
        comboCategories.getItems().addAll(keys);
    }
    
}
