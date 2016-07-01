package jay.antgame.data;

/**
 * Created by Yannick.Pfeiffer on 29.06.2016.
 */

public class Worker extends Ant {

    private int antFood;

    private double movementSpeed = 3;

    private final double nearTargetVariable = 5;
    private final double randomRotation = 1;

    public Worker(Position position) {
        super(position);
    }

    public void tick(World world){

        if(targetPosition!=null){
            double x = targetPosition.getX()-pos.getX();
            double y = targetPosition.getY()-pos.getY();
            double targetAngle = Math.tanh( y/x );
            pos.addX( movementSpeed*Math.cos(targetAngle) );
            pos.addY( movementSpeed*Math.sin(targetAngle) );
        }else{
            if(!checkIfOnFQ(world)) {
                angle += (0.5 - Math.random()) * randomRotation;
                pos.addX(movementSpeed * Math.cos(angle));
                pos.addY(movementSpeed * Math.sin(angle));
            }
        }

    }

    private boolean checkIfOnFQ(World world){
        boolean onFQ = false;
        for(FoodSource foodSource: world.getFoodSources()){
            if(checkIfOn(foodSource.getPosition()))
                onFQ = true;
        }
        return onFQ;
    }

    private boolean checkIfOn(Position targetPosition){

        if(targetPosition.getX()-pos.getX()<nearTargetVariable&&targetPosition.getY()-pos.getY()<nearTargetVariable)
            return true;
        else
            return false;

    }

    public int getFood() {
        return antFood;
    }

    public void setFood(int f) {
        antFood = f;
    }


}
