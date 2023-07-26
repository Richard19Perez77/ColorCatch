package radical.appwards.colorcatch.level;

import java.util.ArrayList;


import android.content.Context;
import android.graphics.Canvas;
import android.os.Process;

import radical.appwards.colorcatch.database.MyDb;
import radical.appwards.colorcatch.logic.GameDraw;
import radical.appwards.colorcatch.variables.GameVariables;

/**
 * A class that defines the end radical.appwards.colorcatch.game radical.appwards.colorcatch.logic.
 *
 * @author Rick
 */

public class LevelEndingImpl implements Level {

    private GameVariables gv;
    private GameDraw gd;
    private String radical, music;
    private String highScore;

    public LevelEndingImpl(Context context) {
        MyDb myDb;
        gv = GameVariables.getInstance();
        gd = new GameDraw();
        gv.gameVarsAreLoading = false;
        gv.setGameVarsLoaded(false);
        radical = "Radical\u2605Appwards";
        music = "\u2669pinklogik.bandcamp.com\u2669";
        myDb = new MyDb(context, gv.score);
        highScore = myDb.getHighScore();
    }

    /**
     * After the hint to use the menu to restart the radical.appwards.colorcatch.game the user is free to
     * draw on the canvas or restart the radical.appwards.colorcatch.game. Current round and high score of
     * previous games is shown.
     */
    @Override
    public void myDraw(Canvas canvas) {

        canvas.drawText(radical, gv.screenW / 2, gv.screenH / 10,
                gv.whitePaintCenterAlign);

        canvas.drawText("Color", gv.screenW / 2, gv.screenH / 3,
                gv.getBlackBoldPaint());
        canvas.drawText("Color", gv.screenW / 2, gv.screenH / 3 + 5,
                gv.getYellowBoldPaint());

        canvas.drawText("!! Thanks for Playing !!", gv.screenW / 2, gv.screenH
                / 2 - gv.randPaint.getTextSize(), gv.randPaint);

        canvas.drawText("Round Score: " + gv.score, gv.screenW / 2, gv.screenH
                / 2 + gv.randPaint.getTextSize(), gv.randPaint);
        canvas.drawText("High Score: " + highScore, gv.screenW / 2, gv.screenH
                / 2 + gv.randPaint.getTextSize() * 2, gv.randPaint);

        canvas.drawText("Catch", gv.screenW / 2, gv.screenH - gv.screenH / 4,
                gv.getBlackBoldPaint());
        canvas.drawText("Catch", gv.screenW / 2, gv.screenH - gv.screenH / 4
                - 5, gv.getYellowBoldPaint());

        canvas.drawText(music, gv.screenW / 2, gv.screenH - gv.screenH / 10,
                gv.whitePaintCenterAlign);

        if (gv.getGameVarsLoaded())
            gd.myEndDraw(canvas);
    }

    /**
     * The radical.appwards.colorcatch.level radical.appwards.colorcatch.logic is in gl or GameLogic class.
     */
    @Override
    public void updatePhysics(Context context) {
        if (!gv.gameVarsAreLoading) {
            loadLevelEndObjects();
        } else if (!gv.getGameVarsLoaded()) {
            // wait
        } else {
            if (!gv.getEnemyCreation())
                gv.setEnemyCreation(true);
        }
    }

    public void loadLevelEndObjects() {
        Thread loadingThread = new Thread() {
            @Override
            public void run() {
                initLevelEndObjects();
            }

            public void initLevelEndObjects() {
                gv.enemyCount = 0;
                gv.levelEnemies = gv.maxEnemies * 3;
                gv.level = 0;

                gv.xs = new ArrayList<>();
                gv.ys = new ArrayList<>();

                gv.gameVarsLoaded = true;
            }
        };

        loadingThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
        loadingThread.start();
        gv.gameVarsAreLoading = true;

    }

}
