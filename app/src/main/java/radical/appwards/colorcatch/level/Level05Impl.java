package radical.appwards.colorcatch.level;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Process;

import radical.appwards.colorcatch.logic.GameDraw;
import radical.appwards.colorcatch.logic.GameLogic;
import radical.appwards.colorcatch.objects.Enemy;
import radical.appwards.colorcatch.variables.GameVariables;

/**
 * A class that defines the radical.appwards.colorcatch.level 5 radical.appwards.colorcatch.logic.
 * 
 * @author Rick
 * 
 */

public class Level05Impl implements Level {

	private GameVariables gv;
	private GameLogic gl;
	private GameDraw gd;
	/**
	 * Sets the timing for the enemies creation on this radical.appwards.colorcatch.level.
	 */
	private int createEnemyTimer;

	public Level05Impl() {
		gv = GameVariables.getInstance();
		gl = new GameLogic();
		gd = new GameDraw();
		gv.gameVarsAreLoading = false;
		gv.setGameVarsLoaded(false);
		createEnemyTimer = 20;
	}

	/**
	 * Shows a message and then starts to draw enemies.
	 */
	@Override
	public void myDraw(Canvas canvas) {
		canvas.drawText("Level 5", gv.screenW / 2, gv.whitePaint.getTextSize(),
				gv.whitePaintCenterAlign);
		canvas.drawText("Misses: " + gv.getHealth() + " ", gv.screenW,
				gv.whitePaint.getTextSize(), gv.getWhitePaintRightAlinged());

		if (gv.getGameVarsLoaded())
			gd.myDraw(canvas);

		if (gv.gameTimer < gv.INTRO_PAUSE_TIME) {
			gv.cyclePaint();
			canvas.drawText("!! Loading Level 5 !!", gv.screenW / 2,
					gv.screenH / 2, gv.randPaint);
		} else {
			gv.setEnemyCreation(true);
		}
	}

	/**
	 * Initializes and then starts creating enemies.
	 */
	@Override
	public void updatePhysics(Context context) {
		// move radical.appwards.colorcatch.objects and check for hits
		if (!gv.gameVarsAreLoading) {
			loadLevel5Objects();
		} else if (!gv.getGameVarsLoaded()) {
			// wait
		} else {
			gl.updateMultiBlockPhysics(context);

			// create or split fiveenemyblock on radical.appwards.colorcatch.screen
			if (gv.getEnemyCreation() && gv.gameTimer % createEnemyTimer == 0) {
				gl.createMultiEnemyBlock();
				gl.splitMultiBlock();
			}

			if (gv.gameTimer % gv.getLevelBreak() == 0) {
				gv.incEnemyArraySpeeed();
				if (createEnemyTimer > 5)
					createEnemyTimer -= 5;
			}
		}
	}

	public void loadLevel5Objects() {
		Thread loadingThread = new Thread() {
			@Override
			public void run() {
				initLevel5Objects();
			}

			public void initLevel5Objects() {
				// restore radical.appwards.colorcatch.objects to radical.appwards.colorcatch.level 2 default
				if (!gv.continuedGame) {
					gv.gameTimer = 0;
				}

				gv.continuedGame = false;

				gv.enemyCount = 0;
				gv.levelEnemies = 9;
				gv.level = 5;

				// reset enemies at new radical.appwards.colorcatch.level with new radical.appwards.colorcatch.movements
				for (int i = 0; i < gv.maxEnemies; i++) {
					gv.tempLeft = gv.rand.nextInt((gv.screenW - gv.enemyW)
							- (gv.enemyW) + 1);
					gv.enemyArray[i] = new Enemy(gv.tempLeft, 0 - gv.enemyH,
							gv.tempLeft + gv.enemyW, 0, gv.speedModifier * 2);

					switch (i) {
					case 0:
						gv.enemyArray[i].setPaint(gv.redPaint);
						break;
					case 1:
						gv.enemyArray[i].setPaint(gv.bluePaint);
						break;
					case 2:
						gv.enemyArray[i].setPaint(gv.greenPaint);
						break;
					case 3:
						gv.enemyArray[i].setPaint(gv.greenPaint);
						break;
					case 4:
						gv.enemyArray[i].setPaint(gv.bluePaint);
						break;
					case 5:
						gv.enemyArray[i].setPaint(gv.redPaint);
						break;
					case 6:
						gv.enemyArray[i].setPaint(gv.bluePaint);
						break;
					case 7:
						gv.enemyArray[i].setPaint(gv.redPaint);
						break;
					case 8:
						gv.enemyArray[i].setPaint(gv.greenPaint);
						break;
					}
					gv.enemyArray[i].setMovable(gv.moveSD[i]);
				}
				gv.gameVarsLoaded = true;
			}
		};
		
		loadingThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
		loadingThread.start();
		gv.gameVarsAreLoading = true;
	}
}
