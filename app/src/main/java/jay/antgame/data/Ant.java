package jay.antgame.data;

/**
 * Created by Julian on 24.06.2016.
 */
public abstract class Ant implements WorldObject {

    private double movementSpeed = 2;

    protected Position pos;
    protected double angle;

    protected Position targetPosition;

    public Ant(Position pos) {
        this.pos = pos;
        this.angle = Math.random()*360;
    }

    @Override
    public Position getPosition() {
        return pos;
    }

    @Override
    public void setPosition(Position pos) {
        this.pos = pos;
    }

    public Position getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Position targetPosition) {
        this.targetPosition = targetPosition;
    }

    public abstract void tick(World world);

    public boolean hasTarget() {
        return targetPosition!=null;
    }
}