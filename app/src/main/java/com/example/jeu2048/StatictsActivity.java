package com.example.jeu2048;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StatictsActivity extends AppCompatActivity {

    private TextView bestScoreText, totalGamesText, totalScoreText, avgScoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staticts);

        bestScoreText = findViewById(R.id.text_best_score);
        totalGamesText = findViewById(R.id.text_total_games);
        totalScoreText = findViewById(R.id.text_total_score);
        avgScoreText = findViewById(R.id.text_score_moy);

        SharedPreferences prefs = getSharedPreferences("game", Context.MODE_PRIVATE);
        int best = prefs.getInt("best", 0);
        int totalGames = prefs.getInt("total_games", 0);
        int totalScore = prefs.getInt("total_score", 0);
        int avgScore = totalGames > 0 ? totalScore / totalGames : 0;

        bestScoreText.setText(""+best);
        totalGamesText.setText(""+totalGames);
        totalScoreText.setText(""+totalScore);
        avgScoreText.setText(""+avgScore);
    }
}
