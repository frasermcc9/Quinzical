package quinzical.impl.models.structures;

import com.google.inject.Inject;
import quinzical.interfaces.models.GameModel;
import quinzical.interfaces.models.structures.UserScore;

public class UserScoreImpl implements UserScore {

    /**
     * GameModel singleton to alert value changes to
     */
    @Inject
    private GameModel gameModel;

    /**
     * The users earnings
     */
    private int value = 0;

    /**
     * @return gets the user's current earnings
     */
    @Override
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of the users earnings. Fires the value change event to the GameModel.
     *
     * @param value the value to set the earnings to.
     */
    @Override
    public void setValue(int value) {
        this.value = value;
        gameModel.fireValueChange();
    }
}
