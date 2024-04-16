package com.example.cs205fishinggame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Bubble extends GameObject {

    private double columnX;
    private int directionX = 1;
    private Paint paint;
    private int radius;
    Random rand;

    public Bubble() {
        super(0, Constants.CANVAS_HEIGHT);
        rand = new Random();
        columnX = rand.nextInt(Constants.CANVAS_WIDTH);
        positionX = columnX;
        positionY += rand.nextInt(Constants.CANVAS_HEIGHT);
        radius = rand.nextInt(25);

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setColor(Color.argb(128, 255,255,255));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle((float) positionX, (float) positionY, radius, paint);
    }

    @Override
    public void update(float deltaTime) {

    }

    public void move() {
        // Make bubble move left and right within a column of x
        if (positionX > columnX + Constants.BUBBLE_X_THRESH) {
            directionX = -1;
        } else if (positionX < columnX - Constants.BUBBLE_X_THRESH) {
            directionX = 1;
        }

        if (positionY < 0) {
            // Reset bubble to bottom of screen
            positionY = Constants.CANVAS_HEIGHT;
            // Randomise size and columnX
            columnX = rand.nextInt(Constants.CANVAS_WIDTH);
            positionX = columnX;
            radius = rand.nextInt(25);
        }


        positionX += Constants.BUBBLE_X_SPEED * directionX;
        positionY -= Constants.BUBBLE_Y_SPEED;
    }
}
