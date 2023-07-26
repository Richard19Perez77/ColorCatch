package radical.appwards.colorcatch.game;

import radical.appwards.colorcatch.audio.Audio;
import radical.appwards.colorcatch.screen.Screen;
import radical.appwards.colorcatch.screen.ScreenFactory;
import radical.appwards.colorcatch.variables.GameVariables;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

/**
 * A class that defines how ColorCatch is played.
 *
 * @author Rick
 */

public class ColorCatchGame {

    private final Context context;

    /**
     * Game Variables are used to store date in a static class that any class
     * can access.
     */
    private GameVariables gv = GameVariables.getInstance();
    /**
     * The Screen Factory creates the loading, menu or radical.appwards.colorcatch.game radical.appwards.colorcatch.screen.
     */
    private ScreenFactory screenFactory = new ScreenFactory();
    /**
     * The current radical.appwards.colorcatch.game radical.appwards.colorcatch.screen.
     */
    private Screen screen;

    /**
     * On start the radical.appwards.colorcatch.game goes to the loading radical.appwards.colorcatch.screen.
     */
    public ColorCatchGame(Context c) {
        setLoadingScreen(c);
        context = c;
    }

    public void myDraw(Canvas canvas) {
        screen.myDraw(context, canvas);
    }

    public void updatePhysics() {
        screen.updatePhysics(context);
    }

    public boolean getMenuPlaying() {
        return gv.getMenuPlaying();
    }

    public void startGame(Context context) {
        gv.setGamePlaying(true);
        gv.loadingPlaying = false;
        gv.setMenuPlaying(false);
        screen = screenFactory.createScreen(context, "radical/appwards/colorcatch/game");
        Audio.getInstance().playGame(context);
    }

    public boolean getLoadingPlaying() {
        return gv.loadingPlaying;
    }

    public boolean getGameVarsLoaded() {
        return gv.getGameVarsLoaded();
    }

    /**
     * I added setting the touch coordinates here so that I avoid any radical.appwards.colorcatch.game radical.appwards.colorcatch.logic
     * in the View or Activity class.
     */
    public void setNewOnTouchCoords(int x, int y) {
        gv.newX = x;
        gv.newY = y;
    }

    public void eventAction(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            gv.movePlayerToPoint();
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {

        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            gv.movePlayerToPoint();
        }
        // radical.appwards.colorcatch.screen specific action events, such as end radical.appwards.colorcatch.game drawing for fun.
        screen.eventAction(event);
    }

    public void init(DisplayMetrics displayMetrics) {
        gv.setMetrics(displayMetrics);
    }

    public void setSurfaceSize(int width, int height) {
        gv.screenW = width;
        gv.screenH = height;
        gv.setScreenVarsSizes();
    }

    public void setMenuScreen(Context context) {
        gv.setGamePlaying(false);
        gv.loadingPlaying = false;
        gv.setMenuPlaying(true);
        screen = screenFactory.createScreen(context, "menu");
        Audio.getInstance().playMenu(context);
    }

    public void setLoadingScreen(Context context) {
        gv.setGamePlaying(false);
        gv.loadingPlaying = true;
        gv.setMenuPlaying(false);
        screen = screenFactory.createScreen(context, "loading");
    }
}
