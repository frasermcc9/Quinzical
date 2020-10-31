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
import javafx.application.Platform;
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
     * @param text     the text to speak
     * @param callback function to execute once callback is complete
     */
    @Override
    public void speak(final String text, final Runnable callback) {
        execute(callback, "espeak",
            "-p", Integer.toString(pitch),
            "-a", Integer.toString(amplitude),
            "-s", Integer.toString(speed),
            "-g", Integer.toString(gap),
            text);
    }

    /**
     * Speaks the given text.
     *
     * @param text the text to speak
     */
    @Override
    public void speak(final String text) {
        speak(text, () -> {
        });
    }

    /**
     * Creates the speaker thread and keeps it in the speaker field. When execute is called, the currently active
     * speaker is stopped and replaced by the new one.
     *
     * @param callback function to run once the process is complete
     * @param command  bash command to execute
     */
    private void execute(final Runnable callback, final String... command) {
        if (speaker != null && speaker.isAlive()) {
            speaker.interrupt();
        }
        speaker = new Thread(() -> {
            final ProcessBuilder pb = new ProcessBuilder(command);
            Process process = null;
            try {
                process = pb.start();
                process.waitFor();
                process.destroy();
                Platform.runLater(callback);
            } catch (final IOException ignored) {

            } catch (final InterruptedException e) {
                process.destroy();
            }
        });
        //This thread will not prevent the application from closing.
        speaker.setDaemon(true);
        speaker.start();
    }

    public int getPitch() {
        return pitch;
    }

    /**
     * Sets the pitch of the speaker
     *
     * @param pitch - The pitch that the speaker will talk in (how high or low the voice sounds)
     */
    @Override
    public void setPitch(final int pitch) {
        if (pitch < 0 || pitch > 99) {
            throw new IllegalArgumentException("The pitch must be be between 0 and 99");
        }
        this.pitch = pitch;
    }

    public int getAmplitude() {
        return amplitude;
    }

    /**
     * Sets the amplitude of the speaker
     *
     * @param amplitude - The amplitude to be set (how loud the voice is)
     */
    @Override
    public void setAmplitude(final int amplitude) {
        if (amplitude < 0 || amplitude > 200) {
            throw new IllegalArgumentException("The amplitude must be be between 0 and 200");
        }
        this.amplitude = amplitude;
    }

    public int getSpeed() {
        return speed;
    }

    /**
     * Sets the reading speed of the speaker
     *
     * @param speed - Reading speed (recommended range between 80 ~ 500)
     */
    @Override
    public void setSpeed(final int speed) {
        if (speed < 1) {
            throw new IllegalArgumentException("The speed must be above 0");
        }
        this.speed = speed;
    }

    public int getGap() {
        return gap;
    }

    /**
     * Sets the gap between words for the speaker
     *
     * @param gap - How long to wait between each word spoken
     */
    @Override
    public void setGap(final int gap) {
        if (gap < 0) {
            throw new IllegalArgumentException("The gap between words must be positive number");
        }
        this.gap = gap;
    }
}
