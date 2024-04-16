package com.example.cs205fishinggame;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import android.media.MediaPlayer;
import android.content.Context;

import androidx.appcompat.view.menu.MenuView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * GameActivity class handles creation of the game view.
 */
public class GameActivity extends Activity {

    private GameView gameView;
    private MediaPlayer mediaPlayer;
    private PopupWindow pauseMenu;
    private PopupWindow endScreen;
    private View endView;
    private boolean isPoppedUp = false;
    Button pauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);

        // Set content view to game so objects in game class can be rendered to screen
        FrameLayout rootLayout = new FrameLayout(this);
        setContentView(rootLayout);

        // Set content view to game so objects in game class can be rendered to screen
        gameView = new GameView(this);
        rootLayout.addView(gameView); // Add GameView to the root layout

        // Create a pause button
        pauseButton = new Button(this);
        pauseButton.setText("||");
        pauseButton.setTextSize(20);

        // Set layout parameters to make the button smaller and position it at the top right corner
        FrameLayout.LayoutParams buttonParams1 = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, // Width
                ViewGroup.LayoutParams.WRAP_CONTENT  // Height
        );
        // Position at top right corner
        buttonParams1.gravity = Gravity.TOP | Gravity.END;
        buttonParams1.setMargins(0, 20, 20, 0);
        pauseButton.setLayoutParams(buttonParams1);

        // Set background color with transparency
        pauseButton.setBackgroundColor(Color.parseColor("#80FFFFFF"));

        // Add the button to the root layout
        rootLayout.addView(pauseButton);

        // Inflate the overlay XML layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.pause_menu, null);

        // Create the PopupWindow
        pauseMenu = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        endView = inflater.inflate(R.layout.end_screen, null);

        // Create the PopupWindow
        endScreen = new PopupWindow(
                endView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        // Set an onClickListener for the pause button
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle menu button click
                // Add your menu logic here
                gameView.pause();

                // Show the PopupWindow at the center of the screen
                pauseMenu.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });


        mediaPlayer = MediaPlayer.create(this, R.raw.game_music);
        mediaPlayer.setLooping(true);

        // Set the volume based on SharedPreferences
        setMediaPlayerVolume();
        mediaPlayer.start();

        hideStatusBar();
    }

    // Method to hide the status bar
    private void hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController wic = getWindow().getInsetsController();
            if (wic != null) {
                wic.hide(WindowInsets.Type.statusBars());
            }
        }
    }

    // Method to resume the game
    public void resume(View view) {
        // Add your menu logic here
        gameView.resume();
        // Dismiss the PopupWindow
        if (pauseMenu != null && pauseMenu.isShowing()) {
            pauseMenu.dismiss();
        }
    }

    // Method to return to the main menu
    public void mainMenu(View view) {
        gameView.stop();
        if (pauseMenu != null && pauseMenu.isShowing()) {
            pauseMenu.dismiss();
        }
        finish();
    }

    private void setMediaPlayerVolume() {
        SharedPreferences prefs = getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        int volumeLevel = prefs.getInt("volume", 50); // Default to 50 if not found

        float volume = volumeLevel / 100f; // Convert to a value between 0 and 1
        mediaPlayer.setVolume(volume, volume);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!gameView.isStopped && !gameView.isPaused) {
            pauseButton.callOnClick();
        }
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gameView.stop();
    }

    public void showEndScreen(int fishCaught, int profit) {// Run UI-related code on the main UI thread
        if (!isPoppedUp) {

            isPoppedUp = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView fishCaughtText = endView.findViewById(R.id.fishesCaughtTextView);
                    fishCaughtText.setText("Fish Caught: " + fishCaught);
                    TextView profitText = endView.findViewById(R.id.profitTextView);
                    profitText.setText("" + profit);

                    // Show the PopupWindow at the center of the screen
                    endScreen.showAtLocation(gameView, Gravity.CENTER, 0, 0);
                }
            });
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
        gameView.stop();
        endScreen.dismiss();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
