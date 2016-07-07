package jay.antgame.data;

import jay.antgame.Getter;
import jay.antgame.Methods;

/**
 * Created by Julian on 24.06.2016.
 */
public class ScentTrail implements WorldObject {

    private int lifetime;
    private int currentLifetime = 0;
    private Position pos;
    private Position target;

    private float a, b;

    private static float notFindingAtNest = 25;

    private Position endPoint;

    public ScentTrail(Position pos, Position target, int lifeTime) {
        this.lifetime = lifeTime;
        this.pos = pos;
        this.target = target;

        a = (target.getY()-pos.getY()) / (target.getX()-pos.getX());
        b = pos.getY() - a*pos.getX();

        lifeTime = (int)(  Math.sqrt( Math.pow(target.getX()-pos.getX(),2) + Math.pow(target.getY()-pos.getY(),2)) /10 * lifeTime  );

        endPoint = pos;
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
        return "";
    }

    public Position getTarget(){
        return target;
    }

    public int getRemainingLifetime(){
        return lifetime;
    }

    public Position getEndPoint1(){ return target; }

    public Position getEndPoint2() {
        return endPoint;
    }

    public boolean isVisible(){
        if(currentLifetime>0){
            currentLifetime --;
            return true;
        }
        return false;
    }

    public void setEndPoint(Position p){
        if(endPoint==pos)
            endPoint = p;
    }

    public void resetEndPoint(){
        endPoint = pos;
    }

    public void setVisible(){
        currentLifetime = lifetime;
    }

    public boolean near(Position p, float howNear){
        float Sx = (p.getY()+ (p.getX()/a) - b ) / (a + 1/a);

        if(!Methods.samePosition(p, Getter.getWorld().getNest().getPosition(), notFindingAtNest)) {

            if (Math.min(endPoint.getX(), target.getX()) <= Sx && Math.max(endPoint.getX(), target.getX()) >= Sx) {

                float Sy = a * Sx + b;
                float l = (float) Math.sqrt(Math.pow((Sx - p.getX()), 2) + Math.pow((Sy - p.getY()), 2));
                if (l <= howNear)
                    return true;

            }

        }
        return false;
    }

}
