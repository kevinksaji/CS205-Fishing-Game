package com.example.cs205fishinggame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ShopActivity extends AppCompatActivity {
    private MoneyManager moneyManager;
    private SharedPreferences prefs;
    private int currentMoney;
    int oxygenLevel;
    int harpoonLevel;
    int fishLevel;

    TextView levelText1;
    TextView levelText2;
    TextView levelText3;
    private SharedPreferences.Editor editor;

    // private Bitmap coinBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //System.out.println("hi lol");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize SharedPrefs
        prefs = getApplicationContext().getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        editor = prefs.edit();

        currentMoney = prefs.getInt("Money", 0);
        oxygenLevel = prefs.getInt("OxygenLevel", 1);
        harpoonLevel = prefs.getInt("HarpoonLevel", 1);
        fishLevel = prefs.getInt("FishLevel", 1);

        // Init current money
        levelText1 = findViewById(R.id.ShopItemLevel1);
        levelText2 = findViewById(R.id.ShopItemLevel2);
        levelText3 = findViewById(R.id.ShopItemLevel3);
        updateMoneyDisplay();
        updateLevelDisplay();
    }

    private void updateLevelDisplay() {
        if (oxygenLevel == Constants.MAX_UPGRADE) {
            levelText1.setText("(Maxed)");
        } else {
            levelText1.setText("(Level " + oxygenLevel + ")");
        }
        if (harpoonLevel == Constants.MAX_UPGRADE) {
            levelText2.setText("(Maxed)");
        } else {
            levelText2.setText("(Level " + harpoonLevel + ")");
        }
        if (fishLevel == Constants.MAX_UPGRADE) {
            levelText3.setText("(Maxed)");
        } else {
            levelText3.setText("(Level " + fishLevel + ")");
        }
    }

    public void updateMoneyDisplay() {
        // Update moneyAmt value
        TextView moneyAmt = findViewById(R.id.moneyAmt);
        moneyAmt.setText("" + currentMoney);
    }

    // Method called when the 'back to main' button is clicked
    public void onBackButtonClicked(View view) {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        finish();
    }

    public void onUpgradeOxygenButtonClicked(View view) {
        if (oxygenLevel == Constants.MAX_UPGRADE) {
            toastMessage("Oxygen is already max level.");
            return;
        }
        int currentMaxOxygen = prefs.getInt("MaxOxygen", Constants.maxOxygen);
        // Subtract money
        if (currentMoney >= Constants.PRICE_UPGRADE_OXYGEN) {
            int newMoney = currentMoney - Constants.PRICE_UPGRADE_OXYGEN;
            editor.putInt("Money", newMoney);

            // Increase max oxygen
            oxygenLevel++;
            editor.putInt("OxygenLevel", oxygenLevel);
            editor.putInt("MaxOxygen", currentMaxOxygen + Constants.UPGRADE_OXYGEN);
            currentMoney = newMoney;
            editor.apply();
            updateMoneyDisplay();
            updateLevelDisplay();
        } else {
            toastMessage("Not enough money.");
        }
    }

    public void onUpgradeFishButtonClicked(View view) {
        if (fishLevel == Constants.MAX_UPGRADE) {
            toastMessage("Fish is already max level.");
            return;
        }
        int currentMaxFishCount = prefs.getInt("MaxFishCount", Constants.maxFishCount);

        // Subtract money
        if (currentMoney >= Constants.PRICE_UPGRADE_FISHES) {
            int newMoney = currentMoney - Constants.PRICE_UPGRADE_FISHES;
            editor.putInt("Money", newMoney);

            // Increase max fishes
            fishLevel++;
            editor.putInt("FishLevel", fishLevel);
            editor.putInt("MaxFishCount", currentMaxFishCount + Constants.UPGRADE_FISHES);
            currentMoney = newMoney;
            editor.apply();
            updateMoneyDisplay();
            updateLevelDisplay();
        } else {
            toastMessage("Not enough money.");
        }

    }

    public void onUpgradeHarpoonButtonClicked(View view) {
        if (harpoonLevel == Constants.MAX_UPGRADE) {
            toastMessage("Harpoon is already max level.");
            return;
        }

        // Subtract money
        if (currentMoney >= Constants.PRICE_UPGRADE_HARPOON) {
            int newMoney = currentMoney - Constants.PRICE_UPGRADE_HARPOON;
            editor.putInt("Money", newMoney);

            // Increase max fishes
            harpoonLevel++;
            editor.putInt("HarpoonLevel", harpoonLevel);
            currentMoney = newMoney;
            editor.apply();
            updateMoneyDisplay();
            updateLevelDisplay();
        } else {
            toastMessage("Not enough money.");
        }

    }

    private void toastMessage(String message) {
        // Create and show the toast
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
