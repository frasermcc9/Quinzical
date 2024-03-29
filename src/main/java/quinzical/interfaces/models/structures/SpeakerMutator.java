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

package quinzical.interfaces.models.structures;

/**
 * Speaker class handles speaking text from the TTS Engine. Extends {@link Speaker} with methods for changing the
 * settings of the TTS engine.
 *
 * @author Fraser McCallum
 * @see Speaker
 * @see quinzical.impl.models.structures.SpeakerManager
 * @since 1.0
 */
public interface SpeakerMutator extends Speaker {

    int getPitch();

    void setPitch(int pitch);

    int getAmplitude();

    void setAmplitude(int amplitude);

    int getSpeed();

    void setSpeed(int speed);

    int getGap();

    void setGap(int gap);
}
