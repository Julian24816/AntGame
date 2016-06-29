package jay.antgame.data;

/**
 * Created by Julian on 24.06.2016.
 */
public class FoodSource implements WorldObject {

    private int sourceFood;
    private Position pos;

    public FoodSource (int f) {
        sourceFood = f;
    }

    //TODO implement methods

    @Override
    public Position getPosition() {
        return pos;
    }

    @Override
    public void setPosition(Position pos) {
        this.pos = pos;
    }

    public void lowerFood(int f) {sourceFood=sourceFood - f;}

    public int getFood() {return sourceFood;}
}
