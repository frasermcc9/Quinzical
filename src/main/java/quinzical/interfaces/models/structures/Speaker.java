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
 * Speaker class handles speaking text from the TTS Engine.
 *
 * @author Fraser McCallum
 * @see SpeakerMutator
 * @see quinzical.impl.models.structures.SpeakerManager
 * @since 1.0
 */
public interface Speaker {

    /**
     * Speaks given text through the speaker manager. Only one speaker can be active at a time. New speak requests will
     * terminate any existing ones.
     *
     * @param text the text to speak.
     */
    void speak(String text);

    /**
     * Speaks given text through the speaker manager and executes a function upon completion. Only one speaker can be
     * active at a time. New speak requests will terminate any existing ones.
     * <p>
     * The callback function will not execute if a new speech request is made.
     *
     * @param text     the text to speak
     * @param callback function to execute once the speaker is completed.
     */
    void speak(String text, Runnable callback);
}
