package jay.antgame.data;

/**
 * Created by Julian on 24.06.2016.
 */
public class Ant implements WorldObject {

    //TODO implement Methods
    private int antFood;
    private int maxFood;
    private Position pos;

    public Ant(int max) {
        maxFood = max;
    }

    @Override
    public Position getPosition() {
        return pos;
    }

    @Override
    public void setPosition(Position pos) {
        this.pos = pos;
    }

    public int getFood() {
        return antFood;
    }

    public void setFood(int f) {
        antFood = f;
    }


}