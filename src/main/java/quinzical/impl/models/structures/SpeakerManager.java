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

import com.google.inject.Singleton;
import quinzical.interfaces.models.structures.Speaker;
import quinzical.interfaces.models.structures.SpeakerMutator;

import java.io.IOException;

/**
 * A manager for the speaking functionality of the questions
 */
@Singleton
public class SpeakerManager implements SpeakerMutator, Speaker {

    /**
     * represents the currently active speaker
     */
    private Thread speaker;

    private int pitch = 50;
    private int amplitude = 100;
    private int speed = 175;
    private int gap = 0;

    public SpeakerManager() {
    }

    /**
     * Speaks the given text.
     *
     * @param text
     */
    @Override
    public void speak(final String text) {
        execute("espeak",
            "-p", Integer.toString(pitch),
            "-a", Integer.toString(amplitude),
            "-s", Integer.toString(speed),
            "-g", Integer.toString(gap),
            text);
    }

    /**
     * Creates the speaker thread and keeps it in the speaker field. When execute is called, the currently active
     * speaker is stopped and replaced by the new one.
     */
    private void execute(final String... command) {
        if (speaker != null && speaker.isAlive()) {
            speaker.interrupt();
        }
        speaker = new Thread(() -> {

            ProcessBuilder pb = new ProcessBuilder(command);
            Process process = null;
            try {
                process = pb.start();
                process.waitFor();
                process.destroy();
            } catch (IOException ignored) {

            } catch (InterruptedException e) {
                process.destroy();
            }
        });
        speaker.start();
    }


    @Override
    public void setPitch(int pitch) {
        if (pitch < 0 || pitch > 99) {
            throw new IllegalArgumentException("The pitch must be be between 0 and 99");
        }
        this.pitch = pitch;
    }

    @Override
    public void setAmplitude(int amplitude) {
        if (amplitude < 0 || amplitude > 200) {
            throw new IllegalArgumentException("The amplitude must be be between 0 and 200");
        }
        this.amplitude = amplitude;
    }

    /**
     * @param speed - Reading speed (recommended range between 80 ~ 500)
     */
    @Override
    public void setSpeed(int speed) {
        if (speed < 1) {
            throw new IllegalArgumentException("The speed must be above 0");
        }
        this.speed = speed;
    }

    @Override
    public void setGap(int gap) {
        if (gap < 0) {
            throw new IllegalArgumentException("The gap between words must be positive number");
        }
        this.gap = gap;
    }
}
