package quinzical.impl.multiplayer.models;

import io.socket.client.Socket;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import quinzical.impl.multiplayer.App;
import quinzical.impl.multiplayer.models.structures.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableArrayList;


public class ActiveGame {

    private static ActiveGame instance;
    private final ObservableList<Player> players = observableArrayList();
    private final Socket socket = SocketModel.getInstance().getSocket();

    private Question currentQuestion;
    private int points;

    public static ActiveGame getInstance() {
        if (instance == null) instance = new ActiveGame();
        return instance;
    }

    public static ActiveGame resetInstance() {
        instance = new ActiveGame();
        return instance;
    }


    public void setData(Object[] socketObjectData) {
        String solution = (String) socketObjectData[0];
        Integer points = (Integer) socketObjectData[1];
        updateUsersFromSocket((JSONArray) socketObjectData[2]);

        this.points = points;
        this.currentQuestion.setSolution(solution);
    }


    public void init() {
        socket.on("newQuestion", (objects -> {
            currentQuestion = new Question((String) objects[0], (String) objects[1]);
            Platform.runLater(() -> App.setRoot("game"));
        }));
        socket.on("roundOver", (objects) -> {
            setData(objects);
            Platform.runLater(() -> App.setRoot("round-end"));
        });

        socket.once("gameFinished", (objects) -> {
            updateUsersFromSocket((JSONArray) objects[0]);
            socket.off("newQuestion").off("roundOver").off("goNextRound");
            Platform.runLater(() -> App.setRoot("game-end"));
        });

        socket.on("goNextRound", objects -> socket.emit("readyToPlay"));


        socket.emit("readyToPlay");

    }


    public int getPoints() {
        return points;
    }

    public String getGivenSolution() {
        if (currentQuestion == null) {
            return "Error getting your answer.";
        }
        return currentQuestion.getGivenSolution();
    }

    public void setGivenSolution(String givenSolution) {
        this.currentQuestion.setGivenSolution(givenSolution);

    }

    public String getTrueSolution() {
        if (currentQuestion == null) {
            return "Error getting answer.";
        }
        return currentQuestion.getSolution();
    }

    public ObservableList<Player> getPlayers() {
        return players;
    }

    public String getQuestion() {
        return currentQuestion.getQuestion();
    }

    public String getPrompt() {
        return currentQuestion.getPrompt();
    }


    private void updateUsersFromSocket(JSONArray topPlayers) {
        List<JSONObject> parsedObjects = new ArrayList<>();
        for (int i = 0; i < topPlayers.length(); i++) {
            try {
                parsedObjects.add((JSONObject) topPlayers.get(i));
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        players.clear();
        players.addAll(parsedObjects.stream().map(o -> {
            try {
                return new Player(o.getInt("Points"), o.getString("Name"));
            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList()));
    }
}

