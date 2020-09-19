package quinzical.impl.bindings;

import com.google.inject.AbstractModule;
import javafx.stage.Stage;
import quinzical.impl.models.GameModelImpl;
import quinzical.impl.models.QuestionCollectionImpl;
import quinzical.impl.models.SceneHandlerImpl;
import quinzical.impl.models.SceneRegistryImpl;
import quinzical.impl.strategies.questiongenerator.QuestionGeneratorStrategyFactoryImpl;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.models.SceneRegistry;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategyFactory;

public class MainModule extends AbstractModule {

    private final Stage stage;

    public MainModule(Stage stage) {
        this.stage = stage;
    }

    @Override
    protected void configure() {
        bind(QuestionCollection.class).to(QuestionCollectionImpl.class);
        bind(GameModel.class).to(GameModelImpl.class);

        bind(SceneHandler.class).to(SceneHandlerImpl.class);
        bind(SceneRegistry.class).to(SceneRegistryImpl.class);

        bind(QuestionGeneratorStrategyFactory.class).to(QuestionGeneratorStrategyFactoryImpl.class);

        bind(Stage.class).toInstance(stage);
    }
}

