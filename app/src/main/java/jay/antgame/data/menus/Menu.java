package jay.antgame.data.menus;

import java.util.ArrayList;
import java.util.List;

import jay.antgame.GameView;
import jay.antgame.data.Position;
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
    private ArrayList<Integer> idsOfShownOffersList = new ArrayList<>();

    public Menu(String name, MenuManager manager,String[] list, int[] costs){
        this.list = list;
        this.costs = costs;
        this.name = name;
        this.manager = manager;
        for(int i=0;i<list.length;i++){
            idsOfShownOffersList.add(i);
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
    public String getSelectedText() {
        return name+" Menu";
    }

    public boolean insideBounds(Position pos, GameView gameView){
        return ( pos.getX()>(position.getX()-gameView.getMenuWidth()/2)&& pos.getX()< (position.getX()+gameView.getMenuWidth()/2) &&
                    pos.getY()>position.getY()-gameView.getMenuHeight(this)/2 && pos.getY()<position.getY()+gameView.getMenuHeight(this)/2 );
    }

    public void updateCost(int arrayID, int newCosts){
        costs[arrayID] = newCosts;
        //shownCosts.set(listID, newCosts );
    }

    public int[] getCosts(){ return costs; }

    public String[] getList(){ return list; }

    public List<Integer> getIdsOfShownOffersList(){ return idsOfShownOffersList; }

    public boolean showList(){ return show; }

    public void setShow(boolean show){ this.show = show; }

}
