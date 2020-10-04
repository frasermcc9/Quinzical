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

/**
 * An extension of SpeakerManager that is used when the OS is windows, meaning
 * that festival cannot be used
 */
@Singleton
public class WindowsSpeakerManager extends SpeakerManager {

    /**
     * "Speaks" the inputted text (prints it out)
     * 
     * @param text the text to speak
     */
    @Override
    public void speak(String text) {
        System.out.println("Operating System is windows. Printing TTS: " + text);
    }

    /**
     * Prints out the inputted pitch value, ensuring that this function is called when it should be
     * 
     * @param pitch - The pitch that the speaker will talk in (how high or low the voice sounds)
     */
    @Override
    public void setPitch(int pitch) {
        super.setPitch(pitch);
        System.out.println("Operating System is windows. Printing new pitch: " + pitch);
    }

    /**
     * Prints out the inputted amplitude, ensuring that this function is called when it should be
     * 
     * @param amplitude - The amplitude to be set (how loud the voice is)
     */
    @Override
    public void setAmplitude(int amplitude) {
        super.setAmplitude(amplitude);
        System.out.println("Operating System is windows. Printing new amplitude: " + amplitude);
    }

    /**
     * Prints out the inputted reading speed, ensuring that this function is called when it should be
     * 
     * @param speed - Reading speed (recommended range between 80 ~ 500)
     */
    @Override
    public void setSpeed(int speed) {
        super.setSpeed(speed);
        System.out.println("Operating System is windows. Printing new speed: " + speed);
    }

    /**
     * Prints out the inputted gap, ensuring that this function is called when it should be
     * 
     * @param gap - How long to wait between each word spoken
     */
    @Override
    public void setGap(int gap) {
        super.setGap(gap);
        System.out.println("Operating System is windows. Printing new gap: " + gap);
    }
}
