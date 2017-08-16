package suza.project.wackyballs.util;


import java.util.Random;

import suza.project.wackyballs.model.MyExplosion;

/**
 * Created by lmark on 15/08/2017.
 */

public final class Util {

    private static final Random random = new Random();

    public static int randomInteger(int aStart, int aEnd){
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long)aEnd - (long)aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long)(range * random.nextDouble());
        int randomNumber =  (int)(fraction + aStart);

        return randomNumber;
    }

}
