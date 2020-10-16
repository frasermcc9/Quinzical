package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import quinzical.impl.constants.GameScene;
import quinzical.impl.constants.Theme;
import quinzical.impl.util.questionparser.Serializer;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.models.structures.SpeakerMutator;

import java.util.Optional;

public class OptionsController extends StandardSceneController {

    static final int DEFAULT_PITCH;
    static final int DEFAULT_SPEED;
    static final int DEFAULT_AMP;
    static final int DEFAULT_GAP;
    static final double DEFAULT_TIMER;

    static {
        DEFAULT_TIMER = 30;
        DEFAULT_PITCH = 50;
        DEFAULT_SPEED = 175;
        DEFAULT_AMP = 100;
        DEFAULT_GAP = 0;
    }

    @Inject
    private SceneHandler sceneHandler;
    @Inject
    private SpeakerMutator speakerMutator;
    @Inject
    private GameModel gameModel;
    @Inject
    private QuestionCollection questionCollection;

    @FXML
    private Label lblTheme;
    @FXML
    private Label lblAccess;
    @FXML
    private Label lblHelp;
    @FXML
    private Label lblAdvanced;
    @FXML
    private ListView<Theme> themeList;

    @FXML
    private Slider sliderSpeed;
    @FXML
    private Slider sliderGap;
    @FXML
    private Slider sliderAmp;
    @FXML
    private Slider sliderPitch;
    @FXML
    private Slider sliderTimer;

    @FXML
    private HBox hboxContainer;
    @FXML
    private VBox vboxTheme;
    @FXML
    private VBox vboxAccess;
    @FXML
    private VBox vboxHelp;
    @FXML
    private VBox vboxAdvanced;

    private VBox activeVbox;
    private Label activeLabel;

    @Override
    protected void onLoad() {

        ObservableList<Theme> list = FXCollections.observableArrayList(Theme.values());
        themeList.setItems(list);
        themeList.getSelectionModel().select(sceneHandler.getActiveTheme());
        themeList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            sceneHandler.fireBackgroundChange(newValue);
            background.setImage(newValue.getImage());
        });

        sliderTimer.valueProperty().addListener(e -> gameModel.setTimerValue(sliderTimer.getValue()));
        sliderTimer.setValue(gameModel.getTimerValue());
        sliderSpeed.valueProperty().addListener(e -> adjustSpeaker(SpeechProperty.SPEED, (int) sliderSpeed.getValue()));
        sliderSpeed.setValue(speakerMutator.getSpeed());
        sliderPitch.valueProperty().addListener(e -> adjustSpeaker(SpeechProperty.PITCH, (int) sliderPitch.getValue()));
        sliderPitch.setValue(speakerMutator.getPitch());
        sliderAmp.valueProperty().addListener(e -> adjustSpeaker(SpeechProperty.AMPLITUDE, (int) sliderAmp.getValue()));
        sliderAmp.setValue(speakerMutator.getAmplitude());
        sliderGap.valueProperty().addListener(e -> adjustSpeaker(SpeechProperty.GAP, (int) sliderGap.getValue()));
        sliderGap.setValue(speakerMutator.getGap());
    }

    @FXML
    void btnMenuClick() {
        sceneHandler.setActiveScene(GameScene.INTRO);
    }

    //#region ACCESSIBILITY

    /**
     * Adjusts the Speakers properties according to the value and the property that is being changed.
     *
     * @param sp    the speech property that is being changed (pitch, speed, amplitude or gap)
     * @param value the value that the property is to be set to.
     */
    private void adjustSpeaker(SpeechProperty sp, int value) {
        switch (sp) {
            case SPEED:
                speakerMutator.setSpeed(value);
                break;
            case PITCH:
                speakerMutator.setPitch(value);
                break;
            case AMPLITUDE:
                speakerMutator.setAmplitude(value);
                break;
            case GAP:
                speakerMutator.setGap(value);
                break;
        }
    }

    /**
     * Sets the pitch back to the default value and puts the slider in the correct position for that.
     */
    @FXML
    void pitchDefault() {
        speakerMutator.setPitch(DEFAULT_PITCH);
        sliderPitch.setValue(DEFAULT_PITCH);
    }

    /**
     * Sets the speed back to the default value and puts the slider in the correct position for that.
     */
    @FXML
    void speedDefault() {
        speakerMutator.setSpeed(DEFAULT_SPEED);
        sliderSpeed.setValue(DEFAULT_SPEED);
    }

    /**
     * Sets the amplitude back to the default value and puts the slider in the correct position for that.
     */
    @FXML
    void ampDefault() {
        speakerMutator.setAmplitude(DEFAULT_AMP);
        sliderAmp.setValue(DEFAULT_AMP);
    }

    /**
     * Sets the speaker gap back to the default value and puts the slider in the correct position for that.
     */
    @FXML
    void gapDefault() {
        speakerMutator.setGap(DEFAULT_GAP);
        sliderGap.setValue(DEFAULT_GAP);
    }

    @FXML
    void timerDefault() {
        gameModel.setTimerValue(DEFAULT_TIMER);
        sliderTimer.setValue(DEFAULT_TIMER);
    }

    /**
     * Runs a sample message of the speaker to show the user what it sounds like.
     */
    @FXML
    void onSampleClick(MouseEvent event) {
        speakerMutator.speak("Hello, welcome to Quinzical!");
    }


    // #endregion

    //#region ADVANCED

    @FXML
    void btnResetLocalData() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Data Deletion");
        alert.setHeaderText("Are you sure you want to delete your local user data?");
        alert.setContentText("This will reset your international question unlock, your statistics and your current " +
            "game. This will not reset your coins.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            gameModel.getUserData().resetUserData();
        }
    }

    @FXML
    void btnLoadNewQuestionSet() {
        Serializer.main(null);
        questionCollection.regenerateQuestionsFromDisk();
    }

    //#endregion

    //#region ANIMATORS

    @FXML
    void advancedHovered(MouseEvent event) {
        if (activeVbox == vboxAdvanced) return;
        if (activeVbox != null) {
            animateOutSettings(activeVbox, activeLabel);
        }

        activeVbox = vboxAdvanced;
        activeLabel = lblAdvanced;
        animateInSettings(activeVbox, activeLabel);
    }

    @FXML
    void helpHovered(MouseEvent event) {
        if (activeVbox == vboxHelp) return;
        if (activeVbox != null) {
            animateOutSettings(activeVbox, activeLabel);
        }

        activeVbox = vboxHelp;
        activeLabel = lblHelp;
        animateInSettings(activeVbox, activeLabel);
    }

    @FXML
    void themeHovered(MouseEvent event) {
        if (activeVbox == vboxTheme) return;
        if (activeVbox != null) {
            animateOutSettings(activeVbox, activeLabel);
        }

        activeVbox = vboxTheme;
        activeLabel = lblTheme;
        animateInSettings(activeVbox, activeLabel);
    }

    @FXML
    void accessHovered(MouseEvent event) {
        if (activeVbox == vboxAccess) return;
        if (activeVbox != null) {
            animateOutSettings(activeVbox, activeLabel);
        }

        activeVbox = vboxAccess;
        activeLabel = lblAccess;
        animateInSettings(activeVbox, activeLabel);
    }

    private void animateInSettings(VBox vBox, Label label) {
        final Timeline timeline = new Timeline(
            new KeyFrame(
                Duration.ZERO,
                new KeyValue(vBox.opacityProperty(), vBox.getOpacity()),
                new KeyValue(vBox.layoutXProperty(), vBox.getLayoutX()),
                new KeyValue(label.scaleXProperty(), label.getScaleX()),
                new KeyValue(label.scaleYProperty(), label.getScaleY()),
                new KeyValue(vBox.visibleProperty(), true)
            ),
            new KeyFrame(
                Duration.millis(100),
                new KeyValue(label.scaleXProperty(), 1.1),
                new KeyValue(label.scaleYProperty(), 1.1)
            ),
            new KeyFrame(
                Duration.millis(250),
                new KeyValue(vBox.opacityProperty(), 0.95),
                new KeyValue(vBox.layoutXProperty(), 20),
                new KeyValue(vBox.visibleProperty(), true)
            )
        );
        timeline.playFromStart();
    }

    private void animateOutSettings(VBox vBox, Label label) {
        if (vBox == null) return;
        final Timeline timeline = new Timeline(
            new KeyFrame(
                Duration.ZERO,
                new KeyValue(vBox.opacityProperty(), vBox.getOpacity()),
                new KeyValue(vBox.layoutXProperty(), vBox.getLayoutX()),
                new KeyValue(label.scaleXProperty(), label.getScaleX()),
                new KeyValue(label.scaleYProperty(), label.getScaleY()),
                new KeyValue(vBox.visibleProperty(), true)
            ),
            new KeyFrame(
                Duration.millis(100),
                new KeyValue(label.scaleXProperty(), 1),
                new KeyValue(label.scaleYProperty(), 1)
            ),
            new KeyFrame(
                Duration.millis(250),
                new KeyValue(vBox.opacityProperty(), 0),
                new KeyValue(vBox.layoutXProperty(), 0),
                new KeyValue(vBox.visibleProperty(), false)
            )
        );
        timeline.playFromStart();
    }

    /**
     * The different possible speech properties, used in adjustSpeaker .
     */
    private enum SpeechProperty {
        SPEED, PITCH, AMPLITUDE, GAP
    }

    //#endregion
}
