package jay.antgame.data;

import jay.antgame.GameEngine;
import jay.antgame.Methods;

/**
 * Created by Yannick.Pfeiffer on 29.06.2016.
 */

public class Worker extends Ant {

    private static float movementSpeed = 3;
    private static int extCostIncrecment = 2;

    private static int workerLevel = 1;
    private static int workerFoodCapacity = 1;

    private static double workerNearFoodVariable = 20;

    private static float nearScent = 10;

    private int antFood = 0;

    private FoodSource foundFoodSource;

    private final double randomRotation = 0.5;

    //Bereich an Ende der Map, ab welchem die Ameisen wieder umdrehen
    private final int worldEndPuffer = 30;
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
                //System.out.println( "Target Angle = "+x+"/"+y+ "  tanh "+targetAngle );
                pos.addX(movementSpeed* Math.cos(targetAngle));
                pos.addY(movementSpeed * Math.sin(targetAngle));
            }else{
                //Wenn Ziel erreicht kein Ziel mehr
                if(targetPosition==world.getNest().getPosition()){
                    world.addFood(antFood);
                    antFood = 0;
                }
                if(foundFoodSource!=null)
                    foundFoodSource.getScentTrail().resetEndPoint();
                foundFoodSource = null;
                angle = Math.tanh( (targetPosition.getY()-pos.getY())/(targetPosition.getX()-pos
                        .getX()) );
                if (targetPosition.getX()-pos.getX() < 0) angle += + Math.PI;
                targetPosition = null;
            }

        }else{
            //Sucht FutterQuelle in der Nähe, wenn gefunden ziel wird Nest gesetzt
            if(checkIfOnFS(world)) {
                targetPosition = world.getNest().getPosition();
            }else if(searchForScentTrail(world)){
                //Sucht nach Scent Trail in der Nähe

            }else{
                //Wenn kein Ziel Winkel zufällig ändern und weiterlaufen
            angle += (0.5 - Math.random()) * randomRotation;
            if(pos.getX()<-world.getWidth()/2+worldEndPuffer||pos.getX()>world.getWidth()/2-worldEndPuffer
                    ||pos.getY()<-world.getHeight()/2+worldEndPuffer||pos.getY()>world.getHeight()/2-worldEndPuffer)
                angle += abdrehWinkel;
            pos.addX(movementSpeed * Math.cos(angle));
            pos.addY(movementSpeed * Math.sin(angle));
            //System.out.println(pos.getX() + " , "+pos.getY()+ "     "+world.getWidth()+" "+world.getHeight());
            }
        }

    }

    private boolean checkIfOnFS(World world){
        boolean onFS = false;
        for(FoodSource foodSource: world.getFoodSources()){
            if(!foodSource.isEmpty()&&checkIfOn(foodSource.getPosition(),Worker
                    .getWorkerNearFoodVariable())){
                onFS = true;
                foundFoodSource = foodSource;
                foodSource.getScentTrail().setEndPoint(pos);
                foodSource.getScentTrail().setVisible();
                antFood = foodSource.removeFood(Worker.getWorkerFoodCapacity());
            }
        }
        return onFS;
    }

    private boolean searchForScentTrail(World world){
        boolean found =false;
        for(ScentTrail scentTrail: world.getScentTrails()){
            if(scentTrail.isVisible() && scentTrail.near(pos, nearScent)) {
                found = true;
                targetPosition = scentTrail.getTarget();
            }
        }
        return  found;
    }

    private boolean checkIfOn(Position targetPosition, double nearTargetVariable){

        return Methods.samePosition(pos,targetPosition,nearTargetVariable);

    }

    public int getFood() {
        return antFood;
    }

    public void setFood(int f) {
        antFood = f;
    }

    @Override
    public void click(Position p) {

    }

    @Override
    public String getSelectedText() {
        return "Worker foodCarring: "+antFood;
    }

    public static int getExtCostIncrecment(){ return extCostIncrecment; }

    public static int getWorkerLevel(){ return workerLevel; }

    public static int getWorkerFoodCapacity(){ return workerFoodCapacity; }

    public static double getWorkerNearFoodVariable(){ return workerNearFoodVariable; }

}
