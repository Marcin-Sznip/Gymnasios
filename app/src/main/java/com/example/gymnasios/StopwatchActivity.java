package com.example.gymnasios;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Chronometer;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class StopwatchActivity extends AppCompatActivity {

    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;

    private CircularProgressBar progressBar;
    private ObjectAnimator progressAnimator;

    TextView btnBackFromStopwatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        btnBackFromStopwatch = findViewById(R.id.btnBackFromStopwatch);

        btnBackFromStopwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StopwatchActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        progressBar = findViewById(R.id.progress_stopwatch);
        setupProgressBarAnimator();
    }

    private void setupProgressBarAnimator() {
        progressAnimator = ObjectAnimator.ofFloat(progressBar, "progress", 0f, 1000f);
        progressAnimator.setDuration(60000);  // 60 seconds
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        progressAnimator.setRepeatMode(ObjectAnimator.RESTART);
    }

    public void startChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
            progressAnimator.start();
        }
    }

    public void pauseChronometer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
            progressAnimator.pause();
        }
    }

    public void resetChronometer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        progressAnimator.cancel();
        progressBar.setProgress(0f);
    }
}
