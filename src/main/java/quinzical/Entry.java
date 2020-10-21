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

package quinzical;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import quinzical.impl.constants.GameScene;
import quinzical.impl.constants.Theme;
import quinzical.impl.util.bindings.MainModule;
import quinzical.interfaces.models.GameModelSaver;
import quinzical.interfaces.models.SceneHandler;

import java.io.IOException;


/**
 * The "main" class that is run to start the application
 */
public class Entry extends Application {

    Injector injector;

    /**
     * Loads the inputted FXML file
     *
     * @param fxml - the FXML file name to load
     * @return - the scene created by loading the FXML file
     * @throws IOException - thrown if the file does not exist
     */
    private Scene loadFXML(String fxml) throws IOException {
        final String path = "quinzical/impl/views/";
        FXMLLoader loader = new FXMLLoader(Entry.class.getClassLoader().getResource(path + fxml + ".fxml"));
        loader.setControllerFactory(injector::getInstance);
        return loader.load();
    }

    /**
     * Sets up all the scenes for the application as well as attempt to save the game when the application is closed.
     */
    @Override
    public void start(Stage stage) {

        // Create the injection container
        injector = Guice.createInjector(new MainModule());
        SceneHandler sceneHandler = injector.getInstance(SceneHandler.class);

        //set the background
        sceneHandler.fireBackgroundChange(Theme.FIELDS);

        // Set the active scene to the intro
        sceneHandler.setActiveScene(GameScene.INTRO);

        sceneHandler.cacheScenes();
        
        // Show interface
        stage.setScene(injector.getInstance(Scene.class));
        stage.setTitle("Quinzical");
        stage.setMinWidth(1280);
        stage.setMinHeight(720);
        stage.show();

        stage.getIcons().add(new Image(Entry.class.getResourceAsStream("APP_ICON_TWO.png")));

        stage.setOnCloseRequest(e -> {
            GameModelSaver model = injector.getInstance(GameModelSaver.class);
            Platform.runLater(() -> {
                try {
                    model.saveGame();
                    System.exit(0);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });

        });
    }

}
