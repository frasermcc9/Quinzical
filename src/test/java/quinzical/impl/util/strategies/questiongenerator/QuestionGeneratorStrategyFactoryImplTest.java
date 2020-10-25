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

package quinzical.impl.util.strategies.questiongenerator;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.BeforeClass;
import org.junit.Test;
import quinzical.impl.models.QuestionCollectionImpl;
import quinzical.impl.models.structures.GameQuestion;
import quinzical.impl.util.strategies.objectreader.ObjectReaderStrategyFactoryImpl;
import quinzical.interfaces.models.QuestionCollection;
import quinzical.interfaces.strategies.objectreader.ObjectReaderStrategyFactory;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategy;
import quinzical.interfaces.strategies.questiongenerator.QuestionGeneratorStrategyFactory;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class QuestionGeneratorStrategyFactoryImplTest {

    static Injector injector;

    @BeforeClass
    public static void before() {
        injector = Guice.createInjector(binder -> {
            binder.bind(QuestionGeneratorStrategyFactory.class).to(QuestionGeneratorStrategyFactoryImpl.class);
            binder.bind(QuestionCollection.class).to(QuestionCollectionImpl.class);
            binder.bind(ObjectReaderStrategyFactory.class).to(ObjectReaderStrategyFactoryImpl.class);
        });
    }

    @Test
    public void TestInternationalQuestionApi() {
        QuestionGeneratorStrategyFactory factory = injector.getInstance(QuestionGeneratorStrategyFactory.class);
        QuestionGeneratorStrategy strategy = factory.createInternationalQuestionStrategy();
        Map<String, List<GameQuestion>> board = strategy.generateQuestions();

        assertEquals(5, board.size());
        board.forEach((category, questions) -> {
            assertEquals(5, questions.size());
        });
    }

}
