package quinzical.impl.models.structures;

import quinzical.interfaces.models.structures.Speaker;

public class WindowsSpeakerManager implements Speaker {
    @Override
    public void speak(String text) {
        System.out.println("Operating System is windows. Printing TTS: " + text);
    }
}
