package jay.antgame.data.menus;

import java.util.ArrayList;
import java.util.List;

import jay.antgame.GameView;
import jay.antgame.data.Position;
import jay.antgame.data.World;
import jay.antgame.data.WorldObject;

/**
 * Created by Yannick on 04.07.2016.
 */
public class Menu implements WorldObject{

    private String name;
    private MenuManager manager;
    private Position position;
    private boolean show = false;
    private String[] list;
    private int[] costs;
    private ArrayList<String> shownList = new ArrayList<String>();
    private ArrayList<Integer> shownCosts = new ArrayList<>();

    public Menu(String name, MenuManager manager,String[] list, int[] costs){
        this.list = list;
        this.costs = costs;
        this.name = name;
        this.manager = manager;
        for(int i=0;i<list.length;i++){
            shownList.add(list[i]);
            if(i<costs.length)
                shownCosts.add(costs[i]);
        }
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position){
        this.position = position;
    }

    @Override
    public void click(Position p) {
        int line = (int)( ( position.getY()+GameView.getMenuHeight(this)/2-p.getY() ) / GameView.getMenuItemHeight() );
        //System.out.println(world.getShopList()[line]);
        manager.itemClicked(this,line);
    }

    @Override
    public String getSlectedText() {
        return name+" Menu";
    }

    public boolean insideBounds(Position pos, GameView gameView){
        return ( pos.getX()>(position.getX()-gameView.getMenuWidth()/2)&& pos.getX()< (position.getX()+gameView.getMenuWidth()/2) &&
                    pos.getY()>position.getY()-gameView.getMenuHeight(this)/2 && pos.getY()<position.getY()+gameView.getMenuHeight(this)/2 );
    }

    public List<Integer> getCosts(){ return shownCosts; }

    public int[] getAllCosts(){ return costs; }

    public List<String> getList(){ return shownList; }

    public boolean showList(){ return show; }

    public String[] getFullList(){ return list; }

    public void setShow(boolean show){ this.show = show; }

}
