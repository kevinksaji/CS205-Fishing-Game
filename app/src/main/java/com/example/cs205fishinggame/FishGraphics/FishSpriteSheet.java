package com.example.cs205fishinggame.FishGraphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.example.cs205fishinggame.R;

public class FishSpriteSheet {
    private Bitmap bitmap;

    //how many pixels is the sprite (including empty spaces)
    private final int SPRITE_WIDTH = 150;
    private final int SPRITE_HEIGHT= 300;
    //width of empty space
    private final int WIDTH_OFFSET = 30;

    private final int HEIGHT_OFFSET = 115;
    private final int BOTTOM_HEIGHT_OFFSET = 115;

    private final int RED_OFFSET = 10;

    private final int GREEN_OFFSET = 10;
    public FishSpriteSheet(Context context) {
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.fish_spritesheet, bitmapOptions);
        //resize bitmap to 3 * original size, new size will be 900 x 600
        bitmap = getResizedBitmap(bitmap, bitmap.getWidth() * 3, bitmap.getHeight() * 3);
    }

    public FishSprite getRedFishSprite() {
        //add 300 to go up/down, add 150 to go left/right
        return new FishSprite(this, new Rect(WIDTH_OFFSET + RED_OFFSET, HEIGHT_OFFSET + RED_OFFSET, SPRITE_WIDTH - RED_OFFSET, SPRITE_HEIGHT - BOTTOM_HEIGHT_OFFSET - RED_OFFSET));
    }

    public FishSprite[] getRedFishSpriteArray() {
        //the edges of the first sprite
        int leftWidth = WIDTH_OFFSET + RED_OFFSET;
        int topHeight = HEIGHT_OFFSET + RED_OFFSET;
        int rightWidth = SPRITE_WIDTH - RED_OFFSET;
        int bottomHeight = SPRITE_HEIGHT - BOTTOM_HEIGHT_OFFSET - RED_OFFSET;

        FishSprite[] spriteArray = new FishSprite[4];

        //add SPRITE_WIDTH to the "left" and "right" parameter to go to the next sprite
        spriteArray[0] = new FishSprite(this, new Rect(leftWidth, topHeight, rightWidth, bottomHeight));
        spriteArray[1] = new FishSprite(this, new Rect(SPRITE_WIDTH + leftWidth, topHeight, SPRITE_WIDTH - rightWidth, bottomHeight));
        spriteArray[2] = new FishSprite(this, new Rect(2*SPRITE_WIDTH + leftWidth, topHeight, 2*SPRITE_WIDTH - rightWidth, bottomHeight));
        spriteArray[3] = new FishSprite(this, new Rect(3*SPRITE_WIDTH + leftWidth, topHeight, 3*SPRITE_WIDTH - rightWidth, bottomHeight));
        return spriteArray;
    }

    public FishSprite getYellowFishSprite() {
        //add 300 to go up/down, add 150 to go left/right
        return new FishSprite(this, new Rect(WIDTH_OFFSET,SPRITE_HEIGHT + HEIGHT_OFFSET, SPRITE_WIDTH - WIDTH_OFFSET,SPRITE_HEIGHT * 2 - BOTTOM_HEIGHT_OFFSET));
    }

    public FishSprite[] getYellowFishSpriteArray() {
        //the edges of the first sprite
        int leftWidth = WIDTH_OFFSET;
        int topHeight = SPRITE_HEIGHT + HEIGHT_OFFSET;
        int rightWidth = SPRITE_WIDTH - WIDTH_OFFSET;
        int bottomHeight = SPRITE_HEIGHT * 2 - BOTTOM_HEIGHT_OFFSET;

        FishSprite[] spriteArray = new FishSprite[4];

        //add SPRITE_WIDTH to the "left" and "right" parameter to go to the next sprite
        spriteArray[0] = new FishSprite(this, new Rect(leftWidth, topHeight, rightWidth, bottomHeight));
        spriteArray[1] = new FishSprite(this, new Rect(SPRITE_WIDTH + leftWidth, topHeight, SPRITE_WIDTH - rightWidth, bottomHeight));
        spriteArray[2] = new FishSprite(this, new Rect(2*SPRITE_WIDTH + leftWidth, topHeight, 2*SPRITE_WIDTH - rightWidth, bottomHeight));
        spriteArray[3] = new FishSprite(this, new Rect(3*SPRITE_WIDTH + leftWidth, topHeight, 3*SPRITE_WIDTH - rightWidth, bottomHeight));
        return spriteArray;
    }

    public FishSprite getGreenFishSprite() {
        //add 300 to go up/down, add 150 to go left/right
        return new FishSprite(this, new Rect(WIDTH_OFFSET + (GREEN_OFFSET * 3),SPRITE_HEIGHT * 2 + HEIGHT_OFFSET + GREEN_OFFSET, SPRITE_WIDTH - (GREEN_OFFSET * 3),SPRITE_HEIGHT * 3 - BOTTOM_HEIGHT_OFFSET - GREEN_OFFSET));
    }

    public FishSprite[] getGreenFishSpriteArray() {
        //the edges of the first sprite
        int leftWidth = WIDTH_OFFSET + (GREEN_OFFSET * 3);
        int topHeight = SPRITE_HEIGHT * 2 + HEIGHT_OFFSET + GREEN_OFFSET;
        int rightWidth = SPRITE_WIDTH - (GREEN_OFFSET * 3);
        int bottomHeight =SPRITE_HEIGHT * 3 - BOTTOM_HEIGHT_OFFSET - GREEN_OFFSET;

        FishSprite[] spriteArray = new FishSprite[4];

        //add SPRITE_WIDTH to the "left" and "right" parameter to go to the next sprite
        spriteArray[0] = new FishSprite(this, new Rect(leftWidth, topHeight, rightWidth, bottomHeight));
        spriteArray[1] = new FishSprite(this, new Rect(SPRITE_WIDTH + leftWidth, topHeight, SPRITE_WIDTH - rightWidth, bottomHeight));
        spriteArray[2] = new FishSprite(this, new Rect(2*SPRITE_WIDTH + leftWidth, topHeight, 2*SPRITE_WIDTH - rightWidth, bottomHeight));
        spriteArray[3] = new FishSprite(this, new Rect(3*SPRITE_WIDTH + leftWidth, topHeight, 3*SPRITE_WIDTH - rightWidth, bottomHeight));
        return spriteArray;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Bitmap getResizedBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create matrix for image transformation
        Matrix matrix = new Matrix();
        // resize bitmap using matrix
        matrix.postScale(scaleWidth, scaleHeight);
        //create the resized bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, width, height, matrix, false);
        //avoid memory leaks
        bitmap.recycle();
        return resizedBitmap;
    }

}
