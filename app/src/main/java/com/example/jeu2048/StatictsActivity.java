package com.example.jeu2048;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
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

        databaseHandler db = new databaseHandler(this);
        int best = db.getBestScore();
        int totalGames = db.getTotalGames();
        int totalScore = db.getTotalScore();
        int avgScore = totalGames > 0 ? totalScore / totalGames : 0;

        bestScoreText.setText(String.valueOf(best));
        totalGamesText.setText(String.valueOf(totalGames));
        totalScoreText.setText(String.valueOf(totalScore));
        avgScoreText.setText(String.valueOf(avgScore));

        Button btnShare = findViewById(R.id.btn_share_réseau);
        btnShare.setOnClickListener(v -> {
            String message = "Mon meilleur score sur 2048 est : " + best + " points ! Essaie de me battre !";
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(shareIntent, "Partager via"));
        });

        /*
        TextView menuLink = findViewById(R.id.menu_link);
menuLink.setOnClickListener(v -> {
    Intent intent = new Intent(JeuClassicActivity.this, MenuActivity.class);
    startActivity(intent);
    finish(); // optionnel si tu veux fermer l'activité actuelle
});

        * */

    }


}
