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

package quinzical.impl.controllers.menus;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import de.saxsys.javafx.test.JfxRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import quinzical.impl.constants.GameScene;
import quinzical.interfaces.models.GameModelSaver;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.strategies.objectreader.ObjectReaderStrategyFactory;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@RunWith(JfxRunner.class)
public class IntroControllerTest {

    @Mock
    private SceneHandler sceneHandler;
    @Mock
    private GameModelSaver gameModel;
    @Mock
    private ObjectReaderStrategyFactory objectReader;

    @InjectMocks
    private IntroController introController;

    private Injector injector;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(SceneHandler.class).toInstance(sceneHandler);
                bind(GameModelSaver.class).toInstance(gameModel);
                bind(ObjectReaderStrategyFactory.class).toInstance(objectReader);
                bind(IntroController.class);
            }
        });
    }

    @Test
    public void TestBtnLoadGamePress() {
        introController.btnLoadGamePress();
        verify(sceneHandler, timeout(100)).setActiveScene(GameScene.GAME);
    }

    @Test
    public void TestBtnNewGamePress() {
        injector.getInstance(IntroController.class).btnNewGamePress();
        verify(sceneHandler, timeout(100)).setActiveScene(GameScene.GAME_TYPE_SELECT);
    }

    @Test
    public void TestBtnOptionsPress() {
        injector.getInstance(IntroController.class).btnOptionsPress();
        verify(sceneHandler, timeout(100)).setActiveScene(GameScene.OPTIONS);
    }

    @Test
    public void TestBtnAchievementsClick() {
        injector.getInstance(IntroController.class).btnAchievementsClick();
        verify(sceneHandler, timeout(100)).setActiveScene(GameScene.STORE);
    }

    @Test
    public void TestBtnStatisticsClick() {
        injector.getInstance(IntroController.class).btnStatisticsClick();
        verify(sceneHandler, timeout(100)).setActiveScene(GameScene.STATISTICS);
    }

    @Test
    public void TestBtnOnlineClick() {
        injector.getInstance(IntroController.class).btnOnlineClick();
        verify(sceneHandler, timeout(100)).setActiveScene(GameScene.MULTI_INTRO);
    }
}
