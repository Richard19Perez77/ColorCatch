package radical.appwards.colorcatch.level;

import android.content.Context;
import android.graphics.Canvas;

public interface Level {
	
	void myDraw(Canvas canvas);

	void updatePhysics(Context context);
}
