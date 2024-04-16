package com.example.cs205fishinggame;

import android.graphics.Color;
import android.graphics.Typeface;

public class Constants {
    public static double HARPOON_SPEED = 2000;

    public static final double HARPOON_DAMPING_FACTOR = 0.4;
    public static final double HARPOON_RETURN_THRESH = 50;
    public static final double CATCH_SPEED = 200;
    public static final float MIN_FISH_SPEED = 75;
    public static final float MAX_FISH_SPEED = 300;
    public static final double BUBBLE_Y_SPEED = 9;
    public static final double BUBBLE_X_SPEED = 5;
    public static final double BUBBLE_X_THRESH = 10;
    public static final double RETRACT_SPEED = 400;
    public static final int BUBBLE_THREADS = 5;
    public static final int BUBBLE_COUNT = 20;
    public static final int PRICE_UPGRADE_HARPOON = 250;
    public static int HARPOON_SPEED_PER_LEVEL = 250;
    public static int CANVAS_WIDTH;
    public static int CANVAS_HEIGHT;
    public static int maxFishCount = 10;
    public static final float COINICON_SCALE = 0.5f;
    public static final int GAMEOVER_DURATION = 2;
    public static final int GAMEOVER_TEXT_SIZE = 200;

    public static final String GAMEOVER_TEXT = "OUT OF OXYGEN!";
    public static final int GAMEOVER_TEXT_COLOR = Color.BLUE;
    public static final int UPS_FPS_COLOR = Color.MAGENTA;
    public static final int UPS_FPS_TEXT_SIZE = 50;
    public static int COINBAR_X, COINBAR_Y;
    public static final int COINBAR_WIDTH = 400;
    public static final int COINBAR_HEIGHT = 100;
    public static int COINICON_X, COINICON_Y;
    public static int COINVAL_X, COINVAL_Y;

    public static final int COINVAL_COLOR = Color.BLACK;
    public static final int COINVAL_TEXT_SIZE = 50;
    public static final Typeface COINVAL_TYPEFACE = Typeface.SANS_SERIF;

    public static int maxOxygen = 35;
    public static final int OXYGEN_DRAIN_RATE = 1;
    public static int OXYGENBAR_X, OXYGENBAR_Y, OXYGENBAR_HEIGHT, OXYGENBAR_WIDTH;

    public static final float OXYGENBAR_CORNER_RADIUS = 30;

    public static final int OXYGENBAR_FILL_START_COLOR = Color.GREEN;
    public static final int OXYGENBAR_FILL_END_COLOR = Color.RED;
    public static final int OXYGENBAR_BORDER_WIDTH = 5;
    public static final int OXYGENBAR_BORDER_COLOR = Color.BLACK;
    public static final float COINBAR_CORNER_RADIUS = 60;

    public static final int COINBAR_BORDER_WIDTH = 5;
    public static final int COINBAR_BORDER_COLOR = Color.BLACK;
    public static final int COINBAR_FILL_COLOR = Color.WHITE;
    public static int JOYSTICK_X, JOYSTICK_Y;
    public static final int JOYSTICK_INNER_RADIUS = 80;
    public static final int JOYSTICK_OUTER_RADIUS = 140;
    public static int PLAYER_X, PLAYER_Y;

    public static final float MERLION_SCALE = 0.3f;
    public static final int MERLION_ALPHA = 100; // Transparency
    public static int MERLION_X, MERLION_Y;

    public static String CHIKI_FONT_ID = "chikibubbles_font.ttf";
    public static String SWIMMING_FONT_ID = "swimmingpool_font.ttf";

    public static final int PRICE_UPGRADE_OXYGEN = 150; // 150

    public static final int UPGRADE_OXYGEN = 10;

    public static final int PRICE_UPGRADE_FISHES = 200; // 200;
    public static final int UPGRADE_FISHES = 3;
    public static final int MAX_UPGRADE = 5;


    public Constants(int widthPixels, int heightPixels) {
        CANVAS_HEIGHT = heightPixels;
        CANVAS_WIDTH = widthPixels;

        COINBAR_X = CANVAS_WIDTH - 750;
        COINBAR_Y = 40;

        COINICON_X = COINBAR_X - 5;
        COINICON_Y = COINBAR_Y - 8;

        COINVAL_X = COINBAR_X + (COINBAR_WIDTH / 2);
        COINVAL_Y = COINBAR_Y + COINBAR_HEIGHT / 3 - 5;

        OXYGENBAR_X = CANVAS_WIDTH - 120;
        OXYGENBAR_Y = CANVAS_HEIGHT / 2;
        OXYGENBAR_HEIGHT = CANVAS_HEIGHT / 2 - 60;
        OXYGENBAR_WIDTH = 70;

        JOYSTICK_X = 275;
        JOYSTICK_Y = CANVAS_HEIGHT - 275;

        HARPOON_SPEED = CANVAS_WIDTH / 2;
        HARPOON_SPEED_PER_LEVEL = CANVAS_WIDTH / 8;

        PLAYER_X = 275;
        PLAYER_Y = CANVAS_HEIGHT / 2;

        MERLION_X = CANVAS_WIDTH / 2 - 50;
        MERLION_Y = CANVAS_HEIGHT / 2 - 50;

    }
}
