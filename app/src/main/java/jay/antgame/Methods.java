package jay.antgame;

import jay.antgame.data.Position;

/**
 * Created by Yannick.Pfeiffer on 06.07.2016.
 *
 * @author Yannick
 */

public class Methods {

    /**
     *
     * @param x1 x position of firts object
     * @param y1 y position of firts object
     * @param x2 x position of second object
     * @param y2 y position of second object
     * @param nearTargetVariable how near the first object has to be to the second object, so true will be returned
     * @return true if object 1 and 2 are nearTagetVariable near each other
     */
    public static boolean samePosition(float x1, float y1, float x2, float y2, double nearTargetVariable){

        boolean nearX = -nearTargetVariable<x2-x1&&x2-x1<nearTargetVariable;
        boolean nearY = -nearTargetVariable<y2-y1&&y2-y1<nearTargetVariable;
        if(nearX&&nearY)
            return true;
        else
            return false;
    }

    /**
     *
     * @param position1 firtst position
     * @param position2 second position
     * @param nearTargetVariable how near the first object has to be to the second object, so true will be returned
     * @return true if object 1 and 2 are nearTagetVariable near each other
     */
    public static boolean samePosition(Position position1, Position position2, double nearTargetVariable){

        return samePosition(position1.getX(),position1.getY(),position2.getX(),position2.getY(), nearTargetVariable);

    }

}
