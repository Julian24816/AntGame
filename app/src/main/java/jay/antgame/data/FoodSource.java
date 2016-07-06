package jay.antgame.data;

/**
 * Created by Julian on 24.06.2016.
 */
public class FoodSource implements WorldObject {

    private int sourceFood, maxFood;
    private Position pos;

    public FoodSource (Position pos, int startFood) {
        this.pos = pos;
        maxFood = sourceFood = startFood;
    }

    public int getMaxFood() {
        return maxFood;
    }

    @Override
    public Position getPosition() {
        return pos;
    }

    @Override @Deprecated
    public void setPosition(Position pos) {
        //this.pos = pos;
        throw new RuntimeException("currently changing the Nests Position isn't allowed");
    }

    @Override
    public void click(Position p) {

    }

    @Override
    public String getSelectedText() {
        return "Food Source  remainingFood: "+sourceFood;
    }

    public void lowerFood(int f) {sourceFood=sourceFood - f;}

    public int getFood() {return sourceFood;}

    //Entferne wantedFood wenn genug da ist, sonst entferne alles was noch da ist
    public int removeFood(int wantedFood){
        if(sourceFood>=wantedFood){
            sourceFood -= wantedFood;
            return  wantedFood;
        }else{
            sourceFood = 0;
            return  sourceFood;
        }
    }
}
