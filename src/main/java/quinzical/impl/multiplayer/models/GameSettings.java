package quinzical.impl.multiplayer.models;

public class GameSettings {
    public int questions;
    public int timePerQuestion;
    public int maxPlayers;
    public boolean isGamePublic;

    public GameSettings(final int questions, final int timePerQuestion, final int maxPlayers, final boolean isGamePublic) {
        this.questions = questions;
        this.timePerQuestion = timePerQuestion;
        this.maxPlayers = maxPlayers;
        this.isGamePublic = isGamePublic;
    }

}
