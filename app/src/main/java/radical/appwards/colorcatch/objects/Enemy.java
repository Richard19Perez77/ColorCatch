package radical.appwards.colorcatch.objects;

import java.io.Serializable;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import radical.appwards.colorcatch.movements.Movable;
import radical.appwards.colorcatch.movements.MovableStraightDownImpl;

/**
 * A class used for the enemy radical.appwards.colorcatch.objects, includes movement and paint.
 * 
 * @author Rick
 * 
 */

public class Enemy implements Movable, Serializable {

	private static final long serialVersionUID = -1145003285892485667L;
	private boolean exist;
	private Movable movable;
	private Paint paint;

	// private boolean shapingRight;
	public Enemy(int left, int top, int right, int bottom, int speed) {
		setExist(false);
		movable = new MovableStraightDownImpl(left, top, right, bottom, speed);
	}

	public void draw(Canvas canvas) {
		canvas.drawRect(movable.getRect(), paint);
	}

	@Override
	public void destroy(int left, int top, int right, int bottom, int speed) {
		movable.destroy(left, top, right, bottom, speed);
		setExist(false);
	}

	public void setMovable(Movable m) {
		m.setRect(movable.getRect());
		m.setSpeed(movable.getSpeed());
		m.resetDirection();
		movable = m;
	}

	public Movable getMovable() {
		return movable;
	}

	@Override
	public void move() {
		movable.move();
	}

	@Override
	public Rect getRect() {
		return movable.getRect();
	}

	@Override
	public void setSpeed(int speed) {
		movable.setSpeed(speed);
	}

	@Override
	public int getSpeed() {
		return movable.getSpeed();
	}

	@Override
	public void incSpeed() {
		movable.incSpeed();
	}

	@Override
	public void setRect(Rect rect) {
		movable.setRect(rect);
	}

	@Override
	public void changeLateralDirection() {
		movable.changeLateralDirection();
	}

	@Override
	public void setNewSideLocation(int left, int top, int right, int bottom) {
		movable.setNewSideLocation(left, top, right, bottom);
	}

	public boolean getExist() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}

	@Override
	public void resetDirection() {
		movable.resetDirection();
	}

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint p) {
		paint = p;
	}

	@Override
	public void plotPoints(Point a, Point b) {
		movable.plotPoints(a, b);
	}

	// once used to reshape left to right like a mirror flipping slim to full
	// front and back

	// public void reShapeEnemy() {
	// if (shapingRight) {
	// x2--;
	// x1++;
	// if (x2 - x1 < w / 2)
	// shapingRight = false;
	// } else {
	// x1--;
	// x2++;
	// ;
	// if (x2 - x1 > w)
	// shapingRight = true;
	// }
	// }

}
