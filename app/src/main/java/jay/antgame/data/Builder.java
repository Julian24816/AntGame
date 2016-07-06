package jay.antgame.data;

import jay.antgame.Getter;
import jay.antgame.Methods;

/**
 * Created by Yannick on 04.07.2016.
 */
public class Builder extends Ant implements WorldClicks{

    private static float movementSpeed = 4;

    private Position targetPosition;
    private World world;

    public Builder(Position pos) {
        super(pos);
    }

    @Override
    public void tick(World world) {
        if(targetPosition!=null){

            //Wenn Ziel noch nicht erreicht weiterlaufen
            if(!Methods.samePosition(pos,targetPosition,10)) {
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
                targetPosition = null;
            }

        }
    }

    @Override
    public void click(Position p) {
        targetPosition = p;
        Getter.getMenuManager().getBuilderMenu().setShow(false);
    }

    @Override
    public void longClick(Position p) {
        Getter.getMenuManager().getBuilderMenu().setPosition(p);
        Getter.getMenuManager().getBuilderMenu().setShow(true);
    }

    @Override
    public String getSelectedText() {
        return "Builder";
    }
}
