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


public class HelpActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        TextView backstoryTextView = findViewById(R.id.backstoryTextView);
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        backstoryTextView.startAnimation(fadeInAnimation);
        backstoryTextView.setBackground(getResources().getDrawable(R.drawable.textview_border));
        Window window = getWindow();

        // Initialising MediaPlayer and setting it to the music file
        mediaPlayer = mediaPlayer.create(this, R.raw.game_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        String backstory = "In the year 2100, the vibrant city-state of Singapore, " +
                "once a bustling metropolis, now lies beneath the waves. " +
                "Climate change and rising sea levels have irreversibly transformed " +
                "the landscape, submerging the once-thriving nation under the ocean's embrace. " +
                "The survivors have to do whatever it takes to survive the murky depths of the unforgiving ocean.";
        backstoryTextView.setText(backstory);
        // Find the button and set up the onClick listener
        findViewById(R.id.toRules).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRulesPage();
            }
        });
    }

    // This method is called when the back button is clicked
    public void onBackButtonClicked(View view) {
        finish(); // Closes this activity and returns to the previous one
    }
    // Method to navigate to the rules page
    private void goToRulesPage() {
        Intent intent = new Intent(HelpActivity.this, RulesActivity.class);
        startActivity(intent);
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
}