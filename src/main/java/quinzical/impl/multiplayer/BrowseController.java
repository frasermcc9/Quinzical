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

import com.google.inject.Inject;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.socket.client.Socket;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import quinzical.impl.constants.GameScene;
import quinzical.impl.multiplayer.models.MultiplayerGame;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.multiplayer.SocketModel;

import static javafx.collections.FXCollections.observableArrayList;

public class BrowseController extends AbstractAlertController {

    @Inject
    private SocketModel socketModel;
    @Inject
    private SceneHandler sceneHandler;

    @FXML
    private JFXTreeTableView<GameData> tableBrowse;

    @FXML
    void btnCancel() {
        sceneHandler.setActiveScene(GameScene.MULTI_MENU);
    }

    @FXML
    void btnOk() {
        GameData gameData = tableBrowse.getSelectionModel().getSelectedItem().getValue();

        Socket socket = socketModel.getSocket();

        socket.emit("joinGameRequest", socketModel.getName(), gameData.getCode());
        socket.once("joinGameNotification", (arg) -> {
            if (arg[0].equals(false)) {
                createAlert("Unable to Join Game", "You were unable to join the game.");

            } else {
                JSONArray jsonArray = (JSONArray) arg[1];
                MultiplayerGame mg = MultiplayerGame.getInstance();
                mg.setCode(gameData.getCode());
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        mg.addPlayer((String) jsonArray.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Platform.runLater(() -> sceneHandler.setActiveScene(GameScene.MULTI_PLAYER_WAIT));

            }
        });
    }

    @Override
    protected void onLoad() {

        JFXTreeTableColumn<GameData, String> hostCol = new JFXTreeTableColumn<>("Host");
        JFXTreeTableColumn<GameData, String> playersCol = new JFXTreeTableColumn<>("Players");
        JFXTreeTableColumn<GameData, String> questionsCol = new JFXTreeTableColumn<>("Questions");
        JFXTreeTableColumn<GameData, String> timeCol = new JFXTreeTableColumn<>("Time to Answer");
        JFXTreeTableColumn<GameData, String> codeCol = new JFXTreeTableColumn<>("Code");

        ObservableList<GameData> list = observableArrayList();

        Platform.runLater(() -> {
            hostCol.setCellValueFactory(v -> v.getValue().getValue().hostProperty());
            playersCol.setCellValueFactory(v -> v.getValue().getValue().playersProperty());
            questionsCol.setCellValueFactory(v -> v.getValue().getValue().questionsProperty());
            timeCol.setCellValueFactory(v -> v.getValue().getValue().timeProperty());
            codeCol.setCellValueFactory(param -> param.getValue().getValue().codeProperty());

            tableBrowse.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

            TreeItem<GameData> root = new RecursiveTreeItem<>(list, RecursiveTreeObject::getChildren);
            tableBrowse.setRoot(root);
            tableBrowse.setShowRoot(false);
            tableBrowse.getColumns().setAll(hostCol, playersCol, questionsCol, timeCol, codeCol);
        });

        Socket socket = socketModel.getSocket();

        socket.emit("browseGames");
        socket.once("browseGameDataLoaded", (args) -> {
            JSONArray array = ((JSONArray) args[0]);
            int size = array.length();
            for (int i = 0; i < size; i++) {
                try {
                    JSONObject a = array.getJSONObject(i);
                    String code = a.getString("code");
                    String host = a.getString("host");
                    String questions = a.getString("questions");
                    String currentPlayers = a.getString("currentPlayers");
                    String maxPlayers = a.getString("maxPlayers");
                    String timePerQuestion = a.getString("timePerQuestion");

                    GameData gameData = new GameData(host, currentPlayers + "/" + maxPlayers, questions,
                        timePerQuestion, code);

                    list.add(gameData);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        });
    }

    private static class GameData extends RecursiveTreeObject<GameData> {
        private final SimpleStringProperty host;
        private final SimpleStringProperty players;
        private final SimpleStringProperty questions;
        private final SimpleStringProperty time;
        private final SimpleStringProperty code;

        public GameData(String host, String players, String questions, String time, String code) {
            this.host = new SimpleStringProperty(host);
            this.players = new SimpleStringProperty(players);
            this.questions = new SimpleStringProperty(questions);
            this.time = new SimpleStringProperty(time);
            this.code = new SimpleStringProperty(code);
        }

        public String getHost() {
            return host.get();
        }

        public void setHost(String host) {
            this.host.set(host);
        }

        public SimpleStringProperty hostProperty() {
            return host;
        }

        public String getPlayers() {
            return players.get();
        }

        public void setPlayers(String players) {
            this.players.set(players);
        }

        public SimpleStringProperty playersProperty() {
            return players;
        }

        public String getQuestions() {
            return questions.get();
        }

        public void setQuestions(String questions) {
            this.questions.set(questions);
        }

        public SimpleStringProperty questionsProperty() {
            return questions;
        }

        public String getTime() {
            return time.get();
        }

        public void setTime(String time) {
            this.time.set(time);
        }

        public SimpleStringProperty timeProperty() {
            return time;
        }

        public String getCode() {
            return code.get();
        }

        public void setCode(String code) {
            this.code.set(code);
        }

        public SimpleStringProperty codeProperty() {
            return code;
        }
    }

}

