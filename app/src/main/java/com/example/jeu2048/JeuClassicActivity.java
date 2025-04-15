package com.example.jeu2048;
/*
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.media.MediaPlayer;

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
            for (int i = 0; i < 4; i++) {
                System.arraycopy(previousGrid[i], 0, grid[i], 0, 4);
            }
            score = previousScore;
            updateScore();
            updateUI();
        });

        createGridUI();
        loadGame();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveGame(); // auto-save
    }


    private void resetGame() {
        score = 0;
        updateScore();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                grid[i][j] = 0;
            }
        }
        generateRandomTile();
        generateRandomTile();
        updateUI();
    }

    private void createGridUI() {
        gridLayout.removeAllViews();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                TextView tile = new TextView(this);
                tile.setText("");
                tile.setGravity(android.view.Gravity.CENTER);
                tile.setTextSize(24);
                tile.setBackgroundColor(Color.LTGRAY);
                tile.setTextColor(Color.BLACK);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 200;
                params.height = 200;
                params.setMargins(8, 8, 8, 8);
                tile.setLayoutParams(params);

                gridLayout.addView(tile);
                tileViews[i][j] = tile;
            }
        }
    }

    private void updateUI() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int val = grid[i][j];
                TextView tile = tileViews[i][j];
                tile.setText(val == 0 ? "" : String.valueOf(val));
                tile.setBackgroundColor(getColorForValue(val));
            }
        }
    }

    private int getColorForValue(int val) {
        switch (val) {
            case 0: return Color.LTGRAY;
            case 2: return Color.parseColor("#EEE4DA");
            case 4: return Color.parseColor("#EDE0C8");
            case 8: return Color.parseColor("#F2B179");
            case 16: return Color.parseColor("#F59563");
            case 32: return Color.parseColor("#F67C5F");
            case 64: return Color.parseColor("#F65E3B");
            case 128: return Color.parseColor("#EDCF72");
            case 256: return Color.parseColor("#EDCC61");
            case 512: return Color.parseColor("#EDC850");
            case 1024: return Color.parseColor("#EDC53F");
            case 2048: return Color.parseColor("#EDC22E");
            default: return Color.DKGRAY;
        }
    }

    private void updateScore() {
        scoreView.setText("Score: " + score);
        bestScoreView.setText("Best: " + bestScore);
        if (score > bestScore) {
            bestScore = score;
            bestScoreView.setText("Best: " + bestScore);
        }
    }

    private void generateRandomTile() {
        List<int[]> empty = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (grid[i][j] == 0) empty.add(new int[]{i, j});

        if (!empty.isEmpty()) {
            int[] pos = empty.get(new Random().nextInt(empty.size()));
            grid[pos[0]][pos[1]] = new Random().nextInt(10) == 0 ? 4 : 2;
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

    private void saveState() {
        for (int i = 0; i < 4; i++) {
            System.arraycopy(grid[i], 0, previousGrid[i], 0, 4);
        }
        previousScore = score;
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
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("\uD83C\uDF89 Victoire !")
                .setMessage("Tu as atteint 2048 ! Tu peux continuer à jouer.")
                .setPositiveButton("Continuer", null)
                .setNegativeButton("Rejouer", (dialog, which) -> resetGame())
                .show();
    }

    private void showGameOverDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("Plus de mouvements possibles. Score final : " + score)
                .setPositiveButton("Rejouer", (dialog, which) -> resetGame())
                .setCancelable(false)
                .show();
    }

    // par souci de place, les onSwipe...() sont à compléter comme dans ton code
    // et tu dois appeler checkGameState() à la fin de chaque "if (moved)" bloc
    public void onSwipeLeft() {
        saveState();
        boolean moved = false;

        for (int i = 0; i < 4; i++) {
            int[] original = grid[i];
            int[] compressed = new int[4];
            int index = 0;

            // Étape 1 : compresser (supprimer les zéros)
            for (int j = 0; j < 4; j++) {
                if (original[j] != 0) {
                    compressed[index++] = original[j];
                }
            }

            // Étape 2 : fusionner
            for (int j = 0; j < 3; j++) {
                if (compressed[j] != 0 && compressed[j] == compressed[j + 1]) {
                    compressed[j] *= 2;
                    score += compressed[j]; // mise à jour du score
                    compressed[j + 1] = 0;
                    moved = true;
                }
            }

            // Étape 3 : recompresser
            int[] newRow = new int[4];
            index = 0;
            for (int j = 0; j < 4; j++) {
                if (compressed[j] != 0) {
                    newRow[index++] = compressed[j];
                }
            }

            // Étape 4 : mettre à jour la grille + détection de mouvement
            for (int j = 0; j < 4; j++) {
                if (grid[i][j] != newRow[j]) {
                    moved = true;
                    grid[i][j] = newRow[j];
                }
            }
        }

        if (moved) {
            generateRandomTile(); // Étape 5 : ajouter une tuile
            updateScore();
            updateUI();
            checkGameState();
        }
    }


    public void onSwipeRight() {
        saveState();
        boolean moved = false;

        for (int i = 0; i < 4; i++) {
            int[] original = grid[i];
            int[] compressed = new int[4];
            int index = 3;

            // Étape 1 : compresser vers la droite
            for (int j = 3; j >= 0; j--) {
                if (original[j] != 0) {
                    compressed[index--] = original[j];
                }
            }

            // Étape 2 : fusion
            for (int j = 3; j > 0; j--) {
                if (compressed[j] != 0 && compressed[j] == compressed[j - 1]) {
                    compressed[j] *= 2;
                    score += compressed[j];
                    compressed[j - 1] = 0;
                    moved = true;
                }
            }

            // Étape 3 : re-compression après fusion
            int[] newRow = new int[4];
            index = 3;
            for (int j = 3; j >= 0; j--) {
                if (compressed[j] != 0) {
                    newRow[index--] = compressed[j];
                }
            }

            // Étape 4 : mise à jour de la grille
            for (int j = 0; j < 4; j++) {
                if (grid[i][j] != newRow[j]) {
                    moved = true;
                    grid[i][j] = newRow[j];
                }
            }
        }

        if (moved) {
            generateRandomTile();
            updateScore();
            updateUI();
            checkGameState();
        }
    }


    public void onSwipeUp() {
        saveState();
        boolean moved = false;

        for (int j = 0; j < 4; j++) { // pour chaque colonne
            int[] column = new int[4];
            int index = 0;

            // Étape 1 : extraire et compresser vers le haut
            for (int i = 0; i < 4; i++) {
                if (grid[i][j] != 0) {
                    column[index++] = grid[i][j];
                }
            }

            // Étape 2 : fusion
            for (int i = 0; i < 3; i++) {
                if (column[i] != 0 && column[i] == column[i + 1]) {
                    column[i] *= 2;
                    score += column[i];
                    column[i + 1] = 0;
                    moved = true;
                }
            }

            // Étape 3 : re-compression
            int[] newCol = new int[4];
            index = 0;
            for (int i = 0; i < 4; i++) {
                if (column[i] != 0) {
                    newCol[index++] = column[i];
                }
            }

            // Étape 4 : mise à jour de la grille
            for (int i = 0; i < 4; i++) {
                if (grid[i][j] != newCol[i]) {
                    moved = true;
                    grid[i][j] = newCol[i];
                }
            }
        }

        if (moved) {
            generateRandomTile();
            updateScore();
            updateUI();
            checkGameState();
        }
    }


    public void onSwipeDown() {
        saveState();
        boolean moved = false;

        for (int j = 0; j < 4; j++) { // pour chaque colonne
            int[] column = new int[4];
            int index = 3;

            // Étape 1 : extraire et compresser vers le bas
            for (int i = 3; i >= 0; i--) {
                if (grid[i][j] != 0) {
                    column[index--] = grid[i][j];
                }
            }

            // Étape 2 : fusion
            for (int i = 3; i > 0; i--) {
                if (column[i] != 0 && column[i] == column[i - 1]) {
                    column[i] *= 2;
                    score += column[i];
                    column[i - 1] = 0;
                    moved = true;
                }
            }

            // Étape 3 : re-compression
            int[] newCol = new int[4];
            index = 3;
            for (int i = 3; i >= 0; i--) {
                if (column[i] != 0) {
                    newCol[index--] = column[i];
                }
            }

            // Étape 4 : mise à jour de la grille
            for (int i = 0; i < 4; i++) {
                if (grid[i][j] != newCol[i]) {
                    moved = true;
                    grid[i][j] = newCol[i];
                }
            }
        }

        if (moved) {
            generateRandomTile();
            updateScore();
            updateUI();
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

    //système de sauvegarde
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
            // nouvelle partie si pas de sauvegarde
            resetGame();
        }

        updateScore();
        updateUI();
    }


}
*/


import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("\uD83C\uDF89 Victoire !")
                .setMessage("Tu as atteint 2048 ! Tu peux continuer à jouer.")
                .setPositiveButton("Continuer", null)
                .setNegativeButton("Rejouer", (dialog, which) -> resetGame())
                .show();
    }

    private void showGameOverDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
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
