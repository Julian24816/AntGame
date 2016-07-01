package jay.antgame.data;

/**
 * Created by Yannick.Pfeiffer on 29.06.2016.
 */

public class Worker extends Ant {

    private int antFood;

    private double movementSpeed = 3;

    private final double randomRotation = 0.3;

    public Worker(Position position) {
        super(position);
    }

    public void tick(){

        if(targetPosition!=null){
            double x = targetPosition.getX()-pos.getX();
            double y = targetPosition.getY()-pos.getY();
            double tan = y/x;

        }else{
            angle += Math.random()* randomRotation;
            pos.addX( movementSpeed*Math.sin(angle) );
            pos.addY( movementSpeed*Math.cos(angle) );
        }

    }


    public int getFood() {
        return antFood;
    }

    public void setFood(int f) {
        antFood = f;
    }


}
