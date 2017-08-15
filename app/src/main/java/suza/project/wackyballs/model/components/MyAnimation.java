package suza.project.wackyballs.model.components;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * This class extends MyFigure and adds animation functionality to it.
 *
 * Created by lmark on 06/08/2017.
 */

public abstract class MyAnimation extends MyFigure {
    private static final String TAG = MyAnimation.class.getSimpleName();

    /**
     * The rectangle to be drawn from the animation bitmap.
     */
    private Rect sourceRect;
    /**
     * Total number of frames in the animation.
     */
    private int frameNr;
    /**
     * Current frame drawn in the animation cycle.
     */
    private int currentFrame;
    /**
     * Time when the last frame of the animation was drawn.
     */
    private long frameTicker;
    /**
     * Time each frame of the animation persists on screen. (miliseconds)
     */
    private int framePeriod;

    /**
     * Width of the sprite(single animation frame) - used to cut the right size
     * frame from the bitmap containing all frames
     */
    private int spriteWidth;
    /**
     * Height of the sprite
     */
    private int spriteHeight;	// the height of the sprite

    public MyAnimation(Bitmap animatedBitmap, int x, int y, int fps, int frameCount) {
        super(animatedBitmap, x, y);
        currentFrame = 0;
        frameNr = frameCount;
        spriteWidth = animatedBitmap.getWidth() / frameCount;
        spriteHeight = animatedBitmap.getHeight();
        sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);
        framePeriod = 1000 / fps;
        frameTicker = 0l;
    }

    /**
     * This method is used to update the animation cycle.
     */
    public void update() {
        super.update();
        long gameTime = System.currentTimeMillis();
        if (gameTime > frameTicker + framePeriod) {
            frameTicker = gameTime;
            // increment the frame
            currentFrame++;
            if (currentFrame >= frameNr) {
                currentFrame = 0;
            }
        }
        // define the rectangle to cut out sprite
        this.sourceRect.left = currentFrame * spriteWidth;
        this.sourceRect.right = this.sourceRect.left + spriteWidth;
    }

    /**
     * Draws the frame defined by the sourceRectangle from the animation bitmap.
     *
     * @param canvas Reference to Canvas object, where the image will be drawn.
     */
    @Override
    public void draw(Canvas canvas) {
        // where to draw the sprite
        Rect destRect = new Rect(
                getX() - spriteWidth/2, getY() - spriteHeight/2,
                getX() + spriteWidth/2, getY() + spriteHeight/2);
        canvas.drawBitmap(super.getBitmap(), sourceRect, destRect, null);
    }

    /**
     * @return Returns bitmap width
     */
    @Override
    public int getWidth() {
        return spriteWidth;
    }

    /**
     * @return Returns bitmab height
     */
    @Override
    public int getHeight() {
        return spriteHeight;
    }
}
