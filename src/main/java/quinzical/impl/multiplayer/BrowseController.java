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
    final void btnCancel() {
        sceneHandler.setActiveScene(GameScene.MULTI_MENU);
    }

    @FXML
    final void btnOk() {
        final GameData gameData = tableBrowse.getSelectionModel().getSelectedItem().getValue();

        final Socket socket = socketModel.getSocket();

        socket.emit("joinGameRequest", socketModel.getName(), gameData.getCode());
        socket.once("joinGameNotification", (arg) -> {
            if (arg[0].equals(false)) {
                createAlert("Unable to Join Game", "You were unable to join the game.");

            } else {
                final JSONArray jsonArray = (JSONArray) arg[1];
                final MultiplayerGame mg = MultiplayerGame.getInstance();
                mg.setCode(gameData.getCode());
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        mg.addPlayer((String) jsonArray.get(i));
                    } catch (final JSONException e) {
                        e.printStackTrace();
                    }
                }
                Platform.runLater(() -> sceneHandler.setActiveScene(GameScene.MULTI_PLAYER_WAIT));

            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final void onLoad() {

        final JFXTreeTableColumn<GameData, String> hostCol = new JFXTreeTableColumn<>("Host");
        final JFXTreeTableColumn<GameData, String> playersCol = new JFXTreeTableColumn<>("Players");
        final JFXTreeTableColumn<GameData, String> questionsCol = new JFXTreeTableColumn<>("Questions");
        final JFXTreeTableColumn<GameData, String> timeCol = new JFXTreeTableColumn<>("Time to Answer");
        final JFXTreeTableColumn<GameData, String> codeCol = new JFXTreeTableColumn<>("Code");

        final ObservableList<GameData> list = observableArrayList();

        Platform.runLater(() -> {
            hostCol.setCellValueFactory(v -> v.getValue().getValue().hostProperty());
            playersCol.setCellValueFactory(v -> v.getValue().getValue().playersProperty());
            questionsCol.setCellValueFactory(v -> v.getValue().getValue().questionsProperty());
            timeCol.setCellValueFactory(v -> v.getValue().getValue().timeProperty());
            codeCol.setCellValueFactory(param -> param.getValue().getValue().codeProperty());

            tableBrowse.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

            final TreeItem<GameData> root = new RecursiveTreeItem<>(list, RecursiveTreeObject::getChildren);
            tableBrowse.setRoot(root);
            tableBrowse.setShowRoot(false);
            tableBrowse.getColumns().setAll(hostCol, playersCol, questionsCol, timeCol, codeCol);
        });

        final Socket socket = socketModel.getSocket();

        socket.emit("browseGames");
        socket.once("browseGameDataLoaded", (args) -> {
            final JSONArray array = ((JSONArray) args[0]);
            final int size = array.length();
            for (int i = 0; i < size; i++) {
                try {
                    final JSONObject a = array.getJSONObject(i);
                    final String code = a.getString("code");
                    final String host = a.getString("host");
                    final String questions = a.getString("questions");
                    final String currentPlayers = a.getString("currentPlayers");
                    final String maxPlayers = a.getString("maxPlayers");
                    final String timePerQuestion = a.getString("timePerQuestion");

                    final GameData gameData = new GameData(host, currentPlayers + "/" + maxPlayers, questions,
                        timePerQuestion, code);

                    list.add(gameData);

                } catch (final JSONException e) {
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

        public GameData(final String host, final String players, final String questions, final String time, final String code) {
            this.host = new SimpleStringProperty(host);
            this.players = new SimpleStringProperty(players);
            this.questions = new SimpleStringProperty(questions);
            this.time = new SimpleStringProperty(time);
            this.code = new SimpleStringProperty(code);
        }

        public final String getHost() {
            return host.get();
        }

        public final void setHost(final String host) {
            this.host.set(host);
        }

        public final SimpleStringProperty hostProperty() {
            return host;
        }

        public final String getPlayers() {
            return players.get();
        }

        public final void setPlayers(final String players) {
            this.players.set(players);
        }

        public final SimpleStringProperty playersProperty() {
            return players;
        }

        public final String getQuestions() {
            return questions.get();
        }

        public final void setQuestions(final String questions) {
            this.questions.set(questions);
        }

        public final SimpleStringProperty questionsProperty() {
            return questions;
        }

        public final String getTime() {
            return time.get();
        }

        public final void setTime(final String time) {
            this.time.set(time);
        }

        public final SimpleStringProperty timeProperty() {
            return time;
        }

        public final String getCode() {
            return code.get();
        }

        public final void setCode(final String code) {
            this.code.set(code);
        }

        public final SimpleStringProperty codeProperty() {
            return code;
        }
    }

}

