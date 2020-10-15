package quinzical.impl.multiplayer.models;

import javafx.collections.ObservableList;

import static javafx.collections.FXCollections.observableArrayList;

public class MultiplayerGame {

    private static MultiplayerGame instance;
    private String code;
    private final ObservableList<String> players;



    private MultiplayerGame() {
        players = observableArrayList();
    }

    public static MultiplayerGame getInstance() {
        if (instance == null) {
            instance = new MultiplayerGame();
        }
        return instance;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ObservableList<String> getObservablePlayers() {
        return players;
    }

    public void addPlayer(String playerName) {
        this.players.add(playerName);
    }

    public void removePlayer(String playerName) {
        this.players.remove(playerName);
    }
}
