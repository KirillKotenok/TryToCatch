package com.diceink.trytocatch.activity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.diceink.trytocatch.R;
import com.diceink.trytocatch.view.MainView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private MainView mainView;
    private Point point;
    private Handler handler;
    public static final String SCORE_KEY = "SCORE";
    private final static long INTERVAL = 30;
    private Intent intent;

    private ImageButton start_btn;
    private ImageButton exit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        point = new Point();
        setContentView(R.layout.activity_main);
        start_btn = findViewById(R.id.start_btn);
        exit_btn = findViewById(R.id.exit_btn);
        getWindowManager().getDefaultDisplay().getSize(point);
        mainView = new MainView(this, point.x, point.y);
        start_btn.setOnClickListener(v -> {
            setContentView(mainView);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mainView.invalidate();
                        }
                    });
                }
            }, 0, INTERVAL);
        });
        exit_btn.setOnClickListener(v -> {
            finish();
        });
    }

    public void callEndActivity(int score) {
        intent = new Intent(this, EndActivity.class);
        intent.putExtra(SCORE_KEY, score);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (MainView.IS_RUNNING) {
            finish();
            System.exit(0);
        } else {
            super.onBackPressed();
        }
    }
}
