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
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.multiplayer.SocketModel;
import quinzical.interfaces.multiplayer.XpClass;
import quinzical.interfaces.multiplayer.XpClassFactory;

import java.io.IOException;

public class ProfileController extends AbstractAlertController {

    @Inject
    private SceneHandler sceneHandler;
    @Inject
    private SocketModel socketModel;
    @Inject
    @Named("socketUrl")
    private String socketUrl;
    @Inject
    private XpClassFactory xpClassFactory;

    @FXML
    private JFXTextField txtSearch;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblLevel;
    @FXML
    private Label lblCorrect;
    @FXML
    private JFXProgressBar barXp;
    @FXML
    private Label lblXp;

    @FXML
    void btnCancel() {
        new Thread(() -> sceneHandler.setActiveScene(GameScene.MULTI_MENU)).start();
    }

    @FXML
    void btnSearch() {
        new Thread(() -> getPlayerInfo(txtSearch.getText().trim())).start();
    }

    @Override
    protected void onLoad() {
        getPlayerInfo(socketModel.getName());
    }

    @Override
    protected void usePassedData(final String passedInfo) {
        final String query;
        if (passedInfo == null) {
            query = socketModel.getName();
        } else {
            query = passedInfo;
        }
        new Thread(() -> getPlayerInfo(query)).start();
    }

    public void getPlayerInfo(final String player) {
        final OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
            .url(socketUrl + "/player/" + player)
            .build();

        try {
            final Response response = client.newCall(request).execute();
            if (response.code() != 200) {
                createAlert("Player not found", "The player you are searching for does not appear to exist.");
                return;
            }

            ResponseBody responseBody = response.body();
            assert responseBody != null;
            JSONObject object = new JSONObject(responseBody.string());

            final String name = object.getString("name");
            final int xp = object.getInt("xp");
            final int correct = object.getInt("correct");
            final int incorrect = object.getInt("incorrect");
            final XpClass xpUtils = xpClassFactory.createXp(xp);
            final int xpThrough = xpUtils.xpThroughLevel();
            final int outOf = xpUtils.xpDeltaBetweenLevels();

            Platform.runLater(() -> {
                lblUsername.setText(name);
                lblCorrect.setText(correct + " / " + (correct + incorrect));
                lblLevel.setText(xpUtils.getLevel() + "");
                lblXp.setText(xpThrough + " / " + outOf);
                barXp.setProgress((double) xpThrough / outOf);
            });

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
