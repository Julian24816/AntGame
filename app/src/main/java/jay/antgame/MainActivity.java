package jay.antgame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Julian on 09.06.2016.
 */
public class MainActivity extends Activity
        implements View.OnClickListener {

    private ViewGroup container;
    private GameView gameView;
    private GameEngine gameEngine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface t = Typeface.createFromAsset(getAssets(), "airmole.ttf");

        TextView title = (TextView) findViewById(R.id.title);
        title.setTypeface(t);
        title.setOnClickListener(this);

        container = (ViewGroup) findViewById(R.id.container);
    }



    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.title) {
            Animation a = AnimationUtils.loadAnimation(this, R.anim.abc_fade_out);
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override public void onAnimationStart(Animation animation) {}
                @Override public void onAnimationRepeat(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    startGame();
                }
            });
            v.startAnimation(a);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (gameEngine!=null) gameEngine.stop();
    }


    private void startGame() {

        findViewById(R.id.title).setVisibility(View.GONE);

        gameView = new GameView(this);

        container.addView(gameView,
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        gameEngine = new GameEngine(gameView);
        /*
         * setup gameEngine
         */
        gameEngine.start();
    }
}
