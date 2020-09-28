package quinzical.impl.models.structures;

import com.google.inject.Inject;
import quinzical.interfaces.models.structures.UserScore;
import quinzical.interfaces.models.GameModel;

public class UserScoreImpl implements UserScore {

    @Inject
    private GameModel gameModel;

    private int value = 0;

    @Override public int getValue() {
        return value;
    }

    @Override public void setValue(int value) {
        this.value = value;
        gameModel.fireValueChange();
    }
}
