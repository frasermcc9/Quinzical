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

import com.google.inject.Inject;
import com.google.inject.Provider;
import quinzical.interfaces.strategies.timer.TimerContext;
import quinzical.interfaces.strategies.timer.TimerStrategy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class TimerContextImpl implements TimerContext {
    private final Provider<DefaultTimerStrategy> defaultTimerStrategyProvider;

    @Inject
    public TimerContextImpl(final Provider<DefaultTimerStrategy> defaultTimerStrategyProvider) {
        this.defaultTimerStrategyProvider = defaultTimerStrategyProvider;
    }

    @Override
    public TimerStrategy createTimer(TimerType timerType) {
        switch (timerType) {
            case DEFAULT:
                return defaultTimerStrategyProvider.get();
            default:
                throw new IllegalArgumentException("Invalid TimerType provided to TimerContext");
        }
    }
}

class DefaultTimerStrategy implements TimerStrategy {
    @Override
    public void setTimeout(Runnable runnable, int delay) {
        CompletableFuture.delayedExecutor(delay, TimeUnit.MILLISECONDS).execute(runnable);
    }
}
