package radical.appwards.colorcatch.objects;

import java.io.Serializable;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;

/**
 * A class to creates shields of sorts that show up when collission are detected
 * or the radical.appwards.colorcatch.level has been cleared or if a point has been scored. The object
 * increased in size and then dissapears.
 * 
 * @author Rick
 * 
 */

public class Shield implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean exists;
	/**
	 * Used to tell the stopping point of a growing shield. Determines its end
	 * size.
	 */
	int shieldCount;
	int x, y, x2, y2;
	private Paint paint;

	public Shield() {
		paint = new Paint();
		exists = false;
		shieldCount = 0;
	}

	public void start(int x, int y, int x2, int y2) {
		this.x = x;
		this.y = y;
		this.x2 = x2;
		this.y2 = y2;
		exists = true;
		shieldCount = 0;
	}

	public void start(int x, int y, int x2, int y2, Paint p) {
		this.x = x;
		this.y = y;
		this.x2 = x2;
		this.y2 = y2;
		exists = true;
		shieldCount = 0;
		paint.setColor(p.getColor());
		paint.setStyle(Style.FILL);
	}

	// the shield object has to be sent since the player moves so does the
	// shield
	public void update(Canvas canvas, int jx1, int jy1, int jx2, int jy2,
			Paint paint) {

		canvas.drawRect(new RectF(jx1 - shieldCount, jy1 - shieldCount, jx2
				+ shieldCount, jy2 + shieldCount), paint);
		shieldCount += 3;
		if (shieldCount >= 30) {
			exists = false;
		}
	}

	// the shield object has to be sent since the player moves so does the
	// shield
	public void update(Canvas canvas) {
		canvas.drawRect(new RectF(x - shieldCount, y - shieldCount, x2
				+ shieldCount, y2 + shieldCount), paint);
		shieldCount += 3;
		if (shieldCount >= 30) {
			exists = false;
		}
	}

	public boolean getExists() {
		return exists;
	}
}
