package suza.project.crazyballs.model.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by lmark on 06/08/2017.
 */

public class MyParticle {
    public static final int STATE_ALIVE = 0;	// particle is alive
    public static final int STATE_DEAD = 1;		// particle is dead

    public static final int DEFAULT_LIFETIME 	= 200;	// play with this
    public static final int MAX_DIMENSION		= 5;	// the maximum width or height
    public static final int MAX_SPEED			= 10;	// maximum speed (per update)

    private int state;			// particle is alive or dead
    private float width;		// width of the particle
    private float height;		// height of the particle
    private float x, y;			// horizontal and vertical position
    private double xv, yv;		// vertical and horizontal velocity
    private int age;			// current age of the particle
    private int lifetime;		// particle dies when it reaches this value
    private int color;			// the color of the particle
    private Paint paint;		// internal use to avoid instantiation

    private Random r = new Random();

    public MyParticle(float x, float y) {
        this.x = x;
        this.y = y;
        this.state = MyParticle.STATE_ALIVE;
        this.width = r.nextInt(MAX_DIMENSION - 1 + 1);
        this.height = this.width;
        this.lifetime = DEFAULT_LIFETIME;
        this.age = 0;
        this.xv = (r.nextInt(MAX_SPEED * 2 + 1) - MAX_SPEED);
        this.yv = (r.nextInt(MAX_SPEED * 2 + 1) - MAX_SPEED);
        // smoothing out the diagonal speed
        if (xv * xv + yv * yv > MAX_SPEED * MAX_SPEED) {
            xv *= 0.7;
            yv *= 0.7;
        }
        this.color = Color.argb(255, r.nextInt(255+1), r.nextInt(255 + 1), r.nextInt(255 + 1));
        this.paint = new Paint(this.color);
    }

    public void update() {
        if (this.state != STATE_DEAD) {
            this.x += this.xv;
            this.y += this.yv;

            // extract alpha
            int a = this.color >>> 24;
            a -= 2; // fade by 2
            if (a <= 0) { // if reached transparency kill the particle
                this.state = STATE_DEAD;
            } else {
                this.color = (this.color & 0x00ffffff) + (a << 24);		// set the new alpha
                this.paint.setAlpha(a);
                this.age++; // increase the age of the particle
            }
            if (this.age >= this.lifetime) {	// reached the end if its life
                this.state = STATE_DEAD;
            }
        }
    }

    public void draw(Canvas canvas) {
        paint.setColor(this.color);
        canvas.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, paint);
    }

    public boolean isAlive() {
        return state == STATE_ALIVE;
    }
}
