package com.example.jeu2048;

import android.content.Context;
import android.graphics.Color;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameUtils {

    public interface ScoreCallback {
        void onScoreUpdated(int newScore);
    }

    public static void createGridUI(Context context, GridLayout gridLayout, TextView[][] tileViews) {
        gridLayout.removeAllViews();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                TextView tile = new TextView(context);
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

    public static void updateUI(int[][] grid, TextView[][] tileViews) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int val = grid[i][j];
                TextView tile = tileViews[i][j];
                tile.setText(val == 0 ? "" : String.valueOf(val));
                tile.setBackgroundColor(getColorForValue(val));
            }
        }
    }

    public static int getColorForValue(int val) {
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

    public static void generateRandomTile(int[][] grid) {
        List<int[]> empty = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (grid[i][j] == 0) empty.add(new int[]{i, j});

        if (!empty.isEmpty()) {
            int[] pos = empty.get(new Random().nextInt(empty.size()));
            grid[pos[0]][pos[1]] = new Random().nextInt(10) == 0 ? 4 : 2;
        }
    }

    public static void copyGrid(int[][] source, int[][] dest) {
        for (int i = 0; i < 4; i++) {
            System.arraycopy(source[i], 0, dest[i], 0, 4);
        }
    }

    public static boolean swipeLeft(int[][] grid, int[] score, ScoreCallback callback) {
        boolean moved = false;
        for (int i = 0; i < 4; i++) {
            int[] compressed = new int[4];
            int index = 0;
            for (int j = 0; j < 4; j++) {
                if (grid[i][j] != 0) compressed[index++] = grid[i][j];
            }
            for (int j = 0; j < 3; j++) {
                if (compressed[j] != 0 && compressed[j] == compressed[j + 1]) {
                    compressed[j] *= 2;
                    score[0] += compressed[j];
                    compressed[j + 1] = 0;
                    moved = true;
                }
            }
            int[] newRow = new int[4];
            index = 0;
            for (int val : compressed) {
                if (val != 0) newRow[index++] = val;
            }
            for (int j = 0; j < 4; j++) {
                if (grid[i][j] != newRow[j]) {
                    grid[i][j] = newRow[j];
                    moved = true;
                }
            }
        }
        if (moved) callback.onScoreUpdated(score[0]);
        return moved;
    }

    public static boolean swipeRight(int[][] grid, int[] score, ScoreCallback callback) {
        boolean moved = false;
        for (int i = 0; i < 4; i++) {
            int[] compressed = new int[4];
            int index = 3;
            for (int j = 3; j >= 0; j--) {
                if (grid[i][j] != 0) compressed[index--] = grid[i][j];
            }
            for (int j = 3; j > 0; j--) {
                if (compressed[j] != 0 && compressed[j] == compressed[j - 1]) {
                    compressed[j] *= 2;
                    score[0] += compressed[j];
                    compressed[j - 1] = 0;
                    moved = true;
                }
            }
            int[] newRow = new int[4];
            index = 3;
            for (int j = 3; j >= 0; j--) {
                if (compressed[j] != 0) newRow[index--] = compressed[j];
            }
            for (int j = 0; j < 4; j++) {
                if (grid[i][j] != newRow[j]) {
                    grid[i][j] = newRow[j];
                    moved = true;
                }
            }
        }
        if (moved) callback.onScoreUpdated(score[0]);
        return moved;
    }

    public static boolean swipeUp(int[][] grid, int[] score, ScoreCallback callback) {
        boolean moved = false;
        for (int j = 0; j < 4; j++) {
            int[] column = new int[4];
            int index = 0;
            for (int i = 0; i < 4; i++) {
                if (grid[i][j] != 0) column[index++] = grid[i][j];
            }
            for (int i = 0; i < 3; i++) {
                if (column[i] != 0 && column[i] == column[i + 1]) {
                    column[i] *= 2;
                    score[0] += column[i];
                    column[i + 1] = 0;
                    moved = true;
                }
            }
            int[] newCol = new int[4];
            index = 0;
            for (int val : column) {
                if (val != 0) newCol[index++] = val;
            }
            for (int i = 0; i < 4; i++) {
                if (grid[i][j] != newCol[i]) {
                    grid[i][j] = newCol[i];
                    moved = true;
                }
            }
        }
        if (moved) callback.onScoreUpdated(score[0]);
        return moved;
    }

    public static boolean swipeDown(int[][] grid, int[] score, ScoreCallback callback) {
        boolean moved = false;
        for (int j = 0; j < 4; j++) {
            int[] column = new int[4];
            int index = 3;
            for (int i = 3; i >= 0; i--) {
                if (grid[i][j] != 0) column[index--] = grid[i][j];
            }
            for (int i = 3; i > 0; i--) {
                if (column[i] != 0 && column[i] == column[i - 1]) {
                    column[i] *= 2;
                    score[0] += column[i];
                    column[i - 1] = 0;
                    moved = true;
                }
            }
            int[] newCol = new int[4];
            index = 3;
            for (int i = 3; i >= 0; i--) {
                if (column[i] != 0) newCol[index--] = column[i];
            }
            for (int i = 0; i < 4; i++) {
                if (grid[i][j] != newCol[i]) {
                    grid[i][j] = newCol[i];
                    moved = true;
                }
            }
        }
        if (moved) callback.onScoreUpdated(score[0]);
        return moved;
    }
}
