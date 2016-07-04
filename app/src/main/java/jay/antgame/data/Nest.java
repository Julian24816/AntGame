package jay.antgame.data;

/**
 * Created by Julian on 24.06.2016.
 */
public class Nest implements WorldObject {

    private Position pos;
    private int food;
    private World world;

    public Nest(Position startPos) {
        pos = startPos;
        food = 0;
    }

    public void setWorld(World world){
        this.world = world;
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
        world.getMenuManager().getNestMenu().setShow(true);
        world.getMenuManager().getNestMenu().setPosition(pos);
    }

    @Override
    public String getSlectedText() {
        return "Nest";
    }

    public void addFoodAmount(int f){
        food += f;
    }

    public int getFood() {
        return food;
    }

}
