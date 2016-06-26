package jay.antgame;

/**
 * Created by Julian on 24.06.2016.
 */
public class Ant implements WorldObject {

    //TODO implement Methods
    private int antFood;
    private int maxFood;

    public Ant(int max) {
        maxFood = max;
    }

    @Override
    public Position getPosition() {
        return null;
    }

    public int getFood() {
        return antFood;
    }

    public void setFood(int f) {
        antFood = f;
    }

    public void foodDelivery() {
        if (ant.getPosition() == Nest.getPosition) {
            Nest.addFoodAmount(antFood);
            Ant.setFood(0);
        }
    }

    public void foodPickup() {
        if (ant.getPosition() == FoodSource.getPosition) {
            if(Ant.getFood() == 0){
                Ant.setFood(maxFood);
                FoodSource.lowerFood(maxFood);
            }
        }
    }
}