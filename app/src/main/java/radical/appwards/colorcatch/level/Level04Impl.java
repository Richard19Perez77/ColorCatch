package radical.appwards.colorcatch.level;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Process;

import radical.appwards.colorcatch.logic.GameDraw;
import radical.appwards.colorcatch.logic.GameLogic;
import radical.appwards.colorcatch.objects.Enemy;
import radical.appwards.colorcatch.variables.GameVariables;

/**
 * A class that defines the radical.appwards.colorcatch.level 4 radical.appwards.colorcatch.logic.
 * 
 * @author Rick
 * 
 */

public class Level04Impl implements Level {

	private GameVariables gv;
	private GameLogic gl;
	private GameDraw gd;
	/**
	 * Sets the timing for the radical.appwards.colorcatch.level enemies creation.
	 */
	private int createEnemyTimer;

	public Level04Impl() {
		gv = GameVariables.getInstance();
		gl = new GameLogic();
		gd = new GameDraw();
		gv.gameVarsAreLoading = false;
		gv.setGameVarsLoaded(false);
		createEnemyTimer = 5;
	}

	/**
	 * Shows an intro message and then starts drawing enemies.
	 */
	@Override
	public void myDraw(Canvas canvas) {
		canvas.drawText("Level 4", gv.screenW / 2, gv.whitePaint.getTextSize(),
				gv.whitePaintCenterAlign);
		canvas.drawText("Misses: " + gv.getHealth() + " ", gv.screenW,
				gv.whitePaint.getTextSize(), gv.getWhitePaintRightAlinged());

		if (gv.getGameVarsLoaded())
			gd.myDraw(canvas);

		if (gv.gameTimer < gv.INTRO_PAUSE_TIME) {
			gv.cyclePaint();
			canvas.drawText("!! Loading Level 4 !!", gv.screenW / 2,
					gv.screenH / 2, gv.randPaint);
		} else {
			gv.setEnemyCreation(true);
		}
	}

	/**
	 * Initializes radical.appwards.colorcatch.level and then creates, updates the radical.appwards.colorcatch.objects.
	 */
	@Override
	public void updatePhysics(Context context) {
		// move radical.appwards.colorcatch.objects and check for hits
		if (!gv.gameVarsAreLoading) {
			loadLevel4Objects();
		} else if (!gv.getGameVarsLoaded()) {
			// wait
		} else {
			gl.updatePhysics(context);
			if (gv.getEnemyCreation() && gv.gameTimer % createEnemyTimer == 0)
				gl.createSingleEnemyBlock();

			if (gv.gameTimer % gv.getLevelBreak() == 0) {
				gv.incEnemyArraySpeeed();
			}
		}
	}

	public void loadLevel4Objects() {
		Thread loadingThread = new Thread() {
			@Override
			public void run() {
				initLevel4Objects();
			}

			public void initLevel4Objects() {
				// restore radical.appwards.colorcatch.objects to radical.appwards.colorcatch.level 2 default
				if (!gv.continuedGame) {
					// reset radical.appwards.colorcatch.level timer
					gv.gameTimer = 0;
				}

				gv.continuedGame = false;

				gv.enemyCount = 0;
				gv.levelEnemies = gv.maxEnemies;
				gv.level = 4;

				// reset enemies at new radical.appwards.colorcatch.level with new radical.appwards.colorcatch.movements
				for (int i = 0; i < gv.maxEnemies; i++) {
					gv.tempLeft = gv.rand.nextInt((gv.screenW - gv.enemyW) - (gv.enemyW)
							+ 1);
					gv.enemyArray[i] = new Enemy(gv.tempLeft, 0 - gv.enemyH, gv.tempLeft
							+ gv.enemyW, 0, gv.speedModifier);

					switch (i % 3) {
					case 0:
						gv.enemyArray[i].setPaint(gv.redPaint);
						break;
					case 1:
						gv.enemyArray[i].setPaint(gv.bluePaint);
						break;
					case 2:
						gv.enemyArray[i].setPaint(gv.greenPaint);
						break;
					}

					gv.enemyArray[i].setMovable(gv.moveSD[i]);
					gv.enemyArray[i].incSpeed();
					gv.enemyArray[i].incSpeed();

				}

				gv.gameVarsLoaded = true;

			}
		};

		loadingThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
		loadingThread.start();
		gv.gameVarsAreLoading = true;

	}
}
