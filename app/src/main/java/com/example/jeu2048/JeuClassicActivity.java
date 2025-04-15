package com.example.jeu2048;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AlertDialog;
public class JeuClassicActivity extends AppCompatActivity {

    private int[][] grid = new int[4][4];
    private TextView[][] tileViews = new TextView[4][4];
    private GridLayout gridLayout;
    private TextView scoreView, bestScoreView;
    private Button btnNew, btnUndo;
    private int score = 0;
    private int bestScore = 0;

    private int[][] previousGrid = new int[4][4];
    private int previousScore = 0;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu_classic);

        gestureDetector = new GestureDetector(this, new JeuClassicActivity.GestureListener());

        gridLayout = findViewById(R.id.grid);
        scoreView = findViewById(R.id.score);
        bestScoreView = findViewById(R.id.best_score);
        btnNew = findViewById(R.id.btn_new);
        btnUndo = findViewById(R.id.btn_undo);

        btnNew.setOnClickListener(v -> resetGame());

        btnUndo.setOnClickListener(v -> {
            GameUtils.copyGrid(previousGrid, grid);
            score = previousScore;
            updateScore();
            GameUtils.updateUI(grid, tileViews);
        });

        GameUtils.createGridUI(this, gridLayout, tileViews);
        loadGame();
        GameUtils.incrementTotalGames(this);
        GameUtils.incrementStatistics(this, bestScore);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveGame();
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
    }

    private void updateScore() {
        scoreView.setText("Score: " + score);
        bestScoreView.setText("Best: " + bestScore);
        GameUtils.incrementStatistics(this, score);
        if (score > bestScore) {
            bestScore = score;
            bestScoreView.setText("Best: " + bestScore);
        }
    }

    private void checkGameState() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (grid[i][j] == 2048) {
                    showWinDialog();
                    return;
                }
            }
        }
        if (isGameOver()) {
            showGameOverDialog();
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

    private void showWinDialog() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.success)
                .setTitle("Victoire !")
                .setMessage("Tu as atteint 2048 ! Tu peux continuer à jouer.")
                .setPositiveButton("Continuer", null)
                .setNegativeButton("Rejouer", (dialog, which) -> resetGame())
                .show();
    }

    private void showGameOverDialog() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.faillure)
                .setTitle("Game Over")
                .setMessage("Plus de mouvements possibles. Score final : " + score)
                .setPositiveButton("Rejouer", (dialog, which) -> resetGame())
                .setCancelable(false)
                .show();
    }

    public void onSwipeLeft() {
        GameUtils.copyGrid(grid, previousGrid);
        previousScore = score;
        final int[] tempScore = {score};
        boolean moved = GameUtils.swipeLeft(grid, tempScore, updated -> {
            this.score = updated;
            updateScore();
        });
        if (moved) {
            GameUtils.generateRandomTile(grid);
            updateScore();
            GameUtils.updateUI(grid, tileViews);
            checkGameState();
        }
    }

    public void onSwipeRight() {
        GameUtils.copyGrid(grid, previousGrid);
        previousScore = score;
        final int[] tempScore = {score};
        boolean moved = GameUtils.swipeRight(grid, tempScore, updated -> {
            this.score = updated;
            updateScore();
        });
        if (moved) {
            GameUtils.generateRandomTile(grid);
            updateScore();
            GameUtils.updateUI(grid, tileViews);
            checkGameState();
        }
    }

    public void onSwipeUp() {
        GameUtils.copyGrid(grid, previousGrid);
        previousScore = score;
        final int[] tempScore = {score};
        boolean moved = GameUtils.swipeUp(grid, tempScore, updated -> {
            this.score = updated;
            updateScore();
        });
        if (moved) {
            GameUtils.generateRandomTile(grid);
            updateScore();
            GameUtils.updateUI(grid, tileViews);
            checkGameState();
        }
    }

    public void onSwipeDown() {
        GameUtils.copyGrid(grid, previousGrid);
        previousScore = score;
        final int[] tempScore = {score};

        boolean moved = GameUtils.swipeDown(grid, tempScore, updated -> {
            this.score = updated;
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

    private void saveGame() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                sb.append(grid[i][j]).append(",");
            }
        }

        getSharedPreferences("game", MODE_PRIVATE)
                .edit()
                .putString("grid", sb.toString())
                .putInt("score", score)
                .putInt("best", bestScore)
                .apply();
    }

    private void loadGame() {
        String gridData = getSharedPreferences("game", MODE_PRIVATE)
                .getString("grid", null);
        score = getSharedPreferences("game", MODE_PRIVATE).getInt("score", 0);
        bestScore = getSharedPreferences("game", MODE_PRIVATE).getInt("best", 0);

        if (gridData != null) {
            String[] values = gridData.split(",");
            int index = 0;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    grid[i][j] = Integer.parseInt(values[index++]);
                }
            }
        } else {
            resetGame();
        }

        updateScore();
        GameUtils.updateUI(grid, tileViews);
    }
}
