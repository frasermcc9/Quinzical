package quinzical.impl.multiplayer.models;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class Player extends RecursiveTreeObject<Player> {
    private final SimpleStringProperty score;
    private final SimpleStringProperty name;

    public Player(int score, String name) {
        this.score = new SimpleStringProperty(score + "");
        this.name = new SimpleStringProperty(name);
    }

    public SimpleStringProperty scoreProperty() {
        return score;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }
}
