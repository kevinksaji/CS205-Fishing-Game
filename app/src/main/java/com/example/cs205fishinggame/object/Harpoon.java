package com.example.cs205fishinggame.object;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.example.cs205fishinggame.Constants;
import com.example.cs205fishinggame.Fish;
import com.example.cs205fishinggame.GameObject;
import com.example.cs205fishinggame.Player;

import java.util.ArrayList;
import java.util.List;

/*
 * Harpoon class represents a harpoon object in the game.
 * It is launched by the player and can capture fish.
 * It will retract to the player after losing speed.
 */
public class Harpoon extends GameObject {
    private RectF hitbox;
    private Paint ropePaint;
    private Player player;
    private Paint tipPaint;
    private double dirX;
    private double dirY;

    private static double harpoonSpeed = -1;
    private static double dampingFactor = -1;
    private boolean retracting = false;
    private List<Fish> fishList;

    public Harpoon(Player player, double strengthX, double strengthY, double upgradeStrength) {
        super(player.getPositionX(), player.getPositionY());

        // Initialize harpoon speed
        if (harpoonSpeed == -1) {
            harpoonSpeed = Constants.HARPOON_SPEED + upgradeStrength;
        }

        // Initialize dampening factor
        if (dampingFactor == -1) {
            dampingFactor = Constants.HARPOON_DAMPING_FACTOR;
        }

        // Initialize velocity and direction
        velocityX = strengthX * harpoonSpeed;
        velocityY = strengthY * harpoonSpeed;
        double distance = getDistanceBetweenPoints(0, 0, velocityX, velocityY);
        dirX = velocityX / distance;
        dirY = velocityY / distance;

        // Initialize paints for the tip of the harpoon
        tipPaint = new Paint();
        tipPaint.setColor(Color.WHITE);

        // Initialize paints for the rope
        this.player = player;
        ropePaint = new Paint();
        ropePaint.setAntiAlias(true);
        ropePaint.setStrokeWidth(20);
        ropePaint.setColor(Color.rgb(88, 57, 39));
        ropePaint.setStyle(Paint.Style.STROKE);

        // Initialize hitbox of the harpoon
        hitbox = new RectF((float) (player.getPositionX() - 12.5), (float) (player.getPositionY() - 12.5), (float) (player.getPositionX() + 12.5), (float) (player.getPositionY() + 12.5));

        // Initialize fish caught list
        fishList = new ArrayList<Fish>();
    }

    // Method to draw the harpoon on the canvas
    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine(
                (float) (player.getPositionX()),
                (float) (player.getPositionY()),
                (float) (positionX),
                (float) (positionY),
                ropePaint
        );
        canvas.drawCircle((float) positionX, (float) positionY, 25, tipPaint);
    }

    // Method to check if the harpoon has collided with a fish
    public boolean hasCollided(RectF fish) {
        if (hitbox.intersect(fish)) {
            return true;
        }
        return false;
    }

    // Method to check if the harpoon is retracting
    public boolean isRetracting() {
        return retracting;
    }

    // Method to add a fish to the list of caught fish, will slow down when colliding
    public void addFish(Fish fish) {
        fishList.add(fish);
        velocityX *= dampingFactor;
        velocityY *= dampingFactor;
    }

    // Method to retrieve the list of caught fish
    public List<Fish> getFishList() {
        return fishList;
    }

    // Method to update the harpoon's position and behavior
    @Override
    public void update(float deltaTime) {
        positionX += velocityX * deltaTime;
        positionY += velocityY * deltaTime;
        // Update hitbox to move with tip
        hitbox.set((int) (positionX - 12.5), (int) (positionY - 12.5), (int) (positionX + 12.5), (int) (positionY + 12.5));

        if (!retracting) {
            // slow down velocityx and y
            // Apply damping to slow down velocity
            float dampingFactor = (float) Math.pow(Constants.HARPOON_DAMPING_FACTOR, deltaTime);

            velocityX *= dampingFactor;
            velocityY *= dampingFactor;

            // If velocity is close to zero, set it to zero to prevent infinite small updates
            if (getSpeed() < Constants.HARPOON_RETURN_THRESH) { //1) {
                velocityX = 0;
                velocityY = 0;
            }

            // set it to retracting and change velocity to opposite
            if (velocityX == 0 && velocityY == 0) {
                retracting = true;
                velocityX = -dirX * Constants.RETRACT_SPEED;
                velocityY = -dirY * Constants.RETRACT_SPEED;
            }
        }
    }

    // Method to get the harpoon speed
    public double getSpeed() {
        return Math.sqrt(velocityX * velocityX + velocityY * velocityY);
    }
}
