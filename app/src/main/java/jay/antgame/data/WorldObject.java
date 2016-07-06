package jay.antgame.data;

import android.graphics.Paint;

import jay.antgame.data.Position;

/**
 * Created by Julian on 24.06.2016.
 */

public interface WorldObject {
    Position getPosition();
    void setPosition(Position pos);
    void click(Position p);
    String getSelectedText();
    //boolean insideBounds(int touchAccuracyIfPoint);
    //TODO getImage
}
