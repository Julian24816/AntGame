package jay.antgame.data;

/**
 * Created by Julian on 24.06.2016.
 */
public abstract class Ant implements WorldObject {

    private double movementSpeed = 2;

    protected Position pos;
    protected Position lastPos;

    protected Position targetPosition;

    public Ant(Position pos) {
        this.pos = this.lastPos = pos;
    }

    @Override
    public Position getPosition() {
        return pos;
    }

    @Override
    public void setPosition(Position pos) {
        lastPos = this.pos;
        this.pos = pos;
    }

    public Position getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Position targetPosition) {
        this.targetPosition = targetPosition;
    }

    public abstract void tick();

}