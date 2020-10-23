package quinzical.impl.multiplayer.models;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.socket.client.Socket;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import quinzical.impl.constants.GameScene;
import quinzical.impl.multiplayer.models.structures.Question;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.multiplayer.ActiveGame;
import quinzical.interfaces.multiplayer.SocketModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableArrayList;

@Singleton
public class ActiveGameImpl implements ActiveGame {

    private ObservableList<Player> players = observableArrayList();
    @Inject
    private SceneHandler sceneHandler;
    @Inject
    private SocketModel socketModel;

    private Question currentQuestion;
    private int points;
    private int duration;

    public ActiveGame reset() {
        this.players = observableArrayList();
        this.points = 0;
        currentQuestion = new Question(null, null);
        return this;
    }

    @Override
    public int getQuestionDuration() {
        return this.duration;
    }

    @Override
    public void setData(Object[] socketObjectData) {
        String solution = (String) socketObjectData[0];
        Integer points = (Integer) socketObjectData[1];
        updateUsersFromSocket((JSONArray) socketObjectData[2]);

        this.points = points;
        this.currentQuestion.setSolution(solution);
    }


    @Override
    public void init(int duration) {
        Socket socket = socketModel.getSocket();

        socket.on("newQuestion", (objects -> {
            currentQuestion = new Question((String) objects[0], (String) objects[1]);
            Platform.runLater(() -> sceneHandler.setActiveScene(GameScene.MULTI_GAME));
        }));
        socket.on("roundOver", (objects) -> {
            setData(objects);
            Platform.runLater(() -> sceneHandler.setActiveScene(GameScene.MULTI_ROUND_END));
        });

        socket.once("gameFinished", (objects) -> {
            updateUsersFromSocket((JSONArray) objects[0]);
            socket.off("newQuestion").off("roundOver").off("goNextRound");
            Platform.runLater(() -> sceneHandler.setActiveScene(GameScene.MULTI_GAME__END));
        });

        socket.on("goNextRound", objects -> socket.emit("readyToPlay"));

        this.duration = duration;

        socket.emit("readyToPlay");

    }


    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public String getGivenSolution() {
        if (currentQuestion == null) {
            return "Error getting your answer.";
        }
        return currentQuestion.getGivenSolution();
    }

    @Override
    public void setGivenSolution(String givenSolution) {
        this.currentQuestion.setGivenSolution(givenSolution);

    }

    @Override
    public String getTrueSolution() {
        if (currentQuestion == null) {
            return "Error getting answer.";
        }
        return currentQuestion.getSolution();
    }

    @Override
    public ObservableList<Player> getPlayers() {
        return players;
    }

    @Override
    public String getQuestion() {
        return currentQuestion.getQuestion();
    }

    @Override
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

