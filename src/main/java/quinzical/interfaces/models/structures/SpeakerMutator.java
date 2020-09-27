package quinzical.interfaces.models.structures;

public interface SpeakerMutator {
    
    void speak(String text);

    void setPitch(int pitch);

    void setAmplitude(int amplitude);

    void setSpeed(int speed);

    void setGap(int gap);
}
