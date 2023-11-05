package com.example.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {


    ImageView imageView;
    TextView score;
    ConstraintLayout layout;
    AtomicInteger cookieScore = new AtomicInteger();
    Button grandmaButton;
    ImageView grandma;
    int grandmaCounter;
    TextView grandmaPrice;



    public class Passive extends Thread {

        public void run() {
            try {
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        cookieScore.getAndAdd(1 * grandmaCounter);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                score.setText((cookieScore) + " Cookies");
                                if(cookieScore.get() >= 10 + (grandmaCounter * 5))
                                {
                                    grandmaButton.setEnabled(true);
                                } else {
                                    grandmaButton.setEnabled(false);
                                }
                            }
                        });
                    }
                }, 1000, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void placeView(ImageView view, int id) {
        view = new ImageView(this);
        view.setImageResource(id);
        view.setId(View.generateViewId());

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);

        layout.addView(view);

        constraintSet.connect(view.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);
        constraintSet.connect(view.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT);
        constraintSet.connect(view.getId(), ConstraintSet.BOTTOM, layout.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(view.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT);

        constraintSet.applyTo(layout);

        view.getLayoutParams().height = 200;
        view.requestLayout();
        float random = (float) (Math.random() * 1450f);
        view.setX(random - 725f);
        //view.setX()
        view.setY(1150f);

        AlphaAnimation fade = new AlphaAnimation(0f, 1f);
        fade.setDuration(1000);
        view.setAnimation(fade);
        view.animate().rotationBy(720).setDuration(2000);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Passive().start();

        cookieScore.set(0);
        grandmaCounter = 0;

        imageView = findViewById(R.id.imageView);
        imageView.getLayoutParams().height = 1000;
        score = findViewById(R.id.score);

        layout = findViewById(R.id.constraintLayout);
        grandmaButton = findViewById(R.id.button);
        grandmaButton.setEnabled(false);

        grandmaPrice = findViewById(R.id.grandmaPrice);

        final ImageView backgroundOne = (ImageView) findViewById(R.id.background_one);
        final ImageView backgroundTwo = (ImageView) findViewById(R.id.background_two);

        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(10000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = width * progress;
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX - width);
            }
        });
        animator.start();

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,1.3f,1.0f,1.3f, Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF, .5f);
        scaleAnimation.setDuration(500);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageView.startAnimation(scaleAnimation);
                floatingOne();
                cookieScore.getAndAdd(1);
                score.setText(cookieScore +" Cookies");


            }
        });

        grandmaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                placeView(grandma, R.drawable.grandma);
                cookieScore.getAndAdd(-10 - (grandmaCounter * 5));
                grandmaCounter++;
                grandmaButton.setText("Grandma: " + grandmaCounter);
                grandmaPrice.setText("Price " + (10 + (grandmaCounter * 5) + "$"));
                score.setText(cookieScore +" Cookies");

            }
        });

    }

    public void floatingOne() {
        final TextView points = new TextView(MainActivity.this);
        points.setId(View.generateViewId());
        points.setText("+1");
        points.setTextSize(40f);
        points.setTextColor(Color.WHITE);


        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        points.setLayoutParams(params);

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);

        constraintSet.connect(points.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.TOP);
        constraintSet.connect(points.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT);
        constraintSet.connect(points.getId(), ConstraintSet.BOTTOM, layout.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(points.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT);

        constraintSet.applyTo(layout);
        layout.addView(points);

        int randX = (int) (Math.random() * 800) + 200;
        points.setX(randX);
        points.setY(900);

        Animation fadeInAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.floating_one);
        points.startAnimation(fadeInAnimation);
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout.post(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                layout.removeView(points);
                                Log.d("TAG", String.valueOf(layout.getChildCount()));
                            }
                        });
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

}