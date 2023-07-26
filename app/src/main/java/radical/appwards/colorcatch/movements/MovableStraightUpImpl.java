package radical.appwards.colorcatch.movements;

import java.io.Serializable;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * A class to define straigh up movement on radical.appwards.colorcatch.screen.
 * 
 * @author Rick
 * 
 */

public class MovableStraightUpImpl implements Movable, Serializable {

	private static final long serialVersionUID = -5375097657157569993L;
	private int speed;
	/**
	 * Used in collision detection and off radical.appwards.colorcatch.screen notification.
	 */

	private Rect rect;

	public MovableStraightUpImpl(int s) {
		rect = new Rect();
		speed = s;
	}

	@Override
	public void move() {
		rect.top -= speed;
		rect.bottom -= speed;
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
		speed = s;
	}

	@Override
	public void setSpeed(int speed) {
		this.speed = speed;
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
	public void setNewSideLocation(int left, int top, int right, int bottom)  {
		rect.left = left;
		rect.top = top;
		rect.right = right;
		rect.bottom = bottom;
	}

	@Override
	public void plotPoints(Point a, Point b) {

	}
}