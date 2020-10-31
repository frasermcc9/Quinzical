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
    private int mostRecentPoints;
    private int duration;

    public final ActiveGame reset() {
        this.players = observableArrayList();
        this.points = 0;
        currentQuestion = new Question(null, null);
        return this;
    }

    @Override
    public final int getQuestionDuration() {
        return this.duration;
    }

    @Override
    public final void setData(final Object[] socketObjectData) {
        final String solution = (String) socketObjectData[0];
        final Integer points = (Integer) socketObjectData[1];
        updateUsersFromSocket((JSONArray) socketObjectData[2]);
        final Integer mostRecentPoints = (Integer) socketObjectData[3];

        this.points = points;
        this.mostRecentPoints = mostRecentPoints;
        this.currentQuestion.setSolution(solution);
    }


    @Override
    public final void init(final int duration) {
        final Socket socket = socketModel.getSocket();

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
            Platform.runLater(() -> sceneHandler.setActiveScene(GameScene.MULTI_GAME_END));
        });

        socket.on("goNextRound", objects -> socket.emit("readyToPlay"));

        this.duration = duration;

        socket.emit("readyToPlay");

    }


    @Override
    public final int getPoints() {
        return points;
    }

    @Override
    public final String getGivenSolution() {
        if (currentQuestion == null) {
            return "Error getting your answer.";
        }
        return currentQuestion.getGivenSolution();
    }

    @Override
    public final void setGivenSolution(final String givenSolution) {
        this.currentQuestion.setGivenSolution(givenSolution);

    }

    @Override
    public final String getTrueSolution() {
        if (currentQuestion == null) {
            return "Error getting answer.";
        }
        return currentQuestion.getSolution();
    }

    @Override
    public final ObservableList<Player> getPlayers() {
        return players;
    }

    @Override
    public final String getQuestion() {
        return currentQuestion.getQuestion();
    }

    @Override
    public final String getPrompt() {
        return currentQuestion.getPrompt();
    }

    private void updateUsersFromSocket(final JSONArray topPlayers) {
        final List<JSONObject> parsedObjects = new ArrayList<>();
        for (int i = 0; i < topPlayers.length(); i++) {
            try {
                parsedObjects.add((JSONObject) topPlayers.get(i));
            } catch (final JSONException jsonException) {
                jsonException.printStackTrace();
            }
        }
        players.clear();
        players.addAll(parsedObjects.stream().map(o -> {
            try {
                return new Player(o.getInt("Points"), o.getString("Name"));
            } catch (final JSONException jsonException) {
                jsonException.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList()));
    }

    public final int getMostRecentPoints() {
        return mostRecentPoints;
    }
}

