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
import com.google.inject.Injector;
import com.jfoenix.controls.JFXMasonryPane;
import io.socket.client.Socket;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import org.json.JSONArray;
import org.json.JSONException;
import quinzical.impl.constants.GameScene;
import quinzical.impl.controllers.components.TileController;
import quinzical.impl.models.structures.FxmlInfo;
import quinzical.impl.multiplayer.models.MultiplayerGame;
import quinzical.interfaces.multiplayer.SocketModel;
import quinzical.interfaces.multiplayer.XpClass;
import quinzical.interfaces.multiplayer.XpClassFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractWaitController extends AbstractAlertController {
    @Inject
    protected SocketModel socketModel;

    @FXML
    protected Label lblCode;
    @FXML
    protected Label lblPlayers;
    @FXML
    protected JFXMasonryPane masonryPane;
    @FXML
    protected StackPane alertPane;

    @Inject
    Injector injector;
    @Inject
    private XpClassFactory xpClassFactory;

    final void loadWaitScreen() {

        final Socket socket = socketModel.getSocket();
        
        lblCode.setText(MultiplayerGame.getInstance().getCode());


        socket.on("interrupt", objects -> Platform.runLater(() -> {
            createAlert("The Host Disconnected", "The host quit the game. You have been returned to the menu.",
                () -> sceneHandler.setActiveScene(GameScene.MULTI_MENU));
        }));

        socket.on("playersChange", objects -> Platform.runLater(() -> {

            final List<String> nameList = new ArrayList<>();
            final List<XpClass> xpList = new ArrayList<>();

            final JSONArray nameArray = (JSONArray) objects[0];
            final JSONArray xpArray = (JSONArray) objects[1];
            for (int i = 0; i < nameArray.length(); i++) {
                try {
                    nameList.add(nameArray.getString(i));
                    xpList.add(xpClassFactory.createXp(xpArray.getInt(i)));
                } catch (final JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }

            final List<Parent> children = new ArrayList<>();

            for (int i = 0; i < nameList.size(); i++) {
                try {
                    final FxmlInfo<TileController> fxmlInfo = FxmlInfo.loadFXML("components/lobby-player", injector);
                    final Parent p = fxmlInfo.getParent();
                    children.add(p);
                    fxmlInfo.getController().setContent(nameList.get(i), xpList.get(i).getLevel() + "");
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(() -> {
                masonryPane.getChildren().clear();
                masonryPane.getChildren().addAll(children);
                Platform.runLater(() -> masonryPane.requestLayout());
                lblPlayers.setText(masonryPane.getChildren().size() + "/" + objects[2]);
            });
        }));
    }

    /**
     * optional hook
     */
    protected void addListeners() {

    }

    @Override
    protected final void onLoad() {
        loadWaitScreen();
        addListeners();
        socketModel.getSocket().emit("clientReady");
    }
}
