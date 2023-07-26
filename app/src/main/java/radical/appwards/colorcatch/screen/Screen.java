package radical.appwards.colorcatch.screen;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

public interface Screen {
	void myDraw(Context context, Canvas canvas);
	void updatePhysics(Context context);
	void eventAction(MotionEvent event);
}
