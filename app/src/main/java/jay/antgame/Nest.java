package jay.antgame;

/**
 * Created by Julian on 24.06.2016.
 */
public class Nest implements WorldObject {

    //TODO implement methods

    @Override
    public Position getPosition() {
        return null;
    }

    public void addFoodAmount(int f){
        World.addFood(f);
    }

}
