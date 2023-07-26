package radical.appwards.colorcatch.screen;

import radical.appwards.colorcatch.background.LoadingScreenBackground;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * A class that defines the radical.appwards.colorcatch.logic at the loading radical.appwards.colorcatch.screen. Its a simple splash
 * radical.appwards.colorcatch.screen.
 * 
 * @author Rick
 * 
 */

public class LoadingScreenImpl implements Screen {

	private LoadingScreenBackground lbg;

	public LoadingScreenImpl() {
		lbg = new LoadingScreenBackground();
	}

	@Override
	public void myDraw(Context context, Canvas canvas) {
		lbg.draw(canvas);
	}

	@Override
	public void updatePhysics(Context context) {

	}

	@Override
	public void eventAction(MotionEvent event) {

	}
}
