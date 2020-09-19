package quinzical.impl.bindings;

import com.google.inject.AbstractModule;
import javafx.stage.Stage;
import quinzical.impl.models.QuestionCollectionImpl;
import quinzical.impl.models.SceneHandlerImpl;
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.models.SceneHandler;

public class MainModule extends AbstractModule {

    private final Stage stage;

    public MainModule(Stage stage) {
        this.stage = stage;
    }

    @Override
    protected void configure() {
        bind(QuestionCollection.class).to(QuestionCollectionImpl.class);
        bind(SceneHandler.class).to(SceneHandlerImpl.class);
        bind(Stage.class).toInstance(stage);
    }
}
