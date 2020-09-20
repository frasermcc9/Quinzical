package quinzical.interfaces.factories;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public interface BoardComponentFactory {
    VBox createVbox();

    Button createButton();

    Label createLabel();
}
