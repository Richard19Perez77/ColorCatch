package radical.appwards.colorcatch.movements;

import java.io.Serializable;

import radical.appwards.colorcatch.variables.GameVariables;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * A class to define straigh up movement on radical.appwards.colorcatch.screen.
 *
 * @author Rick
 */

public class MovablePathImpl implements Movable, Serializable {

    private static final long serialVersionUID = -5375097657157569993L;
    private int speed = 0;
    private Point[] points;
    private PlotPoints plot;
    private GameVariables gv;
    public int position;

    /**
     * Used in collision detection and off radical.appwards.colorcatch.screen notification.
     */

    private Rect rect;

    public MovablePathImpl() {
        rect = new Rect();
        plot = new PlotPoints();
        gv = GameVariables.getInstance();
    }

    @Override
    public void move() {
        Point point = points[position];

        rect.top = point.y;
        rect.bottom = point.y + gv.getEnemyHeight();
        rect.left = point.x;
        rect.right = point.x + gv.getEnemyWidth();

        position++;
        position += speed;

        if (position > points.length - 1) {
            position = 0;
        }
    }

    @Override
    public Rect getRect() {
        return rect;
    }

    @Override
    public void destroy(int left, int top, int right, int bottom, int s) {
        rect.left = left;
        rect.top = top;
        rect.right = right;
        rect.bottom = bottom;
    }

    @Override
    public void setSpeed(int s) {
        speed = s;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public void incSpeed() {
        speed++;
    }

    @Override
    public void setRect(Rect rect) {
        this.rect = rect;
    }

    @Override
    public void changeLateralDirection() {
        // not used in straight up implementation
    }

    @Override
    public void resetDirection() {
        // not used in straight up implementation
    }

    @Override
    public void setNewSideLocation(int left, int top, int right, int bottom) {
        rect.left = left;
        rect.top = top;
        rect.right = right;
        rect.bottom = bottom;
    }

    @Override
    public void plotPoints(Point a, Point b) {
        points = plot.plotLine(a, b);
    }
}