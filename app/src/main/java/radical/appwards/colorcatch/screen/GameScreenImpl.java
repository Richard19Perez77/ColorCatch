package radical.appwards.colorcatch.screen;

import radical.appwards.colorcatch.R;
import radical.appwards.colorcatch.audio.Audio;
import radical.appwards.colorcatch.background.GameScreenBackground;
import radical.appwards.colorcatch.level.Level;
import radical.appwards.colorcatch.level.LevelFactory;
import radical.appwards.colorcatch.logic.GameLogic;
import radical.appwards.colorcatch.variables.GameVariables;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

/**
 * A class to define the Game Screen of color catch.
 *
 * @author Rick Perez
 */

public class GameScreenImpl implements Screen {

    private GameVariables gv;
    private GameLogic gl;
    private GameScreenBackground bg;
    private LevelFactory lf = new LevelFactory();
    private Level level;
    private int levels = GameVariables.LEVELS;
    private static final int POINT = 3;
    private CoordinatorLayout coordinatorLayout;

    /**
     * If the radical.appwards.colorcatch.game has just continued create the radical.appwards.colorcatch.level it left off at. If in debug mode for a particular radical.appwards.colorcatch.level skip to it.
     */
    public GameScreenImpl(final Context context) {
        coordinatorLayout = (CoordinatorLayout) ((Activity) context).findViewById(R.id.coordinator_layout);
        gv = GameVariables.getInstance();
        bg = new GameScreenBackground();
        gl = new GameLogic();
        lf = new LevelFactory();

        if (gv.continuedGame) {
            gameContinue(context);
        } else {
            //set debug radical.appwards.colorcatch.level
            if (gv.DEBUG_MODE) {
                gv.setUpGame();
                level = lf.createLevel(context, gv.DEBUG_LEVEL);
                gv.setCurrLevel(gv.DEBUG_LEVEL);
            } else {
                level = lf.createLevel(context, 1);
                gv.setCurrLevel(1);
            }
        }

    }

    /**
     * Create the radical.appwards.colorcatch.level retrieved from the savedSate radical.appwards.colorcatch.database.
     */
    public void gameContinue(Context context) {
        // start radical.appwards.colorcatch.game with saved radical.appwards.colorcatch.game radical.appwards.colorcatch.variables loaded
        level = lf.createLevel(context, gv.getCurrLevel());
    }

    /**
     * Logic across all levels includes drawing the score board and the radical.appwards.colorcatch.logic of
     * Scoring. Also keeps track of timing to move to the next radical.appwards.colorcatch.level and if the
     * player is out of misses (health).
     */
    @Override
    public void myDraw(Context context, Canvas canvas) {

        bg.draw(canvas);

        if (gv.getGameVarsLoaded()) {
            if (gv.getCurrLevel() != 0) {
                canvas.drawRect(0, 0, gv.screenW, gv.screenH,
                        gv.getTargetPaint());

                canvas.drawText(" Score: " + gv.score, 0,
                        gv.whitePaint.getTextSize(), gv.whitePaint);
            }

            level.myDraw(canvas);

            checkColorMatch(context, canvas);

            // check for new radical.appwards.colorcatch.level if not radical.appwards.colorcatch.game over
            if (gv.gameTimer == gv.getNewLevel()) {
                gv.setEnemyCreation(false);
                gv.incCurrentLevel();
                // switch to next radical.appwards.colorcatch.level if possible
                if (gv.getCurrLevel() <= levels) {
                    gl.destroyAllEnemies(canvas);
                    level = lf.createLevel(context, gv.getCurrLevel());
                    gv.setEnemyCreation(false);
                    bg.refresh();
                    if (gv.getCurrLevel() == GameVariables.LEVELS / 2)
                        Audio.getInstance().playTrack2(context);
                }
            }

            // check for 0 health and start radical.appwards.colorcatch.game over
            if (gv.getHealth() == 0 && gv.getCurrLevel() != 0) {
                gl.destroyAllEnemies(canvas);
                level = lf.createLevel(context, 0);
                gv.setCurrLevel(0);
                Audio.getInstance().playEndTrack(context);
            }
        }

    }

    /**
     * Checks the color of the player and the target color to be so similar
     * enough to score a point.
     *
     * @param canvas The object the rest of the radical.appwards.colorcatch.objects are drawn on.
     */
    private void checkColorMatch(Context context, Canvas canvas) {
        int colora, colorb = 0;

        if (gv.player != null && gv.getTargetPaint() != null) {

            if (gv.getTargetPaint().getColor() == Color.RED) {
                colora = gv.player.getPaint().getColor();
                colorb = Color.red(colora);
            } else if (gv.getTargetPaint().getColor() == Color.BLUE) {
                colora = gv.player.getPaint().getColor();
                colorb = Color.blue(colora);
            } else if (gv.getTargetPaint().getColor() == Color.GREEN) {
                colora = gv.player.getPaint().getColor();
                colorb = Color.green(colora);
            }

            if (colorb >= 235 && colorb <= 256) {
                Audio.getInstance().playSound(context, POINT);
                gl.destroyAllEnemies(canvas);
                gl.newTargetColor();

                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Drag square to catch " + getTargetColoText() + " squares", Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(gv.getTargetPaint().getColor());
                snackbar.show();

                gv.score = gv.score + GameVariables.SCORE_INC;

                //reset bg shapes to white
                gv.mixedColorInt = Color.WHITE;
                gv.setNewMixedColorInt = true;
            }
        }
    }

    private String getTargetColoText() {
        String res = "";
        if (gv.getTargetPaint().getColor() == Color.RED) {
            res = "Red";
        } else if (gv.getTargetPaint().getColor() == Color.BLUE) {
            res = "Blue";
        } else if (gv.getTargetPaint().getColor() == Color.GREEN) {
            res = "Green";
        }

        return res;
    }

    /**
     * If the radical.appwards.colorcatch.game is playing a radical.appwards.colorcatch.level then increment the radical.appwards.colorcatch.game timer. Updates
     * radical.appwards.colorcatch.game physics.
     */
    @Override
    public void updatePhysics(Context context) {
        //check for new color to radical.appwards.colorcatch.background shapes
        if (gv.setNewMixedColorInt)
            bg.updateColors(gv.mixedColorInt);

        gv.cyclePaint();
        if (gv.getCurrLevel() != 0)
            gv.gameTimer = gv.gameTimer + 1;
        bg.updatePhysics();
        level.updatePhysics(context);
    }

    /**
     * Called to send the motion event to the current radical.appwards.colorcatch.level for specific radical.appwards.colorcatch.level
     * radical.appwards.colorcatch.logic.
     */
    @Override
    public void eventAction(MotionEvent event) {
        // ending radical.appwards.colorcatch.screen graphics are touch responsive
        if (gv.getCurrLevel() == 0) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                gv.clearXsYs();
                gv.enemyCount = 0;
            }

            if (gv.getEnemyCount() < gv.getLevelEnemies()) {
                gv.addEndX(gv.getNewX());
                gv.addEndY(gv.getNewY());
                gv.incEnemyCount();
            } else {
                gv.removeEndXY();
                gv.decEnemyCount();
            }
        }
    }
}
