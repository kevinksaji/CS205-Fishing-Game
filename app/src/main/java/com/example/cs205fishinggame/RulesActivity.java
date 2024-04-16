package com.example.cs205fishinggame;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.content.Intent;
import android.media.MediaPlayer;

public class RulesActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_activity);

        TextView rulesTextView = findViewById(R.id.backstoryTextView); // Consider renaming this ID to rulesTextView
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        rulesTextView.startAnimation(fadeInAnimation);
        rulesTextView.setBackground(getResources().getDrawable(R.drawable.textview_border));

        Window window = getWindow();

        // Initialising MediaPlayer and setting it to the music file
        mediaPlayer = mediaPlayer.create(this, R.raw.game_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Update this text to reflect the rules of your game
        String rules = "To survive, you need to catch fish using your harpoon before your oxygen runs out! \n \n Good Luck!";
        rulesTextView.setText(rules);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    // Method called when the back to main button is clicked
    public void onBackToMainClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Method called when the back to backstory page button is clicked
    public void onBackButtonClicked(View view) {
        finish();
    }
}
