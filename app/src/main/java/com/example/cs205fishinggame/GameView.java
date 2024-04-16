package com.example.cs205fishinggame;

import static java.lang.Thread.sleep;

import android.os.Handler;
import android.os.Looper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieDrawable;
import com.example.cs205fishinggame.object.Harpoon;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.example.cs205fishinggame.FishGraphics.FishSpriteSheet;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private HarpoonLauncher harpoonLauncher;
    private GameThread gameThread;
    private Context context;
    private GameActivity activity;
    private LottieDrawable lottieDrawable;
    private List<Harpoon> harpoonList = new ArrayList<Harpoon>();
    private OxygenManager oxygenManager;
    private Player player;
    private Bitmap backgroundBitmap;
    private Bitmap coinBitmap;
    private Bitmap oxygenBitmap;
    private Bitmap merlionBitmap;
    private MoneyManager moneyManager;
    //how many fishes are currently on screen
    int fishCount = 0;
    int fishesCaught = 0;
    int maxFishCount = 10;
    int fishId = 0;
    boolean isPaused = false;

    private boolean isGameOver = false;
    private long gameOverStartTime = -1;

    private final FishSpriteSheet fishSpriteSheet;

    List<Fish> fishes = new ArrayList<Fish>();
    private boolean drawUPSText;
    private boolean drawFPSText;

    private Typeface chikiBubblesFont;

    private SharedPreferences prefs;
    private final BubbleUpdaterPool bubbleUpdaterPool = new BubbleUpdaterPool();
    private final ArrayList<Bubble> bubbles = new ArrayList<Bubble>();

    private final Object mutex = new Object();
    public boolean isStopped = false;

    private Thread diverThread;
    private Handler mainHandler;
    private double harpoonStrength;


    public void loadPreferences(Context context) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        this.drawUPSText = prefs.getBoolean("drawUPS", false); // Default to 0 if not found
        this.drawFPSText = prefs.getBoolean("drawFPS", false); // Default to 0 if not found
    }

    public GameView(GameActivity activity) {
        super((Context) activity);
        this.activity = activity;
        context = (Context) activity;

        // Add callback to surface
        SurfaceHolder surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);

        // Initialize SharedPrefs and load preferences
        prefs = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE);
        loadPreferences(context);

        // Start game thread
        gameThread = new GameThread(this, surfaceHolder);
        setFocusable(true);

        // Initialise fish sprite sheet
        fishSpriteSheet = new FishSpriteSheet(context);

        // Initialise fish
        maxFishCount = prefs.getInt("MaxFishCount", Constants.maxFishCount);
        while (fishCount < maxFishCount) {
            spawnFish();
            fishCount++;
        }

        // Initialise game objects
        oxygenManager = new OxygenManager(context);
        moneyManager = new MoneyManager();

        // Initialising money and oxygen managers
        moneyManager.loadMoney(context);
        setFocusable(true);
        oxygenManager.loadOxygenPrefs(context);

        // Init bitmaps and animations

        initOxygenAndCoin();
        initFonts();

        // Diver Thread
        lottieDrawable = new LottieDrawable();
        mainHandler = new Handler(Looper.getMainLooper());
        DiverThread animationRunnable = new DiverThread(context, lottieDrawable, mainHandler);
        diverThread = new Thread(animationRunnable);
        diverThread.start();
    }

    private void initBackground() {
        // Load the original bitmap
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background2);

        // Create a new bitmap scaled to the size of the canvas
        backgroundBitmap = Bitmap.createScaledBitmap(originalBitmap, getWidth(), getHeight(), true);

        originalBitmap.recycle();

        // Create scaled merlion bitmap
        Bitmap originalMerlionBm = BitmapFactory.decodeResource(getResources(), R.drawable.merlion);
        merlionBitmap = Bitmap.createScaledBitmap(originalMerlionBm, (int) (originalMerlionBm.getWidth() * Constants.MERLION_SCALE), (int) (originalMerlionBm.getHeight() * Constants.MERLION_SCALE), true);
        originalMerlionBm.recycle();

    }

    private void initFonts() {
        chikiBubblesFont = Typeface.createFromAsset(context.getAssets(), Constants.CHIKI_FONT_ID);
    }

    private void initOxygenAndCoin() {
        coinBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.coin_bg_removed);
        coinBitmap = Bitmap.createScaledBitmap(coinBitmap, (int) (coinBitmap.getWidth() * Constants.COINICON_SCALE), (int) (coinBitmap.getHeight() * Constants.COINICON_SCALE), true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        initBackground();
        initOxygenAndCoin();

        // Initialise constants with canvas height and width
        new Constants(getWidth(), getHeight());
        // Init player position
        player = new Player(Constants.PLAYER_X, Constants.PLAYER_Y);
        // Init harpoon launcher position
        harpoonLauncher = new HarpoonLauncher(Constants.JOYSTICK_X, Constants.JOYSTICK_Y, Constants.JOYSTICK_OUTER_RADIUS, Constants.JOYSTICK_INNER_RADIUS, player);
        harpoonStrength = (prefs.getInt("HarpoonLevel", 1) - 1) * Constants.HARPOON_SPEED_PER_LEVEL;

        // Initialise bubbles
        for (int i = 0; i < Constants.BUBBLE_COUNT; ++i) {
            bubbles.add(new Bubble());
        }

        for (int i = 0; i < Constants.BUBBLE_THREADS; ++i) {
            bubbleUpdaterPool.submit(this::bubbleMove);
        }

        gameThread.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        // Save money
        moneyManager.saveMoney(context);

        if (diverThread != null && diverThread.isAlive()) {
            diverThread.interrupt();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isGameOver) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (harpoonLauncher.isPressed((double) event.getX(), (double) event.getY())) {
                    harpoonLauncher.setIsPressed(true);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (harpoonLauncher.getIsPressed()) {
                    harpoonLauncher.setActuator((double) event.getX(), (double) event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
//                Player player = new Player(275,800);
//                System.out.println(player.getPositionX());
//                System.out.println("ID2" + Thread.currentThread().getId());
                // Spawn harpoon
                if (harpoonLauncher.getActuatorX() != 0 || harpoonLauncher.getActuatorY() != 0) {
                    harpoonList.add(new Harpoon(player, -harpoonLauncher.getActuatorX(), -harpoonLauncher.getActuatorY(), harpoonStrength));

                    oxygenManager.depleteOxygen(1);
                }

                harpoonLauncher.setIsPressed(false);
                harpoonLauncher.resetActuator();
                return true;


        }

        return super.onTouchEvent(event);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // Draw background
        if (backgroundBitmap != null) {
            canvas.drawBitmap(backgroundBitmap, 0, 0, null);
        }

        // Draw merlion
        if (merlionBitmap != null) {
            Paint paint = new Paint();
            paint.setAlpha(Constants.MERLION_ALPHA);
            canvas.drawBitmap(merlionBitmap, Constants.MERLION_X, Constants.MERLION_Y, paint);
        }

        if (drawUPSText) {
            drawUPS(canvas);
        }
        if (drawFPSText) {
            drawFPS(canvas);
        }

        // Draw money bar
        moneyManager.draw(canvas, coinBitmap);

        // Draw oxygen bar
        oxygenManager.draw(canvas);

        // Draw harpoons launched
        for (Harpoon harpoon : harpoonList) {
            harpoon.draw(canvas);
        }

        // Draw harpoon joystick
        harpoonLauncher.draw(canvas);

        // Draw diver
        if (lottieDrawable != null) {
            int animationWidth = 600;
            int animationHeight = 600;
            int startX = 0;
            int startY = 0;

            // Set the bounds for the lottieDrawable
            lottieDrawable.setBounds(startX, startY, startX + animationWidth, startY + animationHeight + 200);

            // Save the current state of the canvas
            int saveCount = canvas.save();

            // Flip the animation horizontally
            canvas.scale(-1f, 1f, startX + (animationWidth / 2f), startY + (animationHeight / 2f));

            // Draw the diver
            lottieDrawable.draw(canvas);

            // Restore the canvas to its previous state
            canvas.restoreToCount(saveCount);
        }


        // Draw fishes
        for (Fish fish : fishes) {
            fish.draw(canvas);
        }

        // Draw bubbles
        for (Bubble bubble : bubbles) {
            bubble.draw(canvas);
        }

        // Check game over
        if (isGameOver) {
            // Save money
            saveMoneyState();

            if (gameOverStartTime == -1) {
                gameOverStartTime = System.currentTimeMillis();
            } else {
                if (System.currentTimeMillis() - gameOverStartTime <= Constants.GAMEOVER_DURATION * 1000) {
                    Paint p = new Paint();
                    p.setTypeface(chikiBubblesFont);
                    p.setTextSize(Constants.GAMEOVER_TEXT_SIZE);
                    p.setColor(Constants.GAMEOVER_TEXT_COLOR);

                    // Render game over text
                    drawCenterText(canvas, p, Constants.GAMEOVER_TEXT);
                } else {
                    activity.showEndScreen(fishesCaught, moneyManager.getMoney());
                }
            }
        }
    }


    public void drawUPS(Canvas canvas) {
        String averageUPS = Integer.toString((int) gameThread.getAverageUPS());
        Paint paint = new Paint();
        paint.setColor(Constants.UPS_FPS_COLOR);
        paint.setTextSize(Constants.UPS_FPS_TEXT_SIZE);
        canvas.drawText("UPS: " + averageUPS, 100, 80, paint);
    }

    public void drawFPS(Canvas canvas) {
        String averageUPS = Integer.toString((int) gameThread.getAverageFPS());
        Paint paint = new Paint();
        paint.setColor(Constants.UPS_FPS_COLOR);
        paint.setTextSize(Constants.UPS_FPS_TEXT_SIZE);
        canvas.drawText("FPS: " + averageUPS, 100, 150, paint);
    }

    long lastUpdate = System.currentTimeMillis();

    public void update() {
        float deltaTime = System.currentTimeMillis() - lastUpdate;
        deltaTime /= 1000f;
        lastUpdate = System.currentTimeMillis();
        // Update oxygen, check game over
        oxygenManager.update();
        isGameOver = oxygenManager.getGameOver();

        // Update harpoon game state
        harpoonLauncher.update();

        // Update fish movement
        for (Fish fish : fishes) {
            fish.move(deltaTime);

            // Check for collision with harpoon
            // add fish to harpoon if collided at sufficient speed
            if (!fish.isCaught()) {
                for (Harpoon harpoon : harpoonList) {
                    if (!harpoon.isRetracting() && harpoon.hasCollided(fish.getRect()) && harpoon.getSpeed() > Constants.CATCH_SPEED) {
                        fish.caught(harpoon);
                        harpoon.addFish(fish);
                    }
                }
            }
        }

        // Iterate through fishlist and harpoonlist to check for collisions
        Iterator<Harpoon> iteratorHarpoon = harpoonList.iterator();
        while (iteratorHarpoon.hasNext()) {
            Harpoon harpoon = iteratorHarpoon.next();
            harpoon.update(deltaTime);

            if (harpoon.isRetracting()) {
                // Handle when harpoon retracting
                // Obtain money for each fish caught by harpoon and spawn new fish
                // Then remove the harpoon
                if (GameObject.getDistanceBetweenGameObjects(harpoon, player) <= 100) {
                    for (Fish fish : harpoon.getFishList()) {
                        fishes.remove(fish);
                        fishesCaught++;
                        spawnFish();

                        // Add money here
                        moneyManager.addMoney(10);
                    }
                    iteratorHarpoon.remove();
                }
            }
        }
    }

    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        Rect r = new Rect();
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }


    public void resume() {
        isPaused = false;
    }

    public void pause() {
        isPaused = true;
    }

    public void saveMoneyState() {
        moneyManager.saveMoney(getContext());
    }

    //helper function to spawn new fish
    public void spawnFish() {
        switch (fishId % 3) {
            case 0:
                fishes.add(new Fish(context, fishSpriteSheet.getRedFishSprite()));
                break;
            case 1:
                fishes.add(new Fish(context, fishSpriteSheet.getYellowFishSprite()));
                break;
            case 2:
                fishes.add(new Fish(context, fishSpriteSheet.getGreenFishSprite()));
                break;
        }
        fishId++;
    }

    public void stop() {
        isStopped = true;
        gameThread.stopLoop();
    }

    // Method that randomly picks a bubble to move
    // synchronised on a mutex object so a bubble
    // is only updated one thread at a time
    private void bubbleMove() {
        Random dice = new Random();
        while (true) {
            if (!isPaused) {
                try {
                    sleep(dice.nextInt(10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int index = dice.nextInt(bubbles.size());
                synchronized (mutex) {
                    bubbles.get(index).move();
                }
            }
        }
    }
}
