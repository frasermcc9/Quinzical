package quinzical;

import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;
import quinzical.impl.bindings.MainModule;
import quinzical.interfaces.models.QuestionCollection;

public class Entry extends Application {

    Injector injector = Guice.createInjector(new MainModule());

    @Override
    public void start(Stage stage) throws Exception {
        QuestionCollection t = injector.getInstance(QuestionCollection.class);

        stage.setTitle("Jeopardy");
        stage.show();
    }

}
