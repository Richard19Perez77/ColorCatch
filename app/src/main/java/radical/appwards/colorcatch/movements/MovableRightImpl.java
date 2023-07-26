package radical.appwards.colorcatch.movements;

import java.io.Serializable;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * A class that defines a right moving object.
 * 
 * @author Rick
 * 
 */

public class MovableRightImpl implements Movable, Serializable {

	private static final long serialVersionUID = -5375384903284193615L;
	private int speed;
	/**
	 * Used for collision detection.
	 */
	private Rect rect;
	/**
	 * Direction will change if the object is about to move rigth off radical.appwards.colorcatch.screen.
	 */
	private int direction = 1;

	public MovableRightImpl() {
		rect = new Rect();
	}

	public MovableRightImpl(int left, int top, int right, int bottom, int speed) {
		rect = new Rect(left, top, right, bottom);
		this.speed = speed;
	}

	@Override
	public void move() {
		rect.left += speed * direction;
		rect.right += speed * direction;
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