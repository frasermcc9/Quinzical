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

package quinzical.impl.models.structures;

import com.google.inject.Inject;
import quinzical.impl.constants.Theme;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.SceneHandler;
import quinzical.interfaces.models.structures.PersistentSettings;
import quinzical.interfaces.models.structures.SpeakerMutator;
import quinzical.interfaces.strategies.objectreader.ObjectReaderStrategy;
import quinzical.interfaces.strategies.objectreader.ObjectReaderStrategyFactory;

import java.io.Serializable;

public class PersistentSettingsImpl implements Serializable, PersistentSettings {

    @Inject
    private transient ObjectReaderStrategyFactory objectReaderStrategyFactory;
    @Inject
    private transient SpeakerMutator speakerMutator;
    @Inject
    private transient SceneHandler sceneHandler;
    @Inject
    private transient GameModel gameModel;

    private Theme theme = Theme.FIELDS;

    private int gap = 0;
    private int speed = 175;
    private int amp = 100;
    private int pitch = 50;

    private double timer = 25;

    public final PersistentSettings loadSettingsFromDisk() {
        final ObjectReaderStrategy<PersistentSettings> strategy = objectReaderStrategyFactory.createObjectReader();

        try {
            final PersistentSettings settings = strategy.readObject(System.getProperty("user.dir") + "/data/preferences.qdb");

            theme = settings.getTheme();
            gap = settings.getGap();
            speed = settings.getSpeed();
            amp = settings.getAmp();
            pitch = settings.getPitch();
            timer = settings.getTimer();
        } catch (final Exception e) {
            //dont really care if this happens. It will just use default settings.
        }

        return this;
    }

    public final void applySettings() {
        this.gameModel.setTimerValue(timer);

        this.speakerMutator.setGap(gap);
        this.speakerMutator.setAmplitude(amp);
        this.speakerMutator.setPitch(pitch);
        this.speakerMutator.setSpeed(speed);

        this.sceneHandler.fireBackgroundChange(theme);
    }

    public final PersistentSettings loadSettingsFromGame() {
        timer = gameModel.getTimerValue();

        gap = speakerMutator.getGap();
        amp = speakerMutator.getAmplitude();
        pitch = speakerMutator.getPitch();
        speed = speakerMutator.getSpeed();

        theme = sceneHandler.getActiveTheme();

        return this;
    }

    @Override
    public final Theme getTheme() {
        return theme;
    }

    @Override
    public final void setTheme(final Theme theme) {
        this.theme = theme;
    }

    @Override
    public final int getGap() {
        return gap;
    }

    @Override
    public final void setGap(final int gap) {
        this.gap = gap;
    }

    @Override
    public final int getSpeed() {
        return speed;
    }

    @Override
    public final void setSpeed(final int speed) {
        this.speed = speed;
    }

    @Override
    public final int getAmp() {
        return amp;
    }

    @Override
    public final void setAmp(final int amp) {
        this.amp = amp;
    }

    @Override
    public final int getPitch() {
        return pitch;
    }

    @Override
    public final void setPitch(final int pitch) {
        this.pitch = pitch;
    }

    @Override
    public final double getTimer() {
        return timer;
    }

    @Override
    public final void setTimer(final double timer) {
        this.timer = timer;
    }
}
