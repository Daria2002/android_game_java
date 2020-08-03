package suza.project.crazyballs.model.properties;

import android.util.Log;

import suza.project.crazyballs.model.components.AbstractFigure;
import suza.project.crazyballs.model.figures.BasketFigure;

/**
 * This class is used for resolving collisions between objects.
 *
 * Created by lmark on 13/09/2017.
 */

public class Collision {

    /**
     * Spring collision coefficient.
     */
    public static double spring = 0.9;

    public static double TOP_FACTOR = 1.5;
    public static double BOTTOM_FACTOR = 1.5;
    public static double LEFT_FACTOR = 1.5;
    public static double RIGHT_FACTOR = 1.5;

    public final static int TOP_COLLISION = 1;
    public final static int LEFT_COLLISION = 2;
    public final static int RIGHT_COLLISION = 3;
    public final static int BOTTOM_COLLISION = 4;

    public final static int COLLISION_OFFSET = 10;

    public static final String TAG = Collision.class.getSimpleName();

    /**
     * Resolve collision between figure and all walls.
     * Assume figures have round hitboxes.
     *
     * @param screenWidth Screen width.
     * @param screenHeight Screen height.
     * @param figure Figure reference.
     * @return Wall in collision.
     */
    public static int resolveWallCollision(int screenWidth, int screenHeight, AbstractFigure figure) {
        // check resolveCollision with right wall if heading right
        if (figure.getSpeed().getxDirection() == MySpeed.DIRECTION_RIGHT
                && figure.getX() + figure.getRadius() >= screenWidth) {
            figure.getSpeed().toggleXDirection();
            figure.getSpeed().div(RIGHT_FACTOR);
            return RIGHT_COLLISION;
        }
        // check resolveCollision with left wall if heading left
        if (figure.getSpeed().getxDirection() == MySpeed.DIRECTION_LEFT
                && figure.getX() - figure.getRadius()  <= 0) {
            figure.getSpeed().toggleXDirection();
            figure.getSpeed().div(LEFT_FACTOR);
            return LEFT_COLLISION;
        }
        // check resolveCollision with bottom wall if heading down
        if (figure.getSpeed().getyDirection() == MySpeed.DIRECTION_DOWN
                &&  figure.getY() +  figure.getRadius() >= screenHeight) {
            figure.getSpeed().toggleYDirection();
            figure.getSpeed().div(BOTTOM_FACTOR);
            return BOTTOM_COLLISION;
        }
        // check resolveCollision with top wall if heading up
        if (figure.getSpeed().getyDirection() == MySpeed.DIRECTION_UP
                && figure.getY() -  figure.getRadius() <= 0) {
            figure.getSpeed().toggleYDirection();
            figure.getSpeed().div(TOP_FACTOR);
            return TOP_COLLISION;
        }

        return -1;
    }

    public static int resolveSideWallCollision(int screenWidth, int screenHeight, AbstractFigure figure) {

        // Resolve BALL collisions
        if (figure.getType() != FigureType.STATIC_BALL) {
            // check resolveCollision with right wall if heading right
            if (figure.getSpeed().getxDirection() == MySpeed.DIRECTION_RIGHT
                    && figure.getX() + figure.getRadius() >= screenWidth) {
                figure.getSpeed().toggleXDirection();
                figure.getSpeed().div(RIGHT_FACTOR);
                return RIGHT_COLLISION;
            }
            // check resolveCollision with left wall if heading left
            if (figure.getSpeed().getxDirection() == MySpeed.DIRECTION_LEFT
                    && figure.getX() - figure.getRadius() <= 0) {
                figure.getSpeed().toggleXDirection();
                figure.getSpeed().div(LEFT_FACTOR);
                return LEFT_COLLISION;
            }
        }

        return -1;
    }

    /**
     * Resolve collision between 2 figures.
     * Assume both figures are round.
     *
     * @param fig1 Figure 1 reference.
     * @param fig2 Figure 2 reference.
     * @return True if collision ocurred otherwise false.
     */
    public static boolean resolveFigureCollision(AbstractFigure fig1, AbstractFigure fig2) {
        float dx = fig2.getX() - fig1.getX();
        float dy = fig2.getY() - fig1.getY();

        // Distance between the balls
        double distance = Math.sqrt(dx*dx + dy*dy);

        // Minimal distance to cause resolveCollision
        float minDist = fig2.getRadius() + fig1.getRadius();

        // Check if there is a resolveCollision
        if (distance >= minDist) {
            return false;
        }

        // If both figures have recently collided do not resolve collision
        if (fig1.getState() == FigureState.COLLISION &&
                fig2.getState() == FigureState.COLLISION) {
            return false;
        }

        // Resolve collision
        double angle = Math.atan2(dy, dx);
        angle = angle > 0 ? angle : angle + 2 * Math.PI;
        double targetX = fig1.getX() + Math.cos(angle) * minDist;
        double targetY = fig1.getY() + Math.sin(angle) * minDist;

        double ax = (targetX - fig2.getX()) * spring;
        double ay = (targetY - fig2.getY()) * spring;

        fig1.getSpeed().reduceSpeed(ax, ay);
        fig2.getSpeed().increaseSpeed(ax, ay);

        fig1.setState(FigureState.COLLISION);
        fig2.setState(FigureState.COLLISION);

        return true;
    }

    /**
     * Resolve ball figure collisions.
     *
     * @param fig1 Collision figure 1.
     * @param fig2 Collision figure 2.
     * @return True if collision was resolved, otherwise false.
     */
    public static boolean resolveFigureCollision2(AbstractFigure fig1, AbstractFigure fig2) {
        int xDist = fig1.getX() - fig2.getX();
        int yDist = fig1.getY() - fig2.getY();

        // If both figures have recently collided do not resolve collision
        if (fig1.getState() == FigureState.COLLISION &&
                fig2.getState() == FigureState.COLLISION) {
            return false;
        }

        double distSquared = xDist*xDist + yDist*yDist;
        //Check the squared distances instead of the the distances,
        // same result, but avoids a square root.
        if(distSquared <= Math.pow((fig1.getRadius() + fig2.getRadius()),2)) {
            double xVelocity = fig2.getSpeed().getX() - fig1.getSpeed().getX();
            double yVelocity = fig2.getSpeed().getY() - fig1.getSpeed().getY();
            double dotProduct = xDist*xVelocity + yDist*yVelocity;

            //Neat vector maths, used for checking if the objects moves towards one another.
            if (dotProduct > 0) {
                // Resolve collision between objects
                double collisionScale = dotProduct / distSquared;
                double xCollision = xDist * collisionScale;
                double yCollision = yDist * collisionScale;
                //The Collision vector is the speed difference projected on the Dist vector,
                //thus it is the component of the speed difference needed for the collision.
                double combinedMass = fig1.getMass() + fig2.getMass();
                double collisionWeightFig1 = 2 * fig2.getMass() / combinedMass;
                double collisionWeightFig2 = 2 * fig1.getMass() / combinedMass;

                fig1.getSpeed().increaseSpeed(
                    collisionWeightFig1 * xCollision, collisionWeightFig1 * yCollision);

                fig2.getSpeed().reduceSpeed(
                        collisionWeightFig2 * xCollision, collisionWeightFig2 * yCollision);

                // Set collision states
                fig1.setState(FigureState.COLLISION);
                fig2.setState(FigureState.COLLISION);

                return true;
            }
        }

        return false;
    }

    public static boolean resolveBasketBallCollision(AbstractFigure basket, AbstractFigure ball) {

        int ballRight = ball.getX() + ball.getRadius();
        int ballLeft = ball.getX() - ball.getRadius();

        //If ball is above basket level no collision happened
        if (ball.getY() + ball.getRadius() < basket.getY() - basket.getHeight()/2) {
            return false;
        }

        // If the ball is in the middle
        if (ballLeft >  basket.getX() - basket.getWidth()/2 + COLLISION_OFFSET / 2 &&
                ballRight < basket.getX() + basket.getWidth()/2 - COLLISION_OFFSET/2) {
            ((BasketFigure)basket).addFigure(ball);
        }

        // If basket does not contain the ball check for outside collision
        if (!((BasketFigure)basket).basketContains(ball)) {
            return resolveOutsideBasketBallCollision(basket, ball);
        } else {
            return resolveInsideBasketBallCollision(basket, ball);
        }

    }

    /**
     *
     * @param basket Basket Reference.
     * @param ball Ball reference.
     * @return True if the event was resolved, otherwise false.
     */
    public static boolean resolveOutsideBasketBallCollision(AbstractFigure basket, AbstractFigure ball) {
        int ballRight = ball.getX() + ball.getRadius();
        int ballLeft = ball.getX() - ball.getRadius();

        if (ball.getY() + ball.getRadius() < basket.getY() - basket.getHeight()/2) {
            return true;
        }

        // Outside Left collision
        if (ballRight >=  basket.getX() - basket.getWidth()/2 - COLLISION_OFFSET && // Collision with the basket
                ball.getX() < basket.getX() // Ball on the left of the basket
                ) {

            ball.getSpeed().setXDirection(MySpeed.DIRECTION_LEFT);
            ball.getSpeed().increaseSpeed(basket.getSpeed().getX(), 0);
            ball.setX(ball.getX() -  2);
            //Log.d(TAG, "Ball " + ball.getID() + " outside left collision with basket.");

            return true;
        }

        // Outside Right collision
        if (ballLeft <= basket.getX() + basket.getWidth()/2 + COLLISION_OFFSET && // Collision with the basket
                ball.getX() > basket.getX() // Ball is on the right side of the basket
                 ) {

            ball.getSpeed().setXDirection(MySpeed.DIRECTION_RIGHT);
            ball.getSpeed().increaseSpeed(basket.getSpeed().getX(), 0);
            ball.setX(ball.getX() + 2);
            //Log.d(TAG, "Ball " + ball.getID() + " outside right collision with basket.");
            return true;
        }

        // If the ball is on the outside but there is no collision
        return ballLeft > basket.getX() + basket.getWidth() / 2 + COLLISION_OFFSET ||
                ballRight < basket.getX() - basket.getWidth() / 2 - COLLISION_OFFSET;

    }

    /**
     *
     * @param basket Basket Reference.
     * @param ball Ball reference.
     * @return True if the event was resolved, otherwise false.
     */
    public static boolean resolveInsideBasketBallCollision(AbstractFigure basket, AbstractFigure ball) {
        int ballRight = ball.getX() + ball.getRadius();
        int ballLeft = ball.getX() - ball.getRadius();

        // Inside left collision
        if (ballLeft <=  basket.getX() - basket.getWidth()/2 + COLLISION_OFFSET/2 &&
                ball.getSpeed().getxDirection() == MySpeed.DIRECTION_LEFT) {

            ball.getSpeed().toggleXDirection();
            ball.setX(ball.getX() + ball.getSpeed().getxDirection());

            if (ball.getSpeed().getAmplitude() > 2) {
                ball.getSpeed().mul(0.5);
            }
            //Log.d(TAG, "Ball " + ball.getID() + " inside left collision with basket.");

            return true;
        }

        // Inside right collision
        if (ballRight >= basket.getX() + basket.getWidth()/2 - COLLISION_OFFSET/2 &&
                ball.getSpeed().getxDirection() == MySpeed.DIRECTION_RIGHT) {

            ball.getSpeed().toggleXDirection();
            ball.setX(ball.getX() - ball.getSpeed().getxDirection());

            if (ball.getSpeed().getAmplitude() > 2) {
                ball.getSpeed().mul(0.5);
            }

            //Log.d(TAG, "Ball " + ball.getID() + " inside right collision with basket.");
            return true;
        }

        // Inside bottom  collision
        if (ball.getY() + ball.getRadius() >= basket.getY() + basket.getHeight()/2 - COLLISION_OFFSET &&
                ball.getSpeed().getyDirection() == MySpeed.DIRECTION_DOWN)  {

            ball.getSpeed().toggleYDirection();
            if (ball.getSpeed().getAmplitude() > 2) {
                ball.getSpeed().mul(0.5);
            }

            ball.setY(ball.getY() - 2);
            //Log.d(TAG, "Ball " + ball.getID() + " inside bottom collision with basket.");
            return true;
        }

        // No collision happened
        return false;
    }

}
