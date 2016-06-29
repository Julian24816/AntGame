package jay.antgame.data;

/**
 * Created by Julian on 24.06.2016.
 */
public class ScentTrail implements WorldObject {

    private int lifetime;
    private Position pos;
    private Position target;
    //TODO implement methods


    @Override
    public Position getPosition() {
        return pos;
    }

    @Override
    public void setPosition(Position pos) {
        this.pos = pos;
    }

    public Position getTarget(){ return target; }
    public int getRemainingLifetime(){return lifetime; }
}
