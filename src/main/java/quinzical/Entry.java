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

//mvn clean compile assembly:single

public class Entry extends Application {

    Injector injector = Guice.createInjector(new MainModule());

    @Override
    public void start(Stage stage) throws Exception {
        QuestionCollection t = injector.getInstance(QuestionCollection.class);

        //Load the views and controllers
        FXMLLoader introLoader = new FXMLLoader(getClass().getClassLoader().getResource("quinzical/impl/views/intro" +
            ".fxml"));
        Scene introScene = introLoader.load();
        IntroController introController = introLoader.getController();

        stage.setScene(introScene);
        stage.setTitle("Quinzical");
        stage.show();
    }

}
