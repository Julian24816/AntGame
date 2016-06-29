package jay.antgame.data;

/**
 * Created by Yannick.Pfeiffer on 29.06.2016.
 */

public class Worker extends Ant {

    private int antFood;

    private double movementSpeed = 3;

    private Position target = null;

    public Worker(Position position) {
        super(position);
    }

    public void tick(){

        if(target!=null){
            double x = target.getX()-pos.getX();
            double y = target.getY()-pos.getY();
            double tan = y/x;

        }

    }

    public void setTarget(Position position){
        target = position;
    }

    public int getFood() {
        return antFood;
    }

    public void setFood(int f) {
        antFood = f;
    }


}
