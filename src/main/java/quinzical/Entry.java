package quinzical;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quinzical.impl.bindings.MainModule;
import quinzical.impl.constants.GameScene;
import quinzical.impl.controllers.GameController;
import quinzical.impl.controllers.IntroController;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.models.SceneRegistry;

//mvn clean compile assembly:single

public class Entry extends Application {

    SceneRegistry sceneRegistry;

    @Override
    public void start(Stage stage) throws Exception {

        Injector injector = Guice.createInjector(new MainModule(stage));
        SceneHandler sceneHandler = injector.getInstance(SceneHandler.class);

        // Load the views and controllers
        final String path = "quinzical/impl/views/";

        FXMLLoader introLoader = new FXMLLoader(getClass().getClassLoader().getResource(path + "intro.fxml"));
        introLoader.setControllerFactory(injector::getInstance);
        Scene introScene = introLoader.load();

        FXMLLoader gameLoader = new FXMLLoader(getClass().getClassLoader().getResource(path + "game.fxml"));
        gameLoader.setControllerFactory(injector::getInstance);
        Scene gameScene = gameLoader.load();

        // Register the scenes
        sceneRegistry = injector.getInstance(SceneRegistry.class);
        sceneRegistry.addScene(GameScene.INTRO, introScene);
        sceneRegistry.addScene(GameScene.GAME, gameScene);

        // Set the active scene to the intro
        sceneHandler.setActiveScene(GameScene.INTRO);

        // Show interface
        stage.setTitle("Quinzical");
        stage.show();
    }

}
