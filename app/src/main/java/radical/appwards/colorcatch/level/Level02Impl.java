package radical.appwards.colorcatch.level;

import radical.appwards.colorcatch.logic.GameDraw;
import radical.appwards.colorcatch.logic.GameLogic;
import radical.appwards.colorcatch.objects.Enemy;
import radical.appwards.colorcatch.variables.GameVariables;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Process;

/**
 * A class that defines radical.appwards.colorcatch.level 2 radical.appwards.colorcatch.logic.
 * 
 * @author Rick
 * 
 */

public class Level02Impl implements Level {

	private GameVariables gv;
	private GameLogic gl;
	private GameDraw gd;
	/**
	 * Timing to create a new enemy.
	 */
	private int createEnemyTimer;

	public Level02Impl() {
		gv = GameVariables.getInstance();
		gl = new GameLogic();
		gd = new GameDraw();
		gv.gameVarsAreLoading = false;
		gv.setGameVarsLoaded(false);
		createEnemyTimer = 15;
	}

	/**
	 * Show opening message and them draw new enemies.
	 */
	@Override
	public void myDraw(Canvas canvas) {
		canvas.drawText("Level 2", gv.screenW / 2, gv.whitePaint
				.getTextSize(), gv.whitePaintCenterAlign);
		canvas.drawText("Misses: " + gv.getHealth() + " ", gv.screenW, gv
				.whitePaint.getTextSize(), gv.getWhitePaintRightAlinged());

		if (gv.getGameVarsLoaded())
			gd.myDraw(canvas);

		if (gv.gameTimer < gv.INTRO_PAUSE_TIME) {
			gv.cyclePaint();
			canvas.drawText("!! Loading Level 2 !!", gv.screenW / 2,
					gv.screenH / 2, gv.randPaint);
		} else {
			gv.setEnemyCreation(true);
		}
	}

	/**
	 * Initialize radical.appwards.colorcatch.level and then update radical.appwards.colorcatch.level when ready and/or create new
	 * enemies.
	 */
	@Override
	public void updatePhysics(Context context) {
		// move radical.appwards.colorcatch.objects and check for hits
		if (!gv.gameVarsAreLoading) {
			loadLevel2Objects();
		} else if (!gv.getGameVarsLoaded()) {
			// wait
		} else {
			gl.updatePhysics(context);
			if (gv.getEnemyCreation()
					&& gv.gameTimer % createEnemyTimer == 0)
				gl.createSingleEnemyBlock();

			if (gv.gameTimer % gv.getLevelBreak() == 0) {
				gv.incEnemyArraySpeeed();
			}
		}
	}
	
	public void loadLevel2Objects() {
		// the radical.appwards.colorcatch.objects for level1
		Thread loadingThread = new Thread() {
			@Override
			public void run() {
				initLevel2Objects();
			}

			public void initLevel2Objects() {
				// restore radical.appwards.colorcatch.objects to radical.appwards.colorcatch.level 2 default

				if (!gv.continuedGame) {
					// start as new radical.appwards.colorcatch.level
					gv.gameTimer = 0;
				}

				gv.continuedGame = false;

				gv.level = 2;
				gv.enemyCount = 0;
				gv.levelEnemies = gv.maxEnemies;


				// reset enemies at new radical.appwards.colorcatch.level with new radical.appwards.colorcatch.movements
				for (int i = 0; i < gv.maxEnemies; i++) {
					gv.tempLeft = gv.rand.nextInt((gv.screenW - gv.enemyW) - (gv.enemyW) + 1);
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

					// leaves one of the movable as its straight down movable
					switch (gv.rand.nextInt(3)) {
					case 0:
						gv.enemyArray[i].setMovable(gv.moveLD[i]);
						break;
					case 1:
						gv.enemyArray[i].setMovable(gv.moveRD[i]);
						break;
					}

				}

				gv.gameVarsLoaded = true;

			}
		};
		loadingThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
		loadingThread.start();
		gv.gameVarsAreLoading = true;

	}
}
