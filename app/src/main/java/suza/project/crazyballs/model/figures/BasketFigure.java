package suza.project.crazyballs.model.figures;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import suza.project.crazyballs.GameActivity;
import suza.project.crazyballs.R;
import suza.project.crazyballs.ResultActivity;
import suza.project.crazyballs.game.GamePanel;
import suza.project.crazyballs.model.components.AbstractFigure;
import suza.project.crazyballs.model.containers.BasketBallContainer;
import suza.project.crazyballs.model.properties.Collision;
import suza.project.crazyballs.model.properties.FigureState;
import suza.project.crazyballs.model.properties.FigureType;
import suza.project.crazyballs.model.properties.MySpeed;
import suza.project.crazyballs.state.LevelConfig;

/**
 * Figure representing a basket in basket ball game.
 *
 * Created by lmark on 13/09/2017.
 */

public class BasketFigure extends AbstractFigure {

    public static final String TAG = BasketFigure.class.getSimpleName();

    private static final int OFFSET = 20;

    private LevelConfig levelConfig;
    private GamePanel panel;
    private BasketBallContainer figureContainer;

    /**
     * List containing all figures found in the basket.
     */
    private List<AbstractFigure> basketList = new ArrayList<>();

    /**
     * Figure representing top left edge of the basket.
     */
    private StaticFigure leftEdge;

    /**
     * Figure representing top right edge of the basket.
     */
    private StaticFigure rightEdge;

    /**
     * Time difference used to give basket speed.
     */
    private long dt;
    private long tOld;

    public BasketFigure(GamePanel gamePanel, BasketBallContainer figureContainter, LevelConfig levelConfig) {
        super(BitmapFactory.decodeResource(gamePanel.getResources(), R.drawable.basket),
                0, 0);
        this.levelConfig = levelConfig;
        // Get navigation bar height in order to place the basket properly on screen
        Resources resources = gamePanel.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int navBarHeight = 0;
        if (resourceId > 0) {
            navBarHeight = resources.getDimensionPixelSize(resourceId);
        }
        navBarHeight = navBarHeight == 0 ? 350 : navBarHeight;

        // Set basket starting place
        setX(gamePanel.getScreenWidth() / 2);
        // setY(gamePanel.getScreenHeight() - navBarHeight - getBitmap().getHeight() / 2 - OFFSET);
        setY(gamePanel.getScreenHeight() - getBitmap().getHeight());

        this.panel = gamePanel;
        this.figureContainer = figureContainter;

        // Set basket figure type
        setState(FigureState.ALIVE);
        setType(FigureType.BASKET);

        // Define left edge
        leftEdge = new StaticFigure();
        leftEdge.setMass(30);
        leftEdge.setX(getX() - getWidth() / 2);
        leftEdge.setY(getY() - getHeight() / 2 - 5);
        leftEdge.setRadius(5);
        leftEdge.setID(-1);
        basketList.add(leftEdge);

        // Define right edge
        rightEdge = new StaticFigure();
        rightEdge.setMass(30);
        rightEdge.setX(getX() + getWidth() / 2);
        rightEdge.setY(getY() - getHeight() / 2 - 5);
        rightEdge.setID(-2);
        rightEdge.setRadius(5);
        basketList.add(rightEdge);

        setSpeed(new MySpeed(0, 0));
    }

    @Override
    public void resolveCollision(int screenWidth, int screenHeight, List<AbstractFigure> others) {
        //
    }

    @Override
    public void handleActionMove(int eventX, int eventY) {
        if (isTouched()) {
            double moveReductionFactor = 0.8;

            // If outside of range
            if (eventX > panel.getScreenWidth() - getWidth()/2 || eventX < getWidth()/2) {
                return;
            }

            for (AbstractFigure figure: figureContainer.getFigures()) {

                // If it's a basket continue
                if (figure.equals(this)) {
                    continue;
                }

                if (basketContains(figure)) {
                    // If figure is in the basket move it with the basket
                    figure.setX(figure.getX() + (int) ((eventX - getX()) * moveReductionFactor));
                } else {
                    synchronized (panel.getHolder()) {
                        // resolve outside basket wall collision
                        Collision.resolveOutsideBasketBallCollision(this, figure);

                        // resolve edge collisions
                        Collision.resolveFigureCollision2(getLeftEdge(), figure);
                        Collision.resolveFigureCollision2(getRightEdge(), figure);
                    }
                }
            }

            // Update basket
            setX(getX() + (int) ((eventX - getX()) * moveReductionFactor));

            int delta = (int) ((eventX - getX()) * moveReductionFactor);
            dt = System.currentTimeMillis() - tOld;
            tOld = System.currentTimeMillis();
            MySpeed newSpeed = new MySpeed(delta, 0, (double)dt/200);

            // Limit speed
            int maxSpeed = 10;
            if (newSpeed.getX() > maxSpeed) {
                newSpeed.setX(maxSpeed);
            }

            if (newSpeed.getX() < -maxSpeed) {
                newSpeed.setX(-maxSpeed);
            }

            setSpeed(newSpeed);
        }
    }

    @Override
    public void handleActionDown(int eventX, int eventY) {
        int x = getX();
        int y = getY();

        if (eventX >= x - getWidth()/2 && eventX <= x + getWidth()/2
            && eventY >= y-getHeight()/2 && eventY <= y+getHeight()/2) {
            setTouched(true);
        } else {
            setTouched(false);
        }

        if (isTouched()) {
            tOld = System.currentTimeMillis();
        }
    }

    @Override
    public void handleActionDoubleDown(int eventX, int eventY) {
        int x = getX();
        int y = getY();

        if (!(eventX >= x - getWidth()/2 && eventX <= x + getWidth()/2
                && eventY >= y-getHeight()/2 && eventY <= y+getHeight()/2)) {
            return;
        }

        // Kill and remove all items from basket
        int i = 0;
        int life = 0;
        int score = 0;
        while(i < basketList.size()) {
            // if life saver in the basket pause losing hearts for 10s
            if (basketList.get(i).getType() == FigureType.LIFE_SAVER) {
                panel.lifeSavingTime = System.currentTimeMillis() + 10 * 1000;
            }

            else if (basketList.get(i).getType() == FigureType.STATIC_BALL) {
                i++;
                continue;
            }

            else if (basketList.get(i).getType() == FigureType.LIFE_BALL) {
                life++;
            }

            else if(basketList.get(i).getType() == FigureType.STAR) {
                // remove all figures except bad balls from a screen and add scores
                // for all good balls and add one extra life for a heart figure
                for (AbstractFigure figure: figureContainer.getFigures()) {
                    // If it's a basket or in a basket continue
                    if (figure.equals(this) || basketContains(figure)) {
                        continue;
                    } else {
                        if (figure.getType() == FigureType.BALL) {
                            score += levelConfig.good_ball_score;
                        } else if (figure.getType() == FigureType.LIFE_BALL) {
                            life++;
                        }
                        if (figure.getType() != FigureType.BAD_BALL) {
                            figure.setState(FigureState.DEAD);
                        }
                    }
                }
            }

            else if (basketList.get(i).getType() == FigureType.BAD_BALL) {
                score += levelConfig.bad_ball_score;
            } else if (basketList.get(i).getType() == FigureType.BALL){
                score += levelConfig.good_ball_score;
            }

            basketList.get(i).setState(FigureState.DEAD);
            basketList.remove(i);
        }

        livesChanged(life);
        scoreChanged(score);
    }

    @Override
    public void handleActionUp(int eventX, int eventY) {
        if (isTouched()) {
            setTouched(false);
            getSpeed().setX(0);
            getSpeed().setY(0);
        }
    }

    /**
     * Add figure to the basket.
     * @param figure AbstractFigure reference.
     */
    public void addFigure(AbstractFigure figure) {
        if (!basketList.contains(figure)) {
            basketList.add(figure);
        }
    }

    /**
     * Check if basket contains given figure.
     *
     * @param figure AbstractFigure reference.
     * @return True if it contains figure, false otherwise.
     */
    public boolean basketContains(AbstractFigure figure) {
        return basketList.contains(figure);
    }

    /**
     * Update figures contained in the basket.
     */
    @Override
    public void update() {
        int i = 0;

        // Reset edge speed
        getLeftEdge().getSpeed().setX(0);
        getRightEdge().getSpeed().setY(0);

        while (i < basketList.size()) {
            AbstractFigure curr = basketList.get(i);

            // Do not update basket edges
            if (curr.getID() == getLeftEdge().getID()
                    || curr.getID() == getRightEdge().getID()) {
                i++;
                continue;
            }

            // If outside the basket remove from list
            if (curr.getY() + curr.getRadius()  <= getY() - getHeight()/2 ||
                    curr.getX() + curr.getRadius() <= getX() - getWidth()/2 ||
                    curr.getX() - curr.getRadius() >= getX() + getWidth()/2) {

                basketList.remove(i);
                continue;
            }

            if (!curr.isAlive()) {
                basketList.remove(i);
                continue;
            }

            i++;
        }
    }

    @Override
    public int getWidth() {
        return getBitmap().getWidth();
    }

    public StaticFigure getRightEdge() {
        return rightEdge;
    }

    public StaticFigure getLeftEdge() {
        return leftEdge;
    }
}
