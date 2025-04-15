package com.example.jeu2048;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DefiActivity extends AppCompatActivity {

    private EditText inputTargetScore, inputTimeLimit;
    private Button btnStartDefi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defi);

        inputTargetScore = findViewById(R.id.input_target_score);
        inputTimeLimit = findViewById(R.id.input_time_limit);
        btnStartDefi = findViewById(R.id.btn_start_defi);

        btnStartDefi.setOnClickListener(v -> {
            String scoreStr = inputTargetScore.getText().toString();
            String timeStr = inputTimeLimit.getText().toString();
            int targetScore, timeLimit;

            if (scoreStr.isEmpty() || timeStr.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir les deux champs", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                targetScore = Integer.parseInt(scoreStr);
                timeLimit = Integer.parseInt(timeStr);
            }
            catch (Exception e){
                Toast.makeText(this, "Veuillez entrer des valeurs valides", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, DefiJeuActivity.class);
            intent.putExtra("target_score", targetScore);
            intent.putExtra("time_limit", timeLimit);
            startActivity(intent);
        });
    }
}