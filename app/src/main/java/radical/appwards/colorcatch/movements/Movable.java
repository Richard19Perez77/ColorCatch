package radical.appwards.colorcatch.movements;

import android.graphics.Point;
import android.graphics.Rect;

public interface Movable {
	
	void move();

	Rect getRect();

	void setRect(Rect rect);

	void destroy(int right, int top, int left, int bottom, int speed);

	void setSpeed(int speed);

	int getSpeed();

	void incSpeed();

	void changeLateralDirection();

	void resetDirection();

	void setNewSideLocation(int left, int top, int right, int bottom);

	void plotPoints(Point a, Point b);
}
