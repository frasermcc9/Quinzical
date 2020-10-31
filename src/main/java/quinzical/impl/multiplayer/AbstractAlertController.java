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


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import quinzical.impl.controllers.AbstractSceneController;

public abstract class AbstractAlertController extends AbstractSceneController {

    @FXML
    private StackPane alertPane;
    @FXML
    private JFXSpinner progressIndicator;
    @FXML
    private Label lblProgress;

    protected void createAlert(final String title, final String alertContent, final Runnable runnable) {
        final JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(title));
        content.setBody(new Text(alertContent));
        final JFXDialog dialog = new JFXDialog(alertPane, content, JFXDialog.DialogTransition.CENTER);
        final JFXButton button = new JFXButton("Okay");
        dialog.setOverlayClose(false);
        button.getStyleClass().add("material-button-small");
        button.setOnAction(event -> {
            dialog.setOnDialogClosed(e -> {
                if (alertPane.getChildren().stream().noneMatch(Node::isVisible)) {
                    alertPane.setMouseTransparent(true);
                    Platform.runLater(runnable);
                }
            });
            dialog.close();
        });
        content.setActions(button);
        alertPane.setMouseTransparent(false);
        Platform.runLater(dialog::show);
    }

    protected void createAlert(final String title, final String alertContent) {
        createAlert(title, alertContent, () -> {
        });
    }

    protected void setProgressVisible(final boolean visible) {
        progressIndicator.setVisible(visible);
        lblProgress.setVisible(visible);
    }
}
