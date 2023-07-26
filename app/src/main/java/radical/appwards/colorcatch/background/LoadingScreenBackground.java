package radical.appwards.colorcatch.background;

import radical.appwards.colorcatch.variables.GameVariables;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * A class to create the loading radical.appwards.colorcatch.screen radical.appwards.colorcatch.background. I use the radical.appwards.colorcatch.game radical.appwards.colorcatch.variables to
 * get the radical.appwards.colorcatch.screen size.
 * 
 * @author Rick
 * 
 */

public class LoadingScreenBackground {

	private GameVariables gv;

	public LoadingScreenBackground() {
		gv = GameVariables.getInstance();
	}

	public void draw(Canvas canvas) {
		canvas.drawColor(Color.RED);

		canvas.drawText("Color", gv.screenW / 2, gv.screenH / 3 + 5,
				gv.getYellowBoldPaint());

		canvas.drawText("Catch", gv.screenW / 2,
				gv.screenH - gv.screenH / 4 - 5,
				gv.getYellowBoldPaint());
	}
}
