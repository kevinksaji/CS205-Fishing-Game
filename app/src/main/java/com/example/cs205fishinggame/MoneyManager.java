package com.example.cs205fishinggame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class MoneyManager {
    private int money;
    private SharedPreferences prefs;

    public MoneyManager() {
        this.money = 0; // Setting the initial amount of money by the player to 0
    }

    public void addMoney(int amount) {
        this.money += amount;
    }

    public void subtractMoney(int amount) {
        if (amount <= this.money) {
            this.money -= amount;
        }
    }

    private long incMoneyStart = -1;

    public void draw(Canvas canvas, Bitmap coinBitmap) {
        // Draw coin bar border
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Constants.COINBAR_BORDER_COLOR); // Set color of the bar
        paint.setStrokeWidth(Constants.COINBAR_BORDER_WIDTH);

        RectF rect = new RectF(Constants.COINBAR_X, Constants.COINBAR_Y, Constants.COINBAR_X + Constants.COINBAR_WIDTH,
                Constants.COINBAR_Y + Constants.COINBAR_HEIGHT);
        canvas.drawRoundRect(rect, Constants.COINBAR_CORNER_RADIUS, Constants.COINBAR_CORNER_RADIUS, paint);

        // Draw coin bar filling
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Constants.COINBAR_FILL_COLOR);

        canvas.drawRoundRect(rect, Constants.COINBAR_CORNER_RADIUS, Constants.COINBAR_CORNER_RADIUS, paint);

        // Draw coin icon
        Rect coinDst = new Rect(Constants.COINICON_X, Constants.COINICON_Y, Constants.COINICON_X + coinBitmap.getWidth(), Constants.COINICON_Y + coinBitmap.getHeight());
        canvas.drawBitmap(coinBitmap, null, coinDst, null);

        // Draw money value
        String moneyText = Integer.toString(money);
        paint = new Paint();
        paint.setColor(Constants.COINVAL_COLOR);
        paint.setTextSize(Constants.COINVAL_TEXT_SIZE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Constants.COINVAL_TYPEFACE);
        canvas.drawText(moneyText, Constants.COINVAL_X, Constants.COINVAL_Y - paint.ascent(), paint); // Can change the coordinates for the text as needed
    }

    public int getMoney() {
        return this.money;
    }

    public void saveMoney(Context context) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Money", this.money);
        editor.apply();
    }

    public void loadMoney(Context context) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        this.money = prefs.getInt("Money", 0); // Default to 0 if not found
    }
}
