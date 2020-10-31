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

public class TimerContextImpl implements TimerContext {
    private final Provider<DefaultTimerStrategy> defaultTimerStrategyProvider;

    @Inject
    public TimerContextImpl(final Provider<DefaultTimerStrategy> defaultTimerStrategyProvider) {
        this.defaultTimerStrategyProvider = defaultTimerStrategyProvider;
    }

    @Override
    public final TimerStrategy createTimer(final TimerType timerType) {
        switch (timerType) {
            case DEFAULT:
                return defaultTimerStrategyProvider.get();
            default:
                throw new IllegalArgumentException("Invalid TimerType provided to TimerContext");
        }
    }
}

class DefaultTimerStrategy implements TimerStrategy {
    
    private Thread task;

    @Override
    public final void setTimeout(final Runnable runnable, final int delay) {
        task = new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (final InterruptedException ignored) {
            }
        });

        task.start();
    }

    @Override
    public final void stopTimeout() {
        task.interrupt();
    }
}

