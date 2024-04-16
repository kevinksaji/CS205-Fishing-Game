package com.example.cs205fishinggame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    private boolean isRunning = false;
    private GameView game;
    private SurfaceHolder surfaceHolder;
    private static final double MAX_UPS = 30.0;
    private static final double UPS_PERIOD = 1E+3 / MAX_UPS;
    private double averageUPS;
    private double averageFPS;

    public GameThread(GameView gameView, SurfaceHolder surfaceHolder) {
        this.game = gameView;
        this.surfaceHolder = surfaceHolder;
    }

    public double getAverageUPS() {
        return averageUPS;
    }

    public double getAverageFPS() {
        return averageFPS;
    }

    public void startLoop() {
        isRunning = true;
        start();
    }

    // join() to ensure thread can run to completion
    public void stopLoop() {
        isRunning = false;
        game.isPaused = true;
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();

        Canvas canvas = null;
        long startTime = System.currentTimeMillis();
        long elapsedTime;
        long sleepTime;

        int updateCount = 0;
        int frameCount = 0;

        // Game loop
        while (isRunning) {
                if (!game.isPaused) {
                // Try to update and render game
                try {
                    canvas = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        game.update();
                        updateCount++;
                        game.draw(canvas);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                            frameCount++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                updateCount++;
                frameCount++;
                // Pause game loop to not exceed target UPS
                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);
                if (sleepTime > 0) {
                    try {
                        sleep(sleepTime);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                // Skip frames to keep up with target UPS
                while (sleepTime < 0 && updateCount < MAX_UPS - 1) {
                    game.update();
                    updateCount++;
                    elapsedTime = System.currentTimeMillis() - startTime;
                    sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);
                }
                // Calculate average UPS and FPS
                elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime >= 1000) {
                    averageUPS = updateCount / (1E-3 * elapsedTime);
                    averageFPS = frameCount / (1E-3 * elapsedTime);
                }
            }
        }
    }
}
