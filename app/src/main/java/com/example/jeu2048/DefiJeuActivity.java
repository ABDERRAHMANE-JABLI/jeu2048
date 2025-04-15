package com.example.jeu2048;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DefiJeuActivity extends AppCompatActivity {

    private int[][] grid = new int[4][4];
    private int[][] previousGrid = new int[4][4];
    private int previousScore = 0;
    private int score = 0;
    private int targetScore = 512;
    private int timeLimit = 60; // par défaut 60 secondes

    private TextView[][] tileViews = new TextView[4][4];
    private GridLayout gridLayout;
    private TextView scoreView, targetScoreView, timerView;
    private Button btnNew, btnUndo;

    private CountDownTimer countDownTimer;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defi_jeu);

        gestureDetector = new GestureDetector(this, new GestureListener());

        gridLayout = findViewById(R.id.grid);
        scoreView = findViewById(R.id.score);
        targetScoreView = findViewById(R.id.target_score);
        timerView = findViewById(R.id.timer_text);
        btnNew = findViewById(R.id.btn_new);
        btnUndo = findViewById(R.id.btn_undo);

        targetScore = getIntent().getIntExtra("target_score", 512);
        timeLimit = getIntent().getIntExtra("time_limit", 60);

        btnNew.setOnClickListener(v -> resetGame());

        btnUndo.setOnClickListener(v -> {
            GameUtils.copyGrid(previousGrid, grid);
            score = previousScore;
            updateScore();
            GameUtils.updateUI(grid, tileViews);
        });

        GameUtils.createGridUI(this, gridLayout, tileViews);
        resetGame();
        startTimer();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    private void resetGame() {
        score = 0;
        updateScore();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                grid[i][j] = 0;
            }
        }
        GameUtils.generateRandomTile(grid);
        GameUtils.generateRandomTile(grid);
        GameUtils.updateUI(grid, tileViews);
        if (countDownTimer != null) countDownTimer.cancel();
        startTimer();
    }

    private void updateScore() {
        scoreView.setText("Score: " + score);
        targetScoreView.setText("Cible: " + targetScore);
    }

    private void checkGameState() {
        if (score >= targetScore) {
            showResultDialog("Good","Défi Réussi.");
            if (countDownTimer != null) countDownTimer.cancel();
        } else if (isGameOver()) {
            showResultDialog("Game Over","Défi Echoué.");
            if (countDownTimer != null) countDownTimer.cancel();
        }
    }

    private boolean isGameOver() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (grid[i][j] == 0) return false;
                if (j < 3 && grid[i][j] == grid[i][j + 1]) return false;
                if (i < 3 && grid[i][j] == grid[i + 1][j]) return false;
            }
        }
        return true;
    }

    private void showResultDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    Intent intent = new Intent(this, MenuActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setCancelable(false)
                .show();
    }


    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLimit * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000);
                timerView.setText("Temps : " + secondsLeft + "s");
            }

            @Override
            public void onFinish() {
                if (score < targetScore) {
                    showResultDialog("Game Over","Temps écoulé ! Défi échoué.");
                } else {
                    showResultDialog("Good","Défi Réussi.");                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void onSwipeLeft() {
        GameUtils.copyGrid(grid, previousGrid);
        previousScore = score;
        final int[] tempScore = {score};

        boolean moved = GameUtils.swipeLeft(grid, tempScore, updated -> {
            score = updated;
            updateScore();
        });

        if (moved) {
            GameUtils.generateRandomTile(grid);
            GameUtils.updateUI(grid, tileViews);
            checkGameState();
        }
    }

    public void onSwipeRight() {
        GameUtils.copyGrid(grid, previousGrid);
        previousScore = score;
        final int[] tempScore = {score};

        boolean moved = GameUtils.swipeRight(grid, tempScore, updated -> {
            score = updated;
            updateScore();
        });

        if (moved) {
            GameUtils.generateRandomTile(grid);
            GameUtils.updateUI(grid, tileViews);
            checkGameState();
        }
    }

    public void onSwipeUp() {
        GameUtils.copyGrid(grid, previousGrid);
        previousScore = score;
        final int[] tempScore = {score};

        boolean moved = GameUtils.swipeUp(grid, tempScore, updated -> {
            score = updated;
            updateScore();
        });

        if (moved) {
            GameUtils.generateRandomTile(grid);
            GameUtils.updateUI(grid, tileViews);
            checkGameState();
        }
    }

    public void onSwipeDown() {
        GameUtils.copyGrid(grid, previousGrid);
        previousScore = score;
        final int[] tempScore = {score};

        boolean moved = GameUtils.swipeDown(grid, tempScore, updated -> {
            score = updated;
            updateScore();
        });

        if (moved) {
            GameUtils.generateRandomTile(grid);
            GameUtils.updateUI(grid, tileViews);
            checkGameState();
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float vx, float vy) {
            float dx = e2.getX() - e1.getX();
            float dy = e2.getY() - e1.getY();

            if (Math.abs(dx) > Math.abs(dy)) {
                if (Math.abs(dx) > SWIPE_THRESHOLD && Math.abs(vx) > SWIPE_VELOCITY_THRESHOLD) {
                    if (dx > 0) onSwipeRight();
                    else onSwipeLeft();
                    return true;
                }
            } else {
                if (Math.abs(dy) > SWIPE_THRESHOLD && Math.abs(vy) > SWIPE_VELOCITY_THRESHOLD) {
                    if (dy > 0) onSwipeDown();
                    else onSwipeUp();
                    return true;
                }
            }
            return false;
        }
    }
}
