package jay.antgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jay.antgame.data.FoodSource;
import jay.antgame.data.Nest;
import jay.antgame.data.Position;
import jay.antgame.data.ScreenPosition;
import jay.antgame.data.Worker;
import jay.antgame.data.World;
import jay.antgame.data.WorldObject;
import jay.antgame.data.menus.Menu;
import jay.antgame.data.menus.MenuManager;

/**
 * Created by Julian on 09.06.2016.
 */
public class GameView extends SurfaceView
        implements SurfaceHolder.Callback{

    private static final long FRAME_INTERVAL = 20;

    private static final float ZOOMFACTOR = 0.4f;

    private static final int NEST_SIZE = 50;
    private static final int FOOD_SOURCE_SIZE = 30;
    private static final int SCENT_TRAIL_SIZE = 10;
    private static final int ANT_SIZE = 5;

    private final float density;
    private final float screenWidth, screenHeight;
    private int height, width;

    private float xShifting = 0;
    private float yShifting = 0;

    private float left,top,right,bottom = 0;

    private ScheduledExecutorService executorService;
    private Runnable renderer = new Runnable() {
        @Override
        public void run() {
            Canvas canvas = null;
            try {
                canvas = getHolder().lockCanvas();
                synchronized (getHolder()) {
                    doDraw(canvas);
                }
            } finally {
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    };

    private World gameWorld;
    private MenuManager menuManager;
    private Paint paintNest = new Paint();
    private Paint paintFoodSource = new Paint();
    private Paint paintScentTrail = new Paint();
    private Paint paintAnt = new Paint();
    private Paint paintGrass = new Paint();

    private HashMap<Class, Paint> paints = new HashMap<>();
    private HashMap<Class, Integer> sizes = new HashMap<>();

    private Paint text = new Paint();
    private static int textSizeShopListItem= 60;
    private static int textSizeShopItemDiscription= 40;

    private Paint paintMenu = new Paint();
    private static float shopBackgroundWidth = 600;

    private int tempTextSize = 55;
    private String tempText = "";
    private int tempTextTime = 0;
    private final int TEMPTEXTTIMESHOWN = 200;

    public GameView(Context context, World gameWorld, MenuManager menuManager,Typeface t) {
        super(context);

        this.gameWorld = gameWorld;
        this.menuManager = menuManager;


        // get metrics
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        density = displayMetrics.density;
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        width = gameWorld.getWidth();
        height = gameWorld.getHeight();
        setGrassSize(width,height);
        System.out.println(top+" "+left);

        // initialise Paint-Objects
        paintNest.setAntiAlias(true);
        paintNest.setColor(Color.DKGRAY);
        paintNest.setStyle(Paint.Style.FILL);

        paintFoodSource.setAntiAlias(true);
        paintFoodSource.setColor(Color.YELLOW);
        paintFoodSource.setStyle(Paint.Style.FILL);

        paintScentTrail.setAntiAlias(true);
        paintScentTrail.setColor(Color.GREEN);
        paintScentTrail.setStyle(Paint.Style.FILL);

        paintAnt.setAntiAlias(true);
        paintAnt.setColor(Color.BLACK);
        paintAnt.setStyle(Paint.Style.FILL);

        paintGrass.setColor(Color.rgb(188,245,169));

        paints.put(Worker.class, paintAnt);
        paints.put(Nest.class, paintNest);
        paints.put(FoodSource.class, paintFoodSource);

        sizes.put(Worker.class, ANT_SIZE);
        sizes.put(Nest.class, NEST_SIZE);
        sizes.put(FoodSource.class, FOOD_SOURCE_SIZE);

        text.setTypeface(t);

        paintMenu.setColor(Color.GRAY);



        getHolder().addCallback(this);
    }

    private ScreenPosition getScreenCoordinates(Position pos) {

        float x = width/2 + pos.getX()*density* ZOOMFACTOR;
        float y = height/2 - pos.getY()*density* ZOOMFACTOR;

        return new ScreenPosition(x,y);
    }

    public Position getWorldPosition(float screenX, float screenY){

        float x = -(width/2-(screenX-xShifting))/density/ZOOMFACTOR;
        float y = (height/2-(screenY-yShifting))/density/ZOOMFACTOR;

        return new Position(x,y);
    }

    public Position getWorldPosition(ScreenPosition pos) {
        return getWorldPosition(pos.getX(), pos.getY());
    }


    private void doDraw(Canvas canvas) {

        // draw Background
        canvas.drawColor(Color.rgb(188,235,169));
        canvas.drawRect(left+xShifting,top+yShifting,right+xShifting,bottom+yShifting,paintGrass);

        for(WorldObject object: gameWorld.getWorldObjects()){
            Position pos = getScreenCoordinates(object.getPosition());
            canvas.drawCircle(xShifting+pos.getX(), yShifting+pos.getY(), sizes.get(object.getClass())*density* ZOOMFACTOR /2,
                    paints.get(object.getClass()));
        }

        text.setTextSize(50);
        canvas.drawText("Food: "+gameWorld.getFood(), screenWidth-200, 100, text);

        WorldObject selectedObject = gameWorld.getSelectedObject();
        if(selectedObject!=null){
            canvas.drawText(selectedObject.getSelectedText(), 100, 100, text);
        }

        //Draw all menus
        for(Menu menu: menuManager.getAllMenus()){
            if(menu.showList()){

                text.setTextSize(textSizeShopListItem);
                int itemHeight = textSizeShopListItem + textSizeShopItemDiscription;
                float shopBackgroundHeigth = menu.getIdsOfShownOffersList().size() * itemHeight;
                ScreenPosition menuPosition = getScreenCoordinates(menu.getPosition());
                canvas.drawRect(menuPosition.getX() - shopBackgroundWidth / 2 +xShifting, menuPosition.getY() - shopBackgroundHeigth / 2 + yShifting,
                        menuPosition.getX() + shopBackgroundWidth / 2 +xShifting, menuPosition.getY() + shopBackgroundHeigth / 2 + textSizeShopListItem / 4 + yShifting, paintMenu);

                List<Integer> ids = menu.getIdsOfShownOffersList();
                for (int i = 0; i < ids.size(); i++) {
                    String[] parts = menu.getList()[ids.get(i)].split(":");
                    text.setTextSize(textSizeShopListItem);
                    canvas.drawText(parts[0]+" ("+menu.getCosts()[ids.get(i)]+")", menuPosition.getX() - shopBackgroundWidth / 2 + 10 +xShifting,
                            menuPosition.getY() - shopBackgroundHeigth / 2 + text.getTextSize() + i * itemHeight + yShifting, text);
                    text.setTextSize(textSizeShopItemDiscription);
                    canvas.drawText(parts[1], menuPosition.getX() - shopBackgroundWidth / 2 + 10 +xShifting,
                            menuPosition.getY() - shopBackgroundHeigth / 2 + text.getTextSize() + i * itemHeight + textSizeShopListItem + yShifting, text);
                }
            }
        }

        if(tempTextTime>0){
            text.setTextSize(tempTextSize);
            canvas.drawText(tempText, 100 ,screenHeight-screenHeight/10, text);
            tempTextTime--;
        }




    }

    public void setGrassSize(int width, int height){
        Position topLeft = new Position(-width/2,height/2);
        Position bottomRight = new Position(width/2,-height/2);
        ScreenPosition screenTopLeft = getScreenCoordinates(topLeft);
        ScreenPosition screenBottomRight = getScreenCoordinates(bottomRight);
        top = screenTopLeft.getY();
        left = screenTopLeft.getX();
        right = screenBottomRight.getX();
        bottom = screenBottomRight.getY();
    }

    public void addShifting(float x, float y){
        if( (xShifting<width&&x>0) || (xShifting>-width&&x<0) )
            xShifting += x;
        if( (yShifting<height&&y>0) || (yShifting>-height&&y<0) )
            yShifting += y;
    }

    public void writeTempText(String text){
        tempText = text;
        tempTextTime = TEMPTEXTTIMESHOWN;
    }

    public static int getMenuItemHeight(){ return textSizeShopListItem + textSizeShopItemDiscription; }

    public static float getMenuHeight(Menu menu){
        return menu.getIdsOfShownOffersList().size() * (textSizeShopListItem + textSizeShopItemDiscription);
    }

    public static float getMenuWidth(){ return shopBackgroundWidth; }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(renderer,
                FRAME_INTERVAL, FRAME_INTERVAL, TimeUnit.MILLISECONDS);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        executorService.shutdown();
    }

    public int getWorldWidth(){ return width; }

    public int getWorldHeight(){ return height; }


    //Js old drawing method
    /*
        // draw Nest
        Position nestPos = getScreenCoordinates(gameWorld.getNest().getPosition());
        canvas.drawCircle(xShifting+nestPos.getX(), yShifting+nestPos.getY(), NEST_SIZE*density* ZOOMFACTOR /2, paintNest);

        // draw FoodSources
        for (FoodSource source: gameWorld.getFoodSources()) {
            Position sourcePos = getScreenCoordinates(source.getPosition());
            canvas.drawCircle(xShifting+sourcePos.getX(), yShifting+sourcePos.getY(), FOOD_SOURCE_SIZE*density* ZOOMFACTOR /2,
                    paintFoodSource);
        }

        // draw ScentTrails
        for (ScentTrail trail: gameWorld.getScentTrails()) {
            Position sourcePos = getScreenCoordinates(trail.getPosition());
            canvas.drawCircle(xShifting+sourcePos.getX(), yShifting+sourcePos.getY(), SCENT_TRAIL_SIZE*density* ZOOMFACTOR /2,
                    paintScentTrail);
        }

        // draw FoodSources
        for (Ant ant: gameWorld.getAnts()) {
            Position sourcePos = getScreenCoordinates(ant.getPosition());
            canvas.drawCircle(xShifting+sourcePos.getX(), yShifting+sourcePos.getY(), ANT_SIZE*density* ZOOMFACTOR /2,
                    paintAnt);
        }*/

}
