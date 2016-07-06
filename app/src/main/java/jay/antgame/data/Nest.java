package jay.antgame.data;

import jay.antgame.data.menus.Menu;
import jay.antgame.data.menus.MenuManager;

/**
 * Created by Julian on 24.06.2016.
 */
public class Nest implements WorldObject {

    private Position pos;
    private int food;
    private Menu menu;

    public Nest(Position startPos) {
        pos = startPos;
        food = 0;
    }

    public void setMenu(Menu menu){
        this.menu = menu;
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
        menu.setShow(true);
        menu.setPosition(pos);
    }

    @Override
    public String getSelectedText() {
        return "Nest";
    }

    public void addFoodAmount(int f){
        food += f;
    }

    public int getFood() {
        return food;
    }

}
