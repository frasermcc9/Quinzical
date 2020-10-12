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

package quinzical.impl.controllers;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import quinzical.impl.constants.GameScene;
import quinzical.impl.constants.Theme;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.models.structures.SpeakerMutator;

import java.util.Arrays;
import java.util.Collection;

/**
 * Controller class for the options screen.
 */
public class OptionsController {

    static final int DEFAULT_PITCH;
    static final int DEFAULT_SPEED;
    static final int DEFAULT_AMP;
    static final int DEFAULT_GAP;

    static {
        DEFAULT_PITCH = 50;
        DEFAULT_SPEED = 175;
        DEFAULT_AMP = 100;
        DEFAULT_GAP = 0;
    }

    @Inject
    private SceneHandler sceneHandler;
    @Inject
    private SpeakerMutator speakerMutator;
    @FXML
    private AnchorPane background;
    @FXML
    private ImageView imgBackground;
    @FXML
    private ComboBox<Theme> comboTheme;
    @FXML
    private Slider sliderSpeed;
    @FXML
    private Slider sliderGap;
    @FXML
    private Slider sliderAmp;
    @FXML
    private Slider sliderPitch;

    /**
     * Fired when the done button is pressed, goes back to the intro screen.
     */
    @FXML
    void btnDonePress(ActionEvent event) {
        sceneHandler.setActiveScene(GameScene.INTRO);
    }

    /**
     * Updates the scene when it is changed.
     */
    @FXML
    void onThemeChosen(ActionEvent event) {
        Theme theme = comboTheme.getValue();
        sceneHandler.fireBackgroundChange(theme.getImage());
    }

    /**
     * Runs a sample message of the speaker to show the user what it sounds like.
     */
    @FXML
    void onSampleClick(MouseEvent event) {
        speakerMutator.speak("Hello, welcome to Quinzical!");
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

    /**
     * Fired when the FXML is loaded, sets up the themes combobox for all the available themes
     * and sets up what happens when the background is changed.
     */
    @FXML
    void initialize() {
        assert background != null : "fx:id=\"background\" was not injected: check your FXML file 'options.fxml'.";
        assert imgBackground != null : "fx:id=\"imgBackground\" was not injected: check your FXML file 'options.fxml'.";
        assert comboTheme != null : "fx:id=\"comboTheme\" was not injected: check your FXML file 'options.fxml'.";

        Collection<Theme> list = Arrays.asList(Theme.values());
        comboTheme.getItems().addAll(list);
        comboTheme.setValue(Theme.MOUNTAINS);

        sliderSpeed.valueProperty().addListener(e -> adjustSpeaker(SpeechProperty.SPEED, (int) sliderSpeed.getValue()));
        sliderPitch.valueProperty().addListener(e -> adjustSpeaker(SpeechProperty.PITCH, (int) sliderPitch.getValue()));
        sliderAmp.valueProperty().addListener(e -> adjustSpeaker(SpeechProperty.AMPLITUDE, (int) sliderAmp.getValue()));
        sliderGap.valueProperty().addListener(e -> adjustSpeaker(SpeechProperty.GAP, (int) sliderGap.getValue()));

        sceneHandler.onBackgroundChange(img -> this.imgBackground.setImage(img));
    }

    /**
     * Adjusts the Speakers properties according to the value
     * and the property that is being changed.
     * 
     * @param sp the speech property that is being changed (pitch, speed, amplitude or gap)
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
     * The different possible speech properties, used in adjustSpeaker .
     */
    private enum SpeechProperty {
        SPEED, PITCH, AMPLITUDE, GAP
    }
}
