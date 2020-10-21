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
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import quinzical.interfaces.models.SceneHandler;

public abstract class StandardSceneController {

    @FXML
    protected AnchorPane background;

    @Inject
    protected SceneHandler sceneHandler;

    protected static void createScaleAnimation(Node node, int ms, double toSize) {
        ScaleTransition st = new ScaleTransition(Duration.millis(ms));
        st.setNode(node);
        st.setFromX(node.getScaleX());
        st.setFromY(node.getScaleY());
        st.setToX(toSize);
        st.setToY(toSize);
        st.playFromStart();
    }

    protected static void adjustBrightness(Node node, int ms, double toLevel) {
        ColorAdjust ca = new ColorAdjust();
        node.setEffect(ca);
        new Timeline(
            new KeyFrame(
                Duration.ZERO,
                new KeyValue(ca.brightnessProperty(), ca.getBrightness())
            ),
            new KeyFrame(
                Duration.millis(ms),
                new KeyValue(ca.brightnessProperty(), toLevel)
            )
        ).playFromStart();
    }

    protected static void createAnimation(int ms, double toLevel, Property<Number> property) {
        new Timeline(
            new KeyFrame(
                Duration.ZERO,
                new KeyValue(property, property.getValue())
            ),
            new KeyFrame(
                Duration.millis(ms),
                new KeyValue(property, toLevel)
            )
        ).playFromStart();
    }

    @FXML
    public final void initialize() {
        onLoad();
        refresh();
        this.background.getStyleClass().clear();
        this.background.getStyleClass().add(sceneHandler.getActiveTheme().name());
    }

    protected abstract void onLoad();


    protected void refresh() {
    }

}
