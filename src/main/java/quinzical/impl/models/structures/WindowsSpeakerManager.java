package quinzical.impl.models.structures;

import com.google.inject.Singleton;
import quinzical.interfaces.models.structures.Speaker;

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
