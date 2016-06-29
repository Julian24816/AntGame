package jay.antgame.data;

/**
 * Created by Julian on 24.06.2016.
 */
public class ScentTrail implements WorldObject {

    private int lifetime;
    private Position pos;
    private Position target;

    public ScentTrail(Position pos, Position target, int lifeTime) {
        this.lifetime = lifeTime;
        this.pos = pos;
        this.target = target;
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

    public Position getTarget(){
        return target;
    }

    public int getRemainingLifetime(){
        return lifetime;
    }
}
