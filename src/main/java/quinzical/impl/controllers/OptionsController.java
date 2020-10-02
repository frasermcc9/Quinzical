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

public class OptionsController {

    static int DEFAULT_PITCH;
    static int DEFAULT_SPEED;
    static int DEFAULT_AMP;
    static int DEFAULT_GAP;

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

    @FXML
    void btnDonePress(ActionEvent event) {
        sceneHandler.setActiveScene(GameScene.INTRO);
    }

    @FXML
    void onThemeChosen(ActionEvent event) {
        Theme theme = comboTheme.getValue();
        sceneHandler.fireBackgroundChange(theme.getImage());
    }

    @FXML
    void onSampleClick(MouseEvent event) {
        speakerMutator.speak("Hello, welcome to the Quinzical!");
    }

    @FXML
    void pitchDefault() {
        speakerMutator.setPitch(DEFAULT_PITCH);
        sliderPitch.setValue(DEFAULT_PITCH);
    }

    @FXML
    void speedDefault() {
        speakerMutator.setSpeed(DEFAULT_SPEED);
        sliderSpeed.setValue(DEFAULT_SPEED);
    }

    @FXML
    void ampDefault() {
        speakerMutator.setAmplitude(DEFAULT_AMP);
        sliderAmp.setValue(DEFAULT_AMP);
    }

    @FXML
    void gapDefault() {
        speakerMutator.setGap(DEFAULT_GAP);
        sliderGap.setValue(DEFAULT_GAP);
    }

    @FXML
    void initialize() {
        assert background != null : "fx:id=\"background\" was not injected: check your FXML file 'options.fxml'.";
        assert imgBackground != null : "fx:id=\"imgBackground\" was not injected: check your FXML file 'options.fxml'.";
        assert comboTheme != null : "fx:id=\"comboTheme\" was not injected: check your FXML file 'options.fxml'.";

        Collection<Theme> list = Arrays.asList(Theme.values());
        comboTheme.getItems().addAll(list);
        comboTheme.setValue(Theme.MOUNTAINS);

        sliderSpeed.valueProperty().addListener(e -> {
            adjustSpeaker(SpeechProperty.SPEED, (int) sliderSpeed.getValue());
        });
        sliderPitch.valueProperty().addListener(e -> {
            adjustSpeaker(SpeechProperty.PITCH, (int) sliderPitch.getValue());
        });
        sliderAmp.valueProperty().addListener(e -> {
            adjustSpeaker(SpeechProperty.AMPLITUDE, (int) sliderAmp.getValue());
        });
        sliderGap.valueProperty().addListener(e -> {
            adjustSpeaker(SpeechProperty.GAP, (int) sliderGap.getValue());
        });

        sceneHandler.onBackgroundChange(img -> this.imgBackground.setImage(img));
    }

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

    private enum SpeechProperty {
        SPEED, PITCH, AMPLITUDE, GAP
    }
}
