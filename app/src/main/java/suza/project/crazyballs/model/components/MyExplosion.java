package suza.project.crazyballs.model.components;

import android.graphics.Canvas;
import android.util.Log;


/**
 * Created by lmark on 07/08/2017.
 */

public class MyExplosion {
    public static final int STATE_ALIVE 	= 0;	// at least 1 particle is alive
    public static final int STATE_DEAD 		= 1;	// all particles are dead

    private MyParticle[] particles;			// particles in the explosion
    private float x, y;						// the explosion's origin
    private int size;						// number of particles
    private int state;						// whether it's still active or not

    public static final String TAG = MyExplosion.class.getSimpleName();

    public MyExplosion(int particleNr, float x, float y) {
        Log.d(TAG, "Explosion created at " + x + "," + y);
        this.state = STATE_ALIVE;
        this.particles = new MyParticle[particleNr];
        for (int i = 0; i < this.particles.length; i++) {
            MyParticle p = new MyParticle(x, y);
            this.particles[i] = p;
        }
        this.size = particleNr;
    }

    public void update() {
        boolean alive = false;
        for (MyParticle particle:particles) {
            particle.update();
            if (particle.isAlive()) {
                alive = true;
            }
        }

        if (!alive) {
            state = STATE_DEAD;
        }
    }

    public void draw(Canvas canvas) {
        for (MyParticle particle:particles) {
            particle.draw(canvas);
        }
    }

    public boolean isAlive() {
        return state == STATE_ALIVE;
    }

    public boolean isDead() {
        return state == STATE_DEAD;
    }
}
