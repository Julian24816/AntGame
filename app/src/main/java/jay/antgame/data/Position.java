package jay.antgame.data;

/**
 * Created by Julian on 24.06.2016.
 */
public class Position {
    private double x,y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void addX(double x){
        this.x+= x;
    }

    public void addY(double y){
        this.x+= y;
    }
    
}
