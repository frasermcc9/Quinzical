package quinzical;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quinzical.impl.bindings.MainModule;
import quinzical.impl.controllers.IntroController;
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.models.SceneHandler;

//mvn clean compile assembly:single

public class Entry extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Injector injector = Guice.createInjector(new MainModule(stage));
        SceneHandler sceneHandler = injector.getInstance(SceneHandler.class);

        //Load the views and controllers
        FXMLLoader introLoader = new FXMLLoader(getClass().getClassLoader().getResource("quinzical/impl/views/intro" +
            ".fxml"));
        Scene introScene = introLoader.load();
        IntroController introController = introLoader.getController();
        
        sceneHandler.setActiveScene(introScene);
        stage.setTitle("Quinzical");
        stage.show();
    }

}
