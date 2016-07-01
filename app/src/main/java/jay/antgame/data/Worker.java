package jay.antgame.data;

/**
 * Created by Yannick.Pfeiffer on 29.06.2016.
 */

public class Worker extends Ant {

    private int antFood;

    private double movementSpeed = 3;

    private final double nearFoodVariable = 20;
    private final double randomRotation = 0.5;
    //Bereich an Ende der Map, ab welchem die Ameisen wieder umdrehen
    private final int worldEndPuffer = 20;
    private final int abdrehWinkel = 2;

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
                if(x<0)
                    targetAngle += Math.PI;
                System.out.println( "Target Angle = "+x+"/"+y+ "  tanh "+targetAngle );
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
                if(pos.getX()<-world.getWidth()/2+worldEndPuffer||pos.getX()>world.getWidth()/2-worldEndPuffer
                        ||pos.getY()<-world.getHeight()/2+worldEndPuffer||pos.getY()>world.getHeight()/2-worldEndPuffer)
                    angle += abdrehWinkel;
                pos.addX(movementSpeed * Math.cos(angle));
                pos.addY(movementSpeed * Math.sin(angle));
                //System.out.println(pos.getX() + " , "+pos.getY()+ "     "+world.getWidth()+" "+world.getHeight());
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
