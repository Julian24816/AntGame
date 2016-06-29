package jay.antgame.data;

/**
 * Created by Julian on 24.06.2016.
 */
public class Nest implements WorldObject {

    private Position pos;
    private int food;
    //TODO implement methods

    @Override
    public Position getPosition() {
        return pos;
    }

    @Override
    public void setPosition(Position pos) {
        this.pos = pos;
    }

    public void addFoodAmount(int f){
        food = f;
    }

}
