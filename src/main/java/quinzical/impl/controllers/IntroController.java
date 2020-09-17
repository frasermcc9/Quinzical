package quinzical.impl.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class IntroController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView imgTitle;

    @FXML
    private Button btnPractice;

    @FXML
    private Button btnPlay;

    @FXML
    void btnPlayClick(ActionEvent event) {

    }

    @FXML
    void btnPracticeClick(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert imgTitle != null : "fx:id=\"imgTitle\" was not injected: check your FXML file 'intro.fxml'.";
        assert btnPractice != null : "fx:id=\"btnPractice\" was not injected: check your FXML file 'intro.fxml'.";
        assert btnPlay != null : "fx:id=\"btnPlay\" was not injected: check your FXML file 'intro.fxml'.";

    }
}
