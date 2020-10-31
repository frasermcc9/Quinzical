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

    public final String getCode() {
        return code;
    }

    public final void setCode(final String code) {
        this.code = code;
    }

    public final ObservableList<String> getObservablePlayers() {
        return players;
    }

    public final void addPlayer(final String playerName) {
        this.players.add(playerName);
    }

    public final void removePlayer(final String playerName) {
        this.players.remove(playerName);
    }
}
