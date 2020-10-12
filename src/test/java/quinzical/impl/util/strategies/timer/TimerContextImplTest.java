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

package quinzical.impl.util.strategies.timer;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import quinzical.interfaces.strategies.timer.TimerContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class TimerContextImplTest {
    @Test
    public void TestFunctionIsExecutedEventually() throws ExecutionException, InterruptedException {

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(TimerContext.class).to(TimerContextImpl.class);
            }
        });

        TimerContext timerContext = injector.getInstance(TimerContext.class);
        CompletableFuture<String> future = new CompletableFuture<>();
        timerContext.createTimer(TimerType.DEFAULT).setTimeout(() -> future.complete("Success"), 100);

        Assertions.assertEquals("Success", future.get());
    }
}
