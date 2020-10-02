package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import quinzical.Entry;
import quinzical.impl.constants.Attempts;
import quinzical.impl.constants.GameScene;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.impl.util.questionparser.Solution;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.models.structures.Speaker;
import quinzical.interfaces.strategies.questionverifier.QuestionVerifierFactory;

import java.util.*;

public class PracticeQuestionController {


    
    @Inject
    private SceneHandler sceneHandler;

    @Inject
    private GameModel gameModel;

    @Inject
    private Speaker speaker;
    
    @Inject
    private QuestionVerifierFactory questionVerifierFactory;

    @FXML
    private ImageView imgBackground;

    @FXML
    private Pane panePracticeSolutions;
    
    @FXML
    private ImageView imgOverlay;
    
    @FXML
    private Label lblPrompt;

    @FXML
    private Button btnSubmit;
    
    @FXML
    private Button btnPass;
    
    @FXML
    private Label lblAttempts;
    
    @FXML
    private Label lblHint;

    private List<TextArea> textAreas;
    
    @FXML
    void onSubmitClicked() {
        
        GameQuestion question = gameModel.getActiveQuestion();
        List<Solution> solutions = question.getSolutionsCopy();

        List<Boolean> corrects = questionVerifierFactory.getQuestionVerifier().verifySolutions(solutions, textAreas);
        
        if (corrects.contains(false)){
            System.out.println("wrong");
            if(lblAttempts.getText().equals(Attempts.ATTEMPT_3.getMessage())){
                System.out.println("Attempt 3");
                prepForNewQuestion();
            }
        else if(lblAttempts.getText().equals(Attempts.ATTEMPT_1.getMessage())){
                System.out.println("Attempt 1");
                lblAttempts.setText(Attempts.ATTEMPT_2.getMessage());
                gameModel.activateQuestion(gameModel.getActiveQuestion());
                gameModel.colourTextAreas(textAreas, corrects);
            }
        else {
                System.out.println("Attempt 2");
                lblAttempts.setText(Attempts.ATTEMPT_3.getMessage());
                gameModel.activateQuestion(gameModel.getActiveQuestion());
                gameModel.colourTextAreas(textAreas, corrects);
                for (int i=0;i<textAreas.size();i++){
                    textAreas.get(i).setText(Character.toString(gameModel.getActiveQuestion().getSolutions().get(i).getVariants().get(i).charAt(0)));
                }
            }
            
        }
        else {
            prepForNewQuestion();
        }
        
        //btnPass.setText("Next Question");
        //btnPass.setOnAction(e -> handleNextQuestion(question));
    }

    private void prepForNewQuestion() {
        btnPass.setText("Next Question");
        btnSubmit.setDisable(true);
        btnPass.setOnAction(e -> getNewQuestion());
    }

    private void getNewQuestion() {
        List<GameQuestion> questions = gameModel.getQuestionsForPracticeMode().get(gameModel.getActiveQuestion().getCategory());
        Collections.shuffle(questions);
        gameModel.activateQuestion(questions.get(0));
        btnSubmit.setDisable(false);
        btnPass.setText("Pass");
        btnPass.setOnAction(e -> getNewQuestion());
        this.lblAttempts.setText(Attempts.ATTEMPT_1.getMessage());
    }
    
    @FXML
    void onPassClicked() {
        getNewQuestion();
    }

    @FXML
    void onReplyClick(MouseEvent mouseEvent) {
        speaker.speak(gameModel.getActiveQuestion().getHint());
    }

    @FXML
    void onBackClicked(ActionEvent actionEvent) {
        sceneHandler.setActiveScene(GameScene.PRACTICE);
    }

    private void listen() {
        sceneHandler.onBackgroundChange(img -> this.imgBackground.setImage(img));
        gameModel.onActiveQuestionUpdate(this::initialiseQuestion);
    }

    private void initialiseQuestion(GameQuestion gameQuestion) {

        
        textAreas = new ArrayList<>();
        panePracticeSolutions.getChildren().clear();
        
        this.lblHint.setText(gameQuestion.getHint());
        this.lblPrompt.setText(gameQuestion.getPrompt() + ":");

        speaker.speak(gameQuestion.getHint());

        int slnSize = gameQuestion.getSolutions().size();

        for (int i = 0; i < slnSize; i++) {
            TextArea ta = new TextArea();
            ta.getStylesheets().add(Objects.requireNonNull(Entry.class.getClassLoader().getResource("css/game" +
                "-question.css")).toExternalForm());
            ta.applyCss();
            ta.setPrefWidth(panePracticeSolutions.getPrefWidth());
            ta.setMaxHeight(panePracticeSolutions.getPrefHeight() / slnSize - slnSize);
            ta.setLayoutY(0 + i * panePracticeSolutions.getPrefHeight() / slnSize + 1);
            ta.setPromptText("Enter your solution here...");
            ta.setOnKeyPressed(this::onEnterPressed);
            panePracticeSolutions.getChildren().add(ta);
            textAreas.add(ta);
        }
        textAreas.get(0).requestFocus();
    }

    private void onEnterPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            if (e.getSource() instanceof TextArea) {
                TextArea ta = (TextArea) e.getSource();
                ta.setText(ta.getText().trim());
                int idx = textAreas.indexOf(ta);
                if (idx + 1 == textAreas.size()) {
                    btnSubmit.fire();
                } else {
                    textAreas.get(idx + 1).requestFocus();
                }
            }

        }
    }
    
    @FXML
    void initialize() {
        this.lblAttempts.setText(Attempts.ATTEMPT_1.getMessage());
        listen();
    }
    

}
