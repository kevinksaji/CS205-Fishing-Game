package com.example.cs205fishinggame;

import android.graphics.Canvas;

/**
 * GameObject is an abstract class for objects in the game
 * with common properties and methods.
 */
public abstract class GameObject {
    protected double positionX;
    protected double positionY;
    protected double velocityX;
    protected double velocityY;

    public GameObject (double positionX, double positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    // Calculate distance between 2 game objects
    public static double getDistanceBetweenGameObjects(GameObject obj1, GameObject obj2) {
        return Math.sqrt(Math.pow(obj1.positionX - obj2.positionX, 2) + Math.pow(obj1.positionY - obj2.positionY, 2));
    }

    // Calculate distance between 2 points
    public static double getDistanceBetweenPoints(double p1x, double p1y, double p2x, double p2y) {
        // Pythagoras theorem
        return Math.sqrt(Math.pow(p1x - p2x, 2) + Math.pow(p1y - p2y, 2));
    }

    // Getter methods for position coordinates
    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    // Abstract methods for drawing and updating the game object
    public abstract void draw(Canvas canvas);
    public abstract void update(float deltaTime);
}
