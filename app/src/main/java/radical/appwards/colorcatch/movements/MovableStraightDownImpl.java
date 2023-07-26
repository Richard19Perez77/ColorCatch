package radical.appwards.colorcatch.movements;

import java.io.Serializable;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * A class to define a straight down movement.
 * 
 * @author Rick
 * 
 */

public class MovableStraightDownImpl implements Movable, Serializable {

	private static final long serialVersionUID = -5619081840523584297L;
	private int speed;
	/**
	 * Used in collision detection and offscreen radical.appwards.colorcatch.logic.
	 */
	private Rect rect;

	public MovableStraightDownImpl() {
	}

	public MovableStraightDownImpl(int left, int top, int right, int bottom,
			int s) {
		speed = s;
		rect = new Rect(left, top, right, bottom);
	}

	@Override
	public void move() {
		rect.top += speed;
		rect.bottom += speed;
	}

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
		//not needed in straight down implementation
	}

	@Override
	public void resetDirection() {
		//not needed in straight down implementation
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
