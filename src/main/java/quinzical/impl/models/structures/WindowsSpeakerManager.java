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

@Singleton
public class WindowsSpeakerManager extends SpeakerManager {
    @Override
    public void speak(String text) {
        System.out.println("Operating System is windows. Printing TTS: " + text);
    }

    @Override
    public void setPitch(int pitch) {
        super.setPitch(pitch);
        System.out.println("Operating System is windows. Printing new pitch: " + pitch);
    }

    @Override
    public void setAmplitude(int amplitude) {
        super.setAmplitude(amplitude);
        System.out.println("Operating System is windows. Printing new amplitude: " + amplitude);
    }

    @Override
    public void setSpeed(int speed) {
        super.setSpeed(speed);
        System.out.println("Operating System is windows. Printing new speed: " + speed);
    }

    @Override
    public void setGap(int gap) {
        super.setGap(gap);
        System.out.println("Operating System is windows. Printing new gap: " + gap);
    }
}
