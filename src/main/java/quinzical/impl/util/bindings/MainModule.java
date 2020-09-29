package quinzical.impl.util.bindings;

import com.google.inject.AbstractModule;
import javafx.stage.Stage;
import quinzical.impl.models.GameModelImpl;
import quinzical.impl.models.QuestionCollectionImpl;
import quinzical.impl.models.SceneHandlerImpl;
import quinzical.impl.models.SceneRegistryImpl;
import quinzical.impl.models.structures.SpeakerManager;
import quinzical.impl.models.structures.UserScoreImpl;
import quinzical.impl.models.structures.WindowsSpeakerManager;
import quinzical.impl.util.factories.BoardComponentFactoryImpl;
import quinzical.impl.util.strategies.boardloader.BoardLoaderStrategyFactoryImpl;
import quinzical.impl.util.strategies.objectreader.ObjectReaderStrategyFactoryImpl;
import quinzical.impl.util.strategies.questiongenerator.QuestionGeneratorStrategyFactoryImpl;
import quinzical.impl.util.strategies.questionverifier.QuestionVerifierFactoryImpl;
import quinzical.impl.util.strategies.textnormaliser.TextNormaliserStrategyFactory;
import quinzical.interfaces.factories.BoardComponentFactory;
import quinzical.interfaces.models.*;
import quinzical.interfaces.models.structures.Speaker;
import quinzical.interfaces.models.structures.SpeakerMutator;
import quinzical.interfaces.models.structures.UserScore;
import quinzical.interfaces.strategies.boardloader.BoardLoaderStrategyFactory;
import quinzical.interfaces.strategies.objectreader.ObjectReaderStrategyFactory;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategyFactory;
import quinzical.interfaces.strategies.questionverifier.QuestionVerifierFactory;
import quinzical.interfaces.strategies.textnormaliser.TextNormaliserFactory;

/**
 * This class binds concrete implementations to interfaces. It decides, when a class requires an interface injection
 * with the @Inject annotation, which concrete implementation to give it.
 */
public class MainModule extends AbstractModule {

    private final Stage stage;

    public MainModule(Stage stage) {
        this.stage = stage;
    }

    @Override
    protected void configure() {
        bind(QuestionCollection.class).to(QuestionCollectionImpl.class);

        bind(GameModel.class).to(GameModelImpl.class);
        bind(GameModelSaver.class).to(GameModelImpl.class);

        bind(SceneHandler.class).to(SceneHandlerImpl.class);
        bind(SceneRegistry.class).to(SceneRegistryImpl.class);

        bind(QuestionGeneratorStrategyFactory.class).to(QuestionGeneratorStrategyFactoryImpl.class);
        bind(BoardLoaderStrategyFactory.class).to(BoardLoaderStrategyFactoryImpl.class);
        bind(BoardComponentFactory.class).to(BoardComponentFactoryImpl.class);

        bind(TextNormaliserFactory.class).to(TextNormaliserStrategyFactory.class);
        bind(QuestionVerifierFactory.class).to(QuestionVerifierFactoryImpl.class);
        bind(ObjectReaderStrategyFactory.class).to(ObjectReaderStrategyFactoryImpl.class);
        
        bind(UserScore.class).to(UserScoreImpl.class);

        if (System.getProperty("os.name").startsWith("Windows")) {
            bind(Speaker.class).to(WindowsSpeakerManager.class);
            bind(SpeakerMutator.class).to(WindowsSpeakerManager.class);
        } else {
            bind(Speaker.class).to(SpeakerManager.class);
            bind(SpeakerMutator.class).to(SpeakerManager.class);
        }


        bind(Stage.class).toInstance(stage);
    }
}
