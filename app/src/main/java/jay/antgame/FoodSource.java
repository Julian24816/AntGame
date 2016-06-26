package jay.antgame;

/**
 * Created by Julian on 24.06.2016.
 */
public class FoodSource implements WorldObject {

    private int sourceFood;

    public FoodSource (int f) {
        sourceFood = f;
    }

    //TODO implement methods

    @Override
    public Position getPosition() {
        return null;
    }

    public void lowerFood(int f) {sourceFood=sourceFood - f;}

    public int getFood() {return sourceFood;}
}
