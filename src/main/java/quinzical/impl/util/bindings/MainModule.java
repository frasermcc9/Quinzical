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

package quinzical.impl.util.bindings;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import quinzical.Entry;
import quinzical.impl.models.GameModelImpl;
import quinzical.impl.models.PracticeModelImpl;
import quinzical.impl.models.QuestionCollectionImpl;
import quinzical.impl.models.SceneHandlerImpl;
import quinzical.impl.models.structures.*;
import quinzical.impl.multiplayer.models.ActiveGameImpl;
import quinzical.impl.multiplayer.models.SocketModelImpl;
import quinzical.impl.multiplayer.models.structures.XpClassFactoryImpl;
import quinzical.impl.util.strategies.objectreader.ObjectReaderStrategyFactoryImpl;
import quinzical.impl.util.strategies.questiongenerator.QuestionGeneratorStrategyFactoryImpl;
import quinzical.impl.util.strategies.questionverifier.QuestionVerifierFactoryImpl;
import quinzical.impl.util.strategies.textnormaliser.TextNormaliserStrategyFactory;
import quinzical.impl.util.strategies.timer.TimerContextImpl;
import quinzical.interfaces.models.*;
import quinzical.interfaces.models.structures.*;
import quinzical.interfaces.multiplayer.ActiveGame;
import quinzical.interfaces.multiplayer.SocketModel;
import quinzical.interfaces.multiplayer.XpClassFactory;
import quinzical.interfaces.strategies.objectreader.ObjectReaderStrategyFactory;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategyFactory;
import quinzical.interfaces.strategies.questionverifier.QuestionVerifierFactory;
import quinzical.interfaces.strategies.textnormaliser.TextNormaliserFactory;
import quinzical.interfaces.strategies.timer.TimerContext;

/**
 * This class binds concrete implementations to interfaces. It decides, when a class requires an interface injection
 * with the @Inject annotation, which concrete implementation to give it.
 */
public class MainModule extends AbstractModule {

    private final static boolean TEST_MODE = false;

    @Override
    protected final void configure() {

        final Scene scene = new Scene(new AnchorPane());
        scene.getStylesheets().add(Entry.class.getResource("/css/global-font.css").toExternalForm());
        bind(Scene.class).toInstance(scene);

        bind(QuestionCollection.class).to(QuestionCollectionImpl.class);

        bind(GameModel.class).to(GameModelImpl.class);
        bind(GameModelSaver.class).to(GameModelImpl.class);
        bind(UserData.class).to(UserDataImpl.class);
        bind(AnalyticsEngineMutator.class).to(AnalyticsEngineImpl.class);
        bind(AnalyticsEngineReader.class).to(AnalyticsEngineImpl.class);

        bind(ReadonlyPersistentSettings.class).to(PersistentSettingsImpl.class);
        bind(PersistentSettings.class).to(PersistentSettingsImpl.class);
        
        bind(SceneHandler.class).to(SceneHandlerImpl.class);

        bind(QuestionGeneratorStrategyFactory.class).to(QuestionGeneratorStrategyFactoryImpl.class);

        bind(TextNormaliserFactory.class).to(TextNormaliserStrategyFactory.class);
        bind(QuestionVerifierFactory.class).to(QuestionVerifierFactoryImpl.class);
        bind(ObjectReaderStrategyFactory.class).to(ObjectReaderStrategyFactoryImpl.class);

        bind(PracticeModel.class).to(PracticeModelImpl.class);

        if (System.getProperty("os.name").startsWith("Windows")) {
            bind(Speaker.class).to(WindowsSpeakerManager.class);
            bind(SpeakerMutator.class).to(WindowsSpeakerManager.class);
        } else {
            bind(Speaker.class).to(SpeakerManager.class);
            bind(SpeakerMutator.class).to(SpeakerManager.class);
        }

        bind(Integer.class).annotatedWith(Names.named("attempts")).toInstance(0);

        bind(TimerContext.class).to(TimerContextImpl.class);

        if (TEST_MODE)
            bind(String.class).annotatedWith(Names.named("socketUrl")).toInstance("http://localhost:7373");
        else
            bind(String.class).annotatedWith(Names.named("socketUrl")).toInstance("http://20.190.113.34:7373");

        bind(ActiveGame.class).to(ActiveGameImpl.class);

        bind(XpClassFactory.class).to(XpClassFactoryImpl.class);
        bind(SocketModel.class).to(SocketModelImpl.class);
    }
}
