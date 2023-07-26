package radical.appwards.colorcatch.level;

import radical.appwards.colorcatch.logic.GameDraw;
import radical.appwards.colorcatch.logic.GameLogic;
import radical.appwards.colorcatch.objects.Enemy;
import radical.appwards.colorcatch.variables.GameVariables;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Process;

/**
 * A class that defines radical.appwards.colorcatch.level 7 radical.appwards.colorcatch.logic.
 * 
 * @author Rick
 * 
 */

public class Level07Impl implements Level {

	private GameVariables gv;
	private GameLogic gl;
	private GameDraw gd;

	/**
	 * Sets the enemy creation timing.
	 */
	private int createEnemyTimer;

	public Level07Impl() {
		gv = GameVariables.getInstance();
		gl = new GameLogic();
		gd = new GameDraw();
		gv.gameVarsAreLoading = false;
		gv.setGameVarsLoaded(false);
		createEnemyTimer = 20;
	}

	/**
	 * Begins with an intro message and then begins creating enemies.
	 */
	@Override
	public void myDraw(Canvas canvas) {
		canvas.drawText("Level 7", gv.screenW / 2, gv.whitePaint.getTextSize(),
				gv.whitePaintCenterAlign);
		canvas.drawText("Misses: " + gv.getHealth() + " ", gv.screenW,
				gv.whitePaint.getTextSize(), gv.getWhitePaintRightAlinged());

		if (gv.getGameVarsLoaded())
			gd.myDraw(canvas);

		if (gv.gameTimer < gv.INTRO_PAUSE_TIME) {
			gv.setEnemyCreation(false);
			gv.cyclePaint();
			canvas.drawText("!! Loading Level 7 !!", gv.screenW / 2,
					gv.screenH / 2, gv.randPaint);
		} else {
			gv.setEnemyCreation(true);
		}
	}

	/**
	 * Initializes the radical.appwards.colorcatch.level and then begins creating enemies.
	 */
	@Override
	public void updatePhysics(Context context) {
		// move radical.appwards.colorcatch.objects and check for hits
		if (!gv.gameVarsAreLoading) {
			loadLevel7Objects();
		} else if (!gv.getGameVarsLoaded()) {
			// wait
		} else {
			gl.updatePhysics7(context);

			// create or split fiveenemyblock on radical.appwards.colorcatch.screen
			if (gv.getEnemyCreation() && gv.gameTimer % createEnemyTimer == 0) {
				gl.createSingleEnemyBlock();
			}

			if (gv.gameTimer % gv.getLevelBreak() == 0) {
				gv.incEnemyArraySpeeed();
				if (createEnemyTimer > 5)
					createEnemyTimer -= 5;
			}
		}
	}

	public void loadLevel7Objects() {
		
		Thread loadingThread = new Thread() {
			
			@Override
			public void run() {
				initLevel7Objects();
			}

			public void initLevel7Objects() {
				if (!gv.continuedGame) {
					gv.gameTimer = 0;
				}

				gv.continuedGame = false;

				gv.enemyCount = 0;
				gv.levelEnemies = 6;
				gv.level = 7;

				for (int i = 0; i < gv.maxEnemies; i++) {

					gv.tempside = 5 + gv.rand.nextInt(gv.screenH - gv.enemyH
							- 10);

					switch (i % 6) {
					case 0:
						gv.enemyArray[i] = new Enemy(-gv.enemyW, gv.tempside,
								0, gv.tempside + gv.enemyH, gv.speedModifier);
						gv.enemyArray[i].setPaint(gv.redPaint);
						gv.enemyArray[i].setMovable(gv.moveR[i]);
						break;
					case 1:
						gv.enemyArray[i] = new Enemy(-gv.enemyW, gv.tempside,
								0, gv.tempside + gv.enemyH, gv.speedModifier);
						gv.enemyArray[i].setPaint(gv.bluePaint);
						gv.enemyArray[i].setMovable(gv.moveR[i]);
						break;
					case 2:
						gv.enemyArray[i] = new Enemy(-gv.enemyW, gv.tempside,
								0, gv.tempside + gv.enemyH, gv.speedModifier);
						gv.enemyArray[i].setPaint(gv.greenPaint);
						gv.enemyArray[i].setMovable(gv.moveR[i]);
						break;
					case 3:
						gv.enemyArray[i] = new Enemy(gv.screenW, gv.tempside,
								gv.screenW + gv.enemyW,
								gv.tempside + gv.enemyH, gv.speedModifier);
						gv.enemyArray[i].setPaint(gv.greenPaint);
						gv.enemyArray[i].setMovable(gv.moveL[i]);
						break;
					case 4:
						gv.enemyArray[i] = new Enemy(gv.screenW, gv.tempside,
								gv.screenW + gv.enemyW,
								gv.tempside + gv.enemyH, gv.speedModifier);
						gv.enemyArray[i].setPaint(gv.bluePaint);
						gv.enemyArray[i].setMovable(gv.moveL[i]);
						break;
					case 5:
						gv.enemyArray[i] = new Enemy(gv.screenW, gv.tempside,
								gv.screenW + gv.enemyW,
								gv.tempside + gv.enemyH, gv.speedModifier);
						gv.enemyArray[i].setPaint(gv.redPaint);
						gv.enemyArray[i].setMovable(gv.moveL[i]);
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
