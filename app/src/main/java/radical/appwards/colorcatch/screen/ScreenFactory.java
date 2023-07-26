package radical.appwards.colorcatch.screen;

import android.content.Context;

/**
 * A class to create the differetn screens the app might use such as loading,
 * menu and radical.appwards.colorcatch.game.
 * 
 * @author Rick
 * 
 */

public class ScreenFactory {

	private Screen screen;

	public Screen createScreen(Context context, String type) {
		if (type.equals("loading")) {
			screen = new LoadingScreenImpl();
		} else if (type.equals("menu")) {
			screen = new MenuScreenImpl();
		} else if (type.equals("radical/appwards/colorcatch/game")) {
			screen = new GameScreenImpl(context);
		}

		return screen;
	}
}
