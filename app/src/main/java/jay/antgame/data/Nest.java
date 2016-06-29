package jay.antgame.data;

/**
 * Created by Julian on 24.06.2016.
 */
public class Nest implements WorldObject {

    private Position pos;
    private int food;

    public Nest(Position startPos) {
        pos = startPos;
        food = 0;
    }

    @Override
    public Position getPosition() {
        return pos;
    }

    @Override @Deprecated
    public void setPosition(Position pos) {
        //this.pos = pos;
        throw new RuntimeException("currently changing the Nests Position isn't allowed");
    }

    public void addFoodAmount(int f){
        food += f;
    }

    public int getFood() {
        return food;
    }

}
