package com.example.cs205fishinggame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.media.MediaPlayer;

public class MainActivity extends AppCompatActivity {
    PopupWindow settings;
    private MediaPlayer mediaPlayer;
    View popupView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Start the animation if not auto-playing
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Set window to fullscreen (hide status bar)
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        // Initialising MediaPlayer and setting it to the music file
        mediaPlayer = mediaPlayer.create(this, R.raw.main_menu_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inflate the overlay XML layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.settings, null);

        // Create the PopupWindow
        settings = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

    }

    public void buttonClicked(View view) {
        if (view.getId() == R.id.imageButton3) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.imageButton) {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.imageButton5) {
            Intent intent = new Intent(this, ShopActivity.class);
            startActivity(intent);
        }
    }

    public void openSettings(View view) {

        // Show the PopupWindow at the center of the screen
        settings.showAtLocation(view, Gravity.CENTER, 0, 0);

        // Load settings
        CheckBox upsBox = popupView.findViewById(R.id.checkBox1);
        CheckBox fpsBox = popupView.findViewById(R.id.checkBox2);
        SeekBar seekBar = popupView.findViewById(R.id.seekBarVolume);

        SharedPreferences prefs = this.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        upsBox.setChecked(prefs.getBoolean("drawUPS", false));
        fpsBox.setChecked(prefs.getBoolean("drawFPS", false));
        seekBar.setProgress(prefs.getInt("volume", 50));
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    // When the progress value has changed
                    @Override
                    public void onProgressChanged(
                            SeekBar seekBar,
                            int progress,
                            boolean fromUser) {
                        SharedPreferences prefs = MainActivity.this.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("volume", progress);
                        editor.apply();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                        // This method will automatically
                        // called when the user touches the SeekBar
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                        // This method will automatically
                        // called when the user
                        // stops touching the SeekBar
                    }
                });
    }

    public void onSeekBarChanged(SeekBar seekBar, int progress, boolean fromUser) {
        SharedPreferences prefs = this.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("volume", progress);
        editor.apply();
    }

    public void onDisplayUPSClicked(View view) {
        // Check if the CheckBox is checked
        CheckBox checkBox = (CheckBox) view;
        boolean isChecked = checkBox.isChecked();
        SharedPreferences prefs = this.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("drawUPS", isChecked);
        editor.apply();
    }

    public void onDisplayFPSClicked(View view) {
        // Check if the CheckBox is checked
        CheckBox checkBox = (CheckBox) view;
        boolean isChecked = checkBox.isChecked();
        SharedPreferences prefs = this.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("drawFPS", isChecked);
        editor.apply();
    }

    public void closeSettings(View view) {
        // Dismiss the PopupWindow
        if (settings != null && settings.isShowing()) {
            settings.dismiss();
        }
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