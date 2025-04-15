package com.example.jeu2048;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ImageView;


public class MenuActivity extends AppCompatActivity {

    private boolean isMusicPlaying = true;
    private ImageView musicIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        Intent musicIntent = new Intent(this, MusicService.class);
        startService(musicIntent);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_menu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // fonctionnalités, activer désactiver la music :
        musicIcon = findViewById(R.id.musicIcon);
        musicIcon.setOnClickListener(v -> {
            if (isMusicPlaying) {
                stopService(new Intent(this, MusicService.class));
                musicIcon.setImageResource(R.drawable.baseline_music_off_24);
            } else {
                startService(new Intent(this, MusicService.class));
                musicIcon.setImageResource(R.drawable.baseline_music_note_24);
            }
            isMusicPlaying = !isMusicPlaying;
        });


        // page Jeu Classic :
        Button playClassicBtn = findViewById(R.id.btn_play_classic);
        playClassicBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, JeuClassicActivity.class);
            startActivity(intent);
        });

        Button playDefiBtn = findViewById(R.id.btn_play_defi);
        playDefiBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, DefiActivity.class);
            startActivity(intent);
        });

        Button tutorial = findViewById(R.id.btn_tutorial);
        tutorial.setOnClickListener(v -> {
            stopService(new Intent(this, MusicService.class));
            musicIcon.setImageResource(R.drawable.baseline_music_off_24);
            Intent intent = new Intent(MenuActivity.this, TutorialActivity.class);
            startActivity(intent);
        });

        Button staticts = findViewById(R.id.btn_statistics);
        staticts.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, StatictsActivity.class);
            startActivity(intent);
        });


    }

}