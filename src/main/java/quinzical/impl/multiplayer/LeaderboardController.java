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
import com.google.inject.name.Named;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import quinzical.impl.constants.GameScene;
import quinzical.impl.controllers.AbstractSceneController;
import quinzical.interfaces.models.SceneHandler;

import java.io.IOException;

import static javafx.collections.FXCollections.observableArrayList;

public class LeaderboardController extends AbstractSceneController {

    @Inject
    @Named("socketUrl")
    private String socketUrl;
    @Inject
    private SceneHandler sceneHandler;

    @FXML
    private JFXTreeTableView<PlayerData> tableBrowse;

    private ObservableList<PlayerData> playerList;

    @FXML
    void btnCancel() {
        new Thread(() -> sceneHandler.setActiveScene(GameScene.MULTI_MENU)).start();
    }

    @FXML
    void btnProfile() {
        final String name = tableBrowse.getSelectionModel().getSelectedItem().getValue().getName();
        new Thread(() -> sceneHandler.setActiveScene(GameScene.MULTI_PROFILE).passData(name)).start();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onLoad() {
        playerList = observableArrayList();
        new Thread(this::createLeaderboard).start();

        final JFXTreeTableColumn<PlayerData, String> nameCol = new JFXTreeTableColumn<>("Name");
        final JFXTreeTableColumn<PlayerData, Number> xpCol = new JFXTreeTableColumn<>("Experience");

        Platform.runLater(() -> {
            nameCol.setCellValueFactory(v -> v.getValue().getValue().nameProperty());
            xpCol.setCellValueFactory(v -> v.getValue().getValue().xpProperty());

            tableBrowse.setColumnResizePolicy(TreeTableView.CONSTRAINED_RESIZE_POLICY);

            final TreeItem<PlayerData> root = new RecursiveTreeItem<>(playerList, RecursiveTreeObject::getChildren);
            tableBrowse.setRoot(root);
            tableBrowse.setShowRoot(false);

            tableBrowse.getColumns().setAll(nameCol, xpCol);
        });
    }

    private void createLeaderboard() {
        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
            .url(socketUrl + "/leaders")
            .build();

        try {
            final Response response = client.newCall(request).execute();
            final ResponseBody responseBody = response.body();

            assert responseBody != null;
            final JSONArray array = new JSONArray(responseBody.string());

            for (int i = 0; i < array.length(); i++) {
                final JSONObject player = array.getJSONObject(i);
                playerList.add(new PlayerData(player.getString("name"), player.getInt("XP")));
            }


        } catch (final IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private static class PlayerData extends RecursiveTreeObject<PlayerData> {
        private final SimpleStringProperty name;
        private final SimpleIntegerProperty xp;

        public PlayerData(final String name, final int xp) {
            this.name = new SimpleStringProperty(name);
            this.xp = new SimpleIntegerProperty(xp);
        }

        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public int getXp() {
            return xp.get();
        }

        public SimpleIntegerProperty xpProperty() {
            return xp;
        }
    }

}
