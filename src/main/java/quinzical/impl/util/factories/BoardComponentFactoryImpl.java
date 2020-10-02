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

package quinzical.impl.util.factories;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import quinzical.interfaces.factories.BoardComponentFactory;

/**
 * Factory to create base styles for programmatically added components.
 */
public class BoardComponentFactoryImpl implements BoardComponentFactory {
    /**
     * Creates a vbox with the required parameters that all VBoxes of the game
     * board conform to.
     *
     * @return a new VBox.
     */
    @Override
    public VBox createVbox() {
        return new VBox();
    }

    /**
     * Creates a button with the required parameters that all buttons of the game
     * board conform to.
     *
     * @return a new button.
     */
    @Override
    public Button createButton() {
        Button b = new Button();
        b.setTextFill(Color.rgb(242, 178, 2));
        b.setStyle("-fx-background-color: #2029d6;-fx-border-color: #000000;"
                + "-fx-border-width: 3px;-fx-border-insets: -1;-fx-font-weight: bold;-fx-font-size: "
                + "32;-fx-font-family: Impact;-fx-cursor: hand;");
        b.setEffect(new DropShadow());
        b.backgroundProperty()
                .bind(Bindings.when(b.hoverProperty())
                        .then(new Background(new BackgroundFill(Color.rgb(24, 31, 163), null, null)))
                        .otherwise(new Background(new BackgroundFill(Color.rgb(32, 41, 214), null, null))));

        return b;
    }

    /**
     * Creates a button with the required parameters that all labels of the game
     * board conform to.
     *
     * @return a new label.
     */
    @Override
    public Label createLabel() {
        Label lbl = new Label();
        lbl.setWrapText(true);
        lbl.setTextFill(Color.WHITE);
        lbl.setFont(new Font(30));
        lbl.setAlignment(Pos.CENTER);
        lbl.setTextAlignment(TextAlignment.CENTER);
        return lbl;
    }
}
