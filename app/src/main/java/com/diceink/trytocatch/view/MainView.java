package com.diceink.trytocatch.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;

import com.diceink.trytocatch.R;
import com.diceink.trytocatch.activity.MainActivity;

import java.util.Random;

public class MainView extends View {
    private static final Random random = new Random();
    public static boolean IS_RUNNING = false;
    private float touchX = 0;
    private MainActivity mainActivity;

    //Canvas
    private int canvasWidth;
    private int canvasHeight;

    //background
    private Bitmap background;


    //Score
    private Paint scorePaint = new Paint();
    private int score = 0;

    //life
    private Bitmap lifes[] = new Bitmap[2];
    private int life_count = 3;

    //Blue balls
    private int blueberryX;
    private int blueberryY;
    private int blueberrySpeed = 15;
    private Bitmap blueberryPaint;


    //Black balls
    private int logX;
    private int logY;
    private int logSpeed = 25;
    private Bitmap log;

    //bird
    private Bitmap elk;
    private float elkX;
    private float elkY;
    private int minElkX;
    private int maxElkX;

    //Screen size
    private int screenX;
    private int screenY;

    //Sound
    MediaPlayer mediaPlayer;

    public MainView(Context context, int screenX, int screenY) {
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;
        IS_RUNNING = true;
        initElement();
        mainActivity = (MainActivity) context;
        elkY = (float) screenY - elk.getHeight() ;
        elkX = (float) screenX/2;
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.elk_pain);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        touchX = e.getRawX();
        elkX = touchX;
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
        canvas.drawBitmap(background, 0, 0, null);

        //Elk
        minElkX = 0;
        maxElkX = screenX - elk.getHeight();
        if (elkX < minElkX) {
            elkX = minElkX;
        } else if (elkX > maxElkX) {
            elkX = maxElkX;
        }
        canvas.drawBitmap(elk, elkX, elkY, null);

        //Balls
        blueberryRun(canvas);
        logRun(canvas);

        canvas.drawText("Score : " + score, 20, 60, scorePaint);

        //Life
        for (int i = 0; i < 3; i++) {
            int x = (int) (canvasWidth - 350 + lifes[0].getWidth() * 1.5 * i);
            int y = 30;
            if (i < life_count) {
                canvas.drawBitmap(lifes[0], x, y, null);
            } else {
                canvas.drawBitmap(lifes[1], x, y, null);
            }
        }
    }

    private void logRun(Canvas canvas) {
        //Black balls
        logY += logSpeed;
        if (hit(logX, logY)) {
            mediaPlayer.start();
            logY += 10000;
            life_count--;
            if (life_count == 0) {
                mainActivity.callEndActivity(score);
                mediaPlayer.release();
            }
        } else if (logY > screenY) {
            logY = 0 - 200;
            logX = random.nextInt(screenX) + minElkX;
        }
        canvas.drawBitmap(log, logX, logY,  null);
    }

    private void blueberryRun(Canvas canvas) {
        //Blue balls
        blueberryY += blueberrySpeed;
        if (hit(blueberryX, blueberryY)) {
            score += 10;
            blueberryX += 10000;
        }
        if (blueberryY > screenY) {
            blueberryY = 0 - 20;
            blueberryX = random.nextInt(screenX);
        }
        canvas.drawBitmap(blueberryPaint, blueberryX, blueberryY, null);
    }

    private boolean hit(int ballX, int ballY) {
        if (elkX < ballX && ballX < elkX + elk.getWidth() && elkY
                < ballY && ballY < elkY + elk.getHeight()) {
            return true;
        }
        return false;
    }


    private void initElement() {
        Bitmap buff = BitmapFactory.decodeResource(getResources(), R.drawable.top_elk);
        elk =  Bitmap.createScaledBitmap(buff, 400, 400, false);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.forest_background);

        buff = BitmapFactory.decodeResource(getResources(), R.drawable.cartoon__blueberry);
        blueberryPaint = Bitmap.createScaledBitmap(buff, 100, 100, false);

        buff= BitmapFactory.decodeResource(getResources(), R.drawable.cartoon_log);
        log = Bitmap.createScaledBitmap(buff, 100, 100, false);


        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(32);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        lifes[0] = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        lifes[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_g);
    }
}
