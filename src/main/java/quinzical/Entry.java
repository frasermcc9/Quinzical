package quinzical;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quinzical.impl.constants.GameScene;
import quinzical.impl.constants.Theme;
import quinzical.impl.util.bindings.MainModule;
import quinzical.interfaces.models.GameModelSaver;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.models.SceneRegistry;

import java.io.IOException;

//mvn clean compile assembly:single

public class Entry extends Application {

    SceneRegistry sceneRegistry;

    Injector injector;

    private Scene loadFXML(String fxml) throws IOException {
        final String path = "quinzical/impl/views/";
        FXMLLoader loader = new FXMLLoader(Entry.class.getClassLoader().getResource(path + fxml + ".fxml"));
        loader.setControllerFactory(injector::getInstance);
        return loader.load();
    }

    @Override
    public void start(Stage stage) throws Exception {

        // Create the injection container
        injector = Guice.createInjector(new MainModule(stage));
        SceneHandler sceneHandler = injector.getInstance(SceneHandler.class);

        // Register the scenes
        sceneRegistry = injector.getInstance(SceneRegistry.class);
        sceneRegistry.addScene(GameScene.INTRO, loadFXML("intro"));
        sceneRegistry.addScene(GameScene.GAME, loadFXML("game"));
        sceneRegistry.addScene(GameScene.GAME_QUESTION, loadFXML("gamequestion"));
        sceneRegistry.addScene(GameScene.OPTIONS, loadFXML("options"));


        // Set the active scene to the intro
        sceneHandler.setActiveScene(GameScene.INTRO);

        //set the background
        sceneHandler.fireBackgroundChange(Theme.MOUNTAINS.getImage());

        // Show interface
        stage.setTitle("Quinzical");
        stage.setResizable(false);
        stage.show();

        stage.setOnCloseRequest(e -> {
            GameModelSaver model = injector.getInstance(GameModelSaver.class);
            try {
                model.saveQuestionsToDisk();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

}
