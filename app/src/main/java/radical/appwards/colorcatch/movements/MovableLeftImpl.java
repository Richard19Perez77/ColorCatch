package radical.appwards.colorcatch.movements;

import java.io.Serializable;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * A class that defines an objest that is supposed to be moving left.
 * 
 * @author Rick
 * 
 */

public class MovableLeftImpl implements Movable, Serializable {

	private static final long serialVersionUID = 1L;
	private int speed;
	/**
	 * Direction may change if moving off radical.appwards.colorcatch.screen.
	 */
	private int direction = 1;
	/**
	 * Used for collision detection.
	 */
	private Rect rect;

	public MovableLeftImpl() {
		rect = new Rect();
	}

	public MovableLeftImpl(int s, int left, int top, int right, int bottom) {
		speed = s;
		rect = new Rect(left, top, right, bottom);
	}

	@Override
	public void move() {
		rect.left -= speed * direction;
		rect.right -= speed * direction;
	}

	@Override
	public Rect getRect() {
		return rect;
	}

	@Override
	public void destroy(int left, int top, int right, int bottom, int speed) {
		rect.left = left;
		rect.top = top;
		rect.right = right;
		rect.bottom = bottom;
		this.speed = speed;
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
		direction *= -1;

	}

	@Override
	public void resetDirection() {
		direction = 1;
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