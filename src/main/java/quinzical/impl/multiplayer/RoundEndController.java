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

package quinzical.impl.multiplayer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class RoundEndController extends AbstractEndController {

    @FXML
    private Label lblCorrect;
    @FXML
    private Label lblMessage;
    @FXML
    private Label lblPoints;


    @Override
    protected final void initializeComponents() {

        final int added = activeGame.getMostRecentPoints();
        final int totalPoints = activeGame.getPoints();
        final boolean correct = added != 0;
        final String actualAns = activeGame.getTrueSolution();

        Platform.runLater(() -> {
            lblCorrect.setText(correct ? "CORRECT!" : "WRONG!");
            lblMessage.setText(correct ? "+" + added + " Points!" : "The correct answer was " + actualAns);
            lblPoints.setText("You have " + totalPoints + " points!");
        });
    }

}

