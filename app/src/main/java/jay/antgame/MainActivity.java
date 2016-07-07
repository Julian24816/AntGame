package jay.antgame;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import jay.antgame.data.World;
import jay.antgame.data.menus.MenuManager;

/**
 * Created by Julian on 09.06.2016.
 *
 * @author Julian
 */
public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private ViewGroup container, start_container;
    private GameView gameView;
    private GameEngine gameEngine;
    private GameStorage gameStorage;
    private TouchHandling touchHandling;
    private MenuManager menuManager;

    /**
     * initializes the App
     *
     * Overrides Method from AppCompatActivity;
     * this method gets called when the Activity becomes visible for the first time
     *
     * @param savedInstanceState the saved Instance State
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // show the activity_main layout -> res/layout/activity_main.xml
        setContentView(R.layout.activity_main);

        // load Typeface from assets -> assets/airmole.ttf
        Typeface t = Typeface.createFromAsset(getAssets(), "airmole.ttf");

        // apply this typeface to the Views
        // and make this instance the OnClickListener -> this.OnClick(View v)
        TextView title = (TextView) findViewById(R.id.title);
        if (title != null) {
            title.setTypeface(t);
        }
        TextView start_new = (TextView) findViewById(R.id.start_new);
        if (start_new != null) {
            start_new.setTypeface(t);
            start_new.setOnClickListener(this);
        }
        TextView start_latest = (TextView) findViewById(R.id.start_latest);
        if (start_latest != null) {
            start_latest.setTypeface(t);
            start_latest.setOnClickListener(this);
        }

        start_container = (ViewGroup) findViewById(R.id.start_container);

        // save the View with id container to a field
        // -> will get filled with the GameView in startGame()
        container = (ViewGroup) findViewById(R.id.container);

        // initialize the GameStorage
        gameStorage = new GameStorage(this);
    }


    /**
     * Method of the View.OnClickListener interface.
     *
     * this method gets called when a View with its OnClickListener set to this Instance is clicked
     *
     * @param v the View that was clicked
     */
    @Override
    public void onClick(View v) {

        // if the clicked View was the title view
        if (v.getId()==R.id.start_new) {

            // show an Animation and ...
            Animation a = AnimationUtils.loadAnimation(this, R.anim.abc_fade_out);
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override public void onAnimationStart(Animation animation) {}
                @Override public void onAnimationRepeat(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    //... start the Game on the end of the Animation
                    startGame(gameStorage.getNewWorld());
                }
            });
            start_container.startAnimation(a);
        }

        if (v.getId()==R.id.start_latest) {
            Toast.makeText(this, "not implemented", Toast.LENGTH_SHORT).show();
            Log.e("antgame", "not implemented");
        }


    }

    public boolean onTouchEvent(MotionEvent event) {

        if(touchHandling!=null)
            touchHandling.touchEvent(event);

        return false;
    }

    /**
     * stops the GameEngine's Loop if present
     * the GameView's loop is stopped via GameEngine.onSurfaceDestroyed
     *
     * Overrides Method of AppCompatActivity;
     * this method is called when the Display is locked or the user leaves the App without
     * destroying it.
     * This method is also called when the Application is destroyed; afterwards onDestroy will be
     * called.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (gameEngine!=null) gameEngine.stop();
    }


    /**
     * restarts the GameEngine's Loop if present
     * the GameView's loop is started via GameEngine.onSurfaceCreated
     *
     * Overrides Method of AppCompatActivity;
     * this method is called when the Display is unlocked or the user reenters the App without
     * restarting it.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (gameEngine!=null) gameEngine.resume();
    }


    /**
     * starts the Game with the given World
     *
     * @param world the World to start with
     */
    private void startGame(World world) {

        // remove title View
        start_container.setVisibility(View.GONE);

        //create Menu Manager
        menuManager = new MenuManager(world);

        //create TouchHandling
        touchHandling = new TouchHandling(world, menuManager);

        //set Menu in Nest
        world.getNest().setMenu(menuManager.getNestMenu());

        // create new GameView and show it
        gameView = new GameView(this, world, menuManager , touchHandling ,Typeface.createFromAsset(getAssets() ,"airmole.ttf"));
        container.addView(gameView,
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        //Set GameView in MenuManager
        menuManager.setGameView(gameView);

        //Set GameView in TouchHandling
        touchHandling.setGameView(gameView);

        // create GameEngine and start it
        gameEngine = new GameEngine(gameView, world, touchHandling);

        Getter.set(gameView,gameEngine,touchHandling,menuManager);

        gameEngine.start();
    }

}
