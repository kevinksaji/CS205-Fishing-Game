package com.example.cs205fishinggame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.os.Build;
import android.util.Log;

public class OxygenManager {
    private final int MAX_OXYGEN = 50;
    private final int OXYGEN_DRAIN_RATE = 1;
    private final int LOW_OXYGEN_THRESHOLD = 5;
    private Context context;

    private int currentOxygen;
    private int maxOxygen = MAX_OXYGEN;
    private long startTime;
    private Vibrator vibrator;


    private boolean isGameOver = false;
    private boolean hasVibrated = false;

    public OxygenManager(Context context) {
        startTime = System.currentTimeMillis();
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        System.out.println("Oxygen created");
        //startLoop();
    }

    public boolean getGameOver() {
        return isGameOver;
    }

    public void loadOxygenPrefs(Context context) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        this.maxOxygen = prefs.getInt("MaxOxygen", Constants.maxOxygen);
        currentOxygen = maxOxygen;
    }

    public int getLowOxygenThreshold() {
        return LOW_OXYGEN_THRESHOLD;
    }


    private int getColorOfCurrentOxy() {
        float percent = (float) currentOxygen / (float) maxOxygen;

        int red = (int) (Color.red(Constants.OXYGENBAR_FILL_START_COLOR) * percent + Color.red(Constants.OXYGENBAR_FILL_END_COLOR) * (1 - percent));
        int green = (int) (Color.green(Constants.OXYGENBAR_FILL_START_COLOR) * percent + Color.green(Constants.OXYGENBAR_FILL_END_COLOR) * (1 - percent));
        int blue = (int) (Color.blue(Constants.OXYGENBAR_FILL_START_COLOR) * percent + Color.blue(Constants.OXYGENBAR_FILL_END_COLOR) * (1 - percent));

        return Color.rgb(red, green, blue);

    }

    public void draw(Canvas canvas) {

        // Draw inner bar first
        Paint paint = new Paint();
        paint.setColor(getColorOfCurrentOxy()); // Set color of the bar

        // Calculate height of the bar based on current countdown value
        int innerBarHeight = (int) (((float) currentOxygen / (float) maxOxygen) * Constants.OXYGENBAR_HEIGHT);

        // Draw inner bar using a rounded rect
        RectF rect = new RectF(Constants.OXYGENBAR_X, Constants.OXYGENBAR_Y + (Constants.OXYGENBAR_HEIGHT - innerBarHeight),
                Constants.OXYGENBAR_X + Constants.OXYGENBAR_WIDTH, Constants.OXYGENBAR_Y + Constants.OXYGENBAR_HEIGHT);
        canvas.drawRoundRect(rect, Constants.OXYGENBAR_CORNER_RADIUS, Constants.OXYGENBAR_CORNER_RADIUS, paint);

        // Draw border
        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(Constants.OXYGENBAR_BORDER_COLOR); // Set color of the bar
        borderPaint.setStrokeWidth(Constants.OXYGENBAR_BORDER_WIDTH);

        RectF borderRect = new RectF(Constants.OXYGENBAR_X, Constants.OXYGENBAR_Y,Constants.OXYGENBAR_X + Constants.OXYGENBAR_WIDTH, Constants.OXYGENBAR_Y + Constants.OXYGENBAR_HEIGHT);
        canvas.drawRoundRect(borderRect, Constants.OXYGENBAR_CORNER_RADIUS,  Constants.OXYGENBAR_CORNER_RADIUS, borderPaint);

    }

    private void checkOxygenLevel(int oxygenLevel) {
        if (oxygenLevel < LOW_OXYGEN_THRESHOLD & !hasVibrated) {
            vibratePhone();
            hasVibrated = true;
        }
    }

    private void vibratePhone() {
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(500); // Vibrate for 500 milliseconds
            }
            Log.d("Vibration", "Phone should be vibrating now");
        }
    }

    public void depleteOxygen(int amount) {
        this.currentOxygen -= amount;
    }

    public void update() {
        if (System.currentTimeMillis() - startTime >= Constants.OXYGEN_DRAIN_RATE * 1000) {
            currentOxygen--;

            startTime = System.currentTimeMillis();
        }

        if (currentOxygen < 0) {
            currentOxygen = 0;
            isGameOver = true;
        }

        checkOxygenLevel(currentOxygen);

        //System.out.println("Current oxygen: " + currentOxygen);
    }


}
