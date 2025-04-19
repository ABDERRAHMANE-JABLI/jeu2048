package com.example.jeu2048;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class TutorialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo("lCotJo5L8xk", 0);
            }
        });

        Button tutorial = findViewById(R.id.btn_start_game);
        tutorial.setOnClickListener(v -> {
            Intent intent = new Intent(TutorialActivity.this, JeuClassicActivity.class);
            startActivity(intent);
            startService(new Intent(this, MusicService.class));
        });

        TextView menuLink = findViewById(R.id.menu_link);
        menuLink.setOnClickListener(v -> {
            Intent intent = new Intent(TutorialActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        });
    }

}
