package jay.antgame.data;

/**
 * Created by Julian on 24.06.2016.
 */
public class FoodSource implements WorldObject {

    private int sourceFood;
    private Position pos;

    public FoodSource (Position pos, int startFood) {
        this.pos = pos;
        sourceFood = startFood;
    }

    //TODO implement methods

    @Override
    public Position getPosition() {
        return pos;
    }

    @Override @Deprecated
    public void setPosition(Position pos) {
        //this.pos = pos;
        throw new RuntimeException("currently changing the Nests Position isn't allowed");
    }

    public void lowerFood(int f) {sourceFood=sourceFood - f;}

    public int getFood() {return sourceFood;}
}
