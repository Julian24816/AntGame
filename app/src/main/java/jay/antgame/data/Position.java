package jay.antgame.data;

/**
 * Created by Julian on 24.06.2016.
 */
public class Position {
    private float x,y;

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void addX(double x){
        this.x+= x;
    }

    public void addY(double y){
        this.y+= y;
    }

    public Position clone(){
        return new Position(x,y);
    }
    
}
