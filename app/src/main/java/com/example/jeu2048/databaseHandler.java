package com.example.jeu2048;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class databaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "jeu2048.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_STATS = "stats";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_BEST_SCORE = "best_score";
    private static final String COLUMN_TOTAL_SCORE = "total_score";
    private static final String COLUMN_TOTAL_GAMES = "total_games";

    public databaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STATS_TABLE = "CREATE TABLE " + TABLE_STATS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_BEST_SCORE + " INTEGER, "
                + COLUMN_TOTAL_SCORE + " INTEGER, "
                + COLUMN_TOTAL_GAMES + " INTEGER)";
        db.execSQL(CREATE_STATS_TABLE);

        // Initialisation avec une ligne par défaut
        ContentValues values = new ContentValues();
        values.put(COLUMN_BEST_SCORE, 0);
        values.put(COLUMN_TOTAL_SCORE, 0);
        values.put(COLUMN_TOTAL_GAMES, 0);
        db.insert(TABLE_STATS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATS);
        onCreate(db);
    }

    public void updateBestScore(int newBestScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BEST_SCORE, newBestScore);
        db.update(TABLE_STATS, values, COLUMN_ID + " = ?", new String[]{"1"});
        db.close();
    }

    public int getBestScore() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_BEST_SCORE + " FROM " + TABLE_STATS + " WHERE " + COLUMN_ID + " = 1", null);
        int bestScore = 0;
        if (cursor.moveToFirst()) {
            bestScore = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return bestScore;
    }

    public void updateTotalScore(int newTotalScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TOTAL_SCORE, newTotalScore);
        db.update(TABLE_STATS, values, COLUMN_ID + " = ?", new String[]{"1"});
        db.close();
    }

    public int getTotalScore() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_TOTAL_SCORE + " FROM " + TABLE_STATS + " WHERE " + COLUMN_ID + " = 1", null);
        int totalScore = 0;
        if (cursor.moveToFirst()) {
            totalScore = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return totalScore;
    }

    public void updateTotalGames(int newTotalGames) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TOTAL_GAMES, newTotalGames);
        db.update(TABLE_STATS, values, COLUMN_ID + " = ?", new String[]{"1"});
        db.close();
    }

    public int getTotalGames() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_TOTAL_GAMES + " FROM " + TABLE_STATS + " WHERE " + COLUMN_ID + " = 1", null);
        int totalGames = 0;
        if (cursor.moveToFirst()) {
            totalGames = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return totalGames;
    }
}
