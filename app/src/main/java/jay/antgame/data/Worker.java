package jay.antgame.data;

/**
 * Created by Yannick.Pfeiffer on 29.06.2016.
 */

public class Worker extends Ant {

    private int antFood;

    private double movementSpeed = 3;

    private final double nearFoodVariable = 20;
    private final double randomRotation = 1;

    public Worker(Position position) {
        super(position);
    }

    public void tick(World world){

        //Wenn hat ein Ziel zulaufen auf Ziel
        if(targetPosition!=null){

            //Wenn Ziel noch nicht erreicht weiterlaufen
            if(!checkIfOn(targetPosition, 10)) {
                double x = targetPosition.getX() - pos.getX();
                double y = targetPosition.getY() - pos.getY();
                double targetAngle = Math.tanh(y / x);
                pos.addX(movementSpeed * Math.cos(targetAngle));
                pos.addY(movementSpeed * Math.sin(targetAngle));
            }else{
                //Wenn Ziel erreicht kein Ziel mehr
                targetPosition = null;
            }

        }else{
            //Wenn kein Ziel Winkel zufällig ändern und weiterlaufen
            if(!checkIfOnFQ(world)) {
                angle += (0.5 - Math.random()) * randomRotation;
                pos.addX(movementSpeed * Math.cos(angle));
                pos.addY(movementSpeed * Math.sin(angle));
            }else{
                targetPosition = world.getNest().getPosition();
            }
        }

    }

    private boolean checkIfOnFQ(World world){
        boolean onFQ = false;
        for(FoodSource foodSource: world.getFoodSources()){
            if(checkIfOn(foodSource.getPosition(),nearFoodVariable))
                onFQ = true;
        }
        return onFQ;
    }

    private boolean checkIfOn(Position targetPosition, double nearTargetVariable){

        boolean nearX = -nearTargetVariable<targetPosition.getX()-pos.getX()&&targetPosition.getX()-pos.getX()<nearTargetVariable;
        boolean nearY = -nearTargetVariable<targetPosition.getY()-pos.getY()&&targetPosition.getY()-pos.getY()<nearTargetVariable;
        if(nearX&&nearY)
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
