package radical.appwards.colorcatch.level;

import radical.appwards.colorcatch.logic.GameDraw;
import radical.appwards.colorcatch.logic.GameLogic;
import radical.appwards.colorcatch.objects.Enemy;
import radical.appwards.colorcatch.variables.GameVariables;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Process;
import android.graphics.Point;

/**
 * A class that defines radical.appwards.colorcatch.level 8 radical.appwards.colorcatch.logic.
 * 
 * @author Rick
 * 
 */

public class Level08Impl implements Level {

	private GameVariables gv;
	private GameLogic gl;
	private GameDraw gd;

	/**
	 * Sets the enemy creation timing.
	 */
	private int createEnemyTimer;

	public Level08Impl() {
		gv = GameVariables.getInstance();
		gl = new GameLogic();
		gd = new GameDraw();
		gv.gameVarsAreLoading = false;
		gv.setGameVarsLoaded(false);
		createEnemyTimer = 25;
	}

	/**
	 * Begins with an intro message and then begins creating enemies.
	 */
	@Override
	public void myDraw(Canvas canvas) {
		canvas.drawText("Level 8", gv.screenW / 2, gv.whitePaint.getTextSize(),
				gv.whitePaintCenterAlign);
		canvas.drawText("Misses: " + gv.getHealth() + " ", gv.screenW,
				gv.whitePaint.getTextSize(), gv.getWhitePaintRightAlinged());

		//create center square for constant random paint
		int top = gv.screenH / 2 - gv.getEnemyHeight() / 2;
		int left = gv.screenW / 2 - gv.getEnemyWidth() / 2;
		int bottom = top + gv.getEnemyHeight();
		int right = left + gv.getEnemyWidth();

		if (gv.getGameVarsLoaded()) {
			gd.myDraw(canvas);
		}

		if (gv.gameTimer < gv.INTRO_PAUSE_TIME) {
			gv.setEnemyCreation(false);
			gv.cyclePaint();
			canvas.drawText("!! Loading Level 8 !!", gv.screenW / 2,
					gv.screenH / 2, gv.randPaint);
		} else {
			gv.setEnemyCreation(true);
			canvas.drawRect(left, top, right, bottom, gv.randPaint);
		}
	}

	/**
	 * Initializes the radical.appwards.colorcatch.level and then begins creating enemies.
	 */
	@Override
	public void updatePhysics(Context context) {
		if (!gv.gameVarsAreLoading) {
			loadLevel8Objects();
		} else if (!gv.getGameVarsLoaded()) {
			// wait
		} else {
			gl.updatePhysics8(context);

			// create or split fiveenemyblock on radical.appwards.colorcatch.screen
			if (gv.getEnemyCreation() && gv.gameTimer % createEnemyTimer == 0) {
				gl.createSingleEnemyBlock();
			}

		}
	}

	public void loadLevel8Objects() {

		Thread loadingThread = new Thread() {

			@Override
			public void run() {
				initLevel8Objects();
			}

			public void initLevel8Objects() {
				if (!gv.continuedGame) {
					gv.gameTimer = 0;
				}
				
				gv.continuedGame = false;

				gv.enemyCount = 0;
				gv.levelEnemies = gv.maxEnemies;
				gv.level = 8;

				// create all enemies to start at center square and issue a
				// border point to each at random
				int y = gv.screenH / 2 - gv.getEnemyHeight() / 2;
				int x = gv.screenW / 2 - gv.getEnemyWidth() / 2;

				for (int i = 0; i < gv.maxEnemies; i++) {
					switch (i % 4) {
					case 0:
						gv.enemyArray[i] = new Enemy(x, y, x + gv.enemyW, y
								+ gv.enemyH, gv.speedModifier);
						gv.enemyArray[i].setPaint(gv.getRandomPaint());
						gv.enemyArray[i].setMovable(gv.movePath[i]);
						gv.enemyArray[i].getMovable().plotPoints(
								new Point(x, y), new Point(0, 0));
						break;
					case 1:
						gv.enemyArray[i] = new Enemy(x, y, x + gv.enemyW, y
								+ gv.enemyH, gv.speedModifier);
						gv.enemyArray[i].setPaint(gv.getRandomPaint());
						gv.enemyArray[i].setMovable(gv.movePath[i]);
						gv.enemyArray[i].getMovable().plotPoints(
								new Point(x, y),
								new Point(gv.screenW - gv.enemyW, 0));
						break;
					case 2:
						gv.enemyArray[i] = new Enemy(x, y, x + gv.enemyW, y
								+ gv.enemyH, gv.speedModifier);
						gv.enemyArray[i].setPaint(gv.getRandomPaint());
						gv.enemyArray[i].setMovable(gv.movePath[i]);
						gv.enemyArray[i].getMovable().plotPoints(
								new Point(x, y),
								new Point(gv.screenW - gv.enemyW, gv.screenH
										- gv.enemyH));
						break;
					case 3:
						gv.enemyArray[i] = new Enemy(x, y, x + gv.enemyW, y
								+ gv.enemyH, gv.speedModifier);
						gv.enemyArray[i].setPaint(gv.getRandomPaint());
						gv.enemyArray[i].setMovable(gv.movePath[i]);
						gv.enemyArray[i].getMovable().plotPoints(
								new Point(x, y),
								new Point(0, gv.screenH - gv.enemyH));
						break;
					}
					gv.enemyArray[i].setSpeed(0);
				}
				gv.gameVarsLoaded = true;
			}
		};
		
		loadingThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
		loadingThread.start();
		gv.gameVarsAreLoading = true;
	}
}
