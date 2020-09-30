package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
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
    private ImageView imgBackground;
    
    @FXML
    void btnOKPress(ActionEvent actionEvent) {
        if (comboCategories.getValue() != null) {
            List<GameQuestion> questions = gameModel.getQuestionsForPracticeMode().get(comboCategories.getValue());
            Collections.shuffle(questions);
            gameModel.activateQuestion(questions.get(0));
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
        listen();
        List<String> keys = new ArrayList<>(gameModel.getQuestionsForPracticeMode().keySet());
        comboCategories.getItems().addAll(keys);
    }

    private void listen() {
        sceneHandler.onBackgroundChange(img -> this.imgBackground.setImage(img));
    }

    
}
