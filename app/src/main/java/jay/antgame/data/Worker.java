package jay.antgame.data;

import jay.antgame.GameEngine;
import jay.antgame.Methods;

/**
 * Created by Yannick.Pfeiffer on 29.06.2016.
 */

public class Worker extends Ant {

    //bewegungsgeschwindigkeit pro tick
    private static float movementSpeed = 3;
    //faktor der mit preis multipliziert wird, nachdem ein Worker gekauft wurde
    private static int extCostIncrecment = 2;

    //Level der ameise
    private static int workerLevel = 1;
    private static int workerFoodCapacity = 1;

    //ab wie nah die ameise eine futterquelle bemerkt
    private static double workerNearFoodVariable = 20;

    //Ab wo die Ameise den ScentTrail bemerkt
    private static float nearScent = 10;

    //Essen welches die Ameise mit sich trägt
    private int antFood = 0;

    private FoodSource foundFoodSource;

    //Zufällige max Drehung (in Bogenmaß) welche ein Worker pro tick macht
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
                //wenn vorher eine foodSource gefunden wurde und die Ameise zurück zum bau gelaufen ist
                //wird in ScentTrail die EndPosition des trails von ameise zurück auf nest gesetzt
                if(foundFoodSource!=null)
                    foundFoodSource.getScentTrail().resetEndPoint();
                foundFoodSource = null;
                //drehung wird so angepasst, dass die ameise gerade weiterläuft
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
                //Wenn einer gefunden wird wird die targetPosition die targetPosition des scentTrails
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

    /**
     * Überprüft für alle Futterquellen ob der Worker nahe einer ist
     * @param world
     * @return boolean nahe einer Futterquelle
     */
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

    /**
     * Überprüft für alle ScentTrails ob der Worker nahe einer ist
     * @param world
     * @return boolean nahe eines ScentTrails
     */
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
