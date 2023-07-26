package radical.appwards.colorcatch.level;

import radical.appwards.colorcatch.logic.GameDraw;
import radical.appwards.colorcatch.logic.GameLogic;
import radical.appwards.colorcatch.movements.MovableLeftDownImpl;
import radical.appwards.colorcatch.movements.MovableLeftImpl;
import radical.appwards.colorcatch.movements.MovablePathImpl;
import radical.appwards.colorcatch.movements.MovableRightDownImpl;
import radical.appwards.colorcatch.movements.MovableRightImpl;
import radical.appwards.colorcatch.movements.MovableStraightDownImpl;
import radical.appwards.colorcatch.objects.Enemy;
import radical.appwards.colorcatch.objects.Player;
import radical.appwards.colorcatch.objects.Shield;
import radical.appwards.colorcatch.variables.GameVariables;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Process;

/**
 * A class that defines radical.appwards.colorcatch.level 1 radical.appwards.colorcatch.logic.
 * 
 * @author Rick Perez
 * 
 */

public class Level01Impl implements Level {

	private GameVariables gv;
	private GameLogic gl;
	private GameDraw gd;
	/**
	 * Used to time when to create new squares.
	 */
	private int createEnemyTimer;
	private boolean hintDisplayed;

	public Level01Impl() {
		gv = GameVariables.getInstance();
		gl = new GameLogic();
		gv.gameVarsAreLoading = false;
		gv.setGameVarsLoaded(false);
		gd = new GameDraw();
		createEnemyTimer = 20;
		hintDisplayed = false;
	}

	/**
	 * Draws on the canvas the opening message and when finished the radical.appwards.colorcatch.level
	 * radical.appwards.colorcatch.objects.
	 */
	@Override
	public void myDraw(Canvas canvas) {
		canvas.drawText("Level 1", gv.screenW / 2, gv.whitePaint
				.getTextSize(), gv.whitePaintCenterAlign);
		canvas.drawText("Misses: " + gv.getHealth() + " ", gv.screenW, gv
				.whitePaint.getTextSize(), gv.getWhitePaintRightAlinged());

		if (gv.getGameVarsLoaded())
			gd.myDraw(canvas);

		if (gv.gameTimer < gv.INTRO_PAUSE_TIME) {
			gv.setEnemyCreation(false);
			gv.cyclePaint();
			canvas.drawText("!! Loading Level 1 !!", gv.screenW / 2,
					gv.screenH / 2, gv.randPaint);
		} else {
			gv.setEnemyCreation(true);
		}
	}

	/**
	 * The update radical.appwards.colorcatch.logic for the radical.appwards.colorcatch.level. If the radical.appwards.colorcatch.level has not been initialized it
	 * will do that here before starting enemy creation.
	 */
	@Override
	public void updatePhysics(Context context) {
		// move radical.appwards.colorcatch.objects and check for hits
		if (!gv.gameVarsAreLoading) {
			loadLevel1Objects();
		} else if (!gv.getGameVarsLoaded()) {
			// wait while vars are loading
		} else {
			//now loaded updated them 
			gl.updatePhysics(context);
			if (gv.getEnemyCreation()
					&& gv.gameTimer % createEnemyTimer == 0)
				gl.createSingleEnemyBlock();

			if (gv.gameTimer % gv.getLevelBreak() == 0
					&& gv.getGameVarsLoaded()) {
				gv.incEnemyArraySpeeed();
				if (createEnemyTimer > 5)
					createEnemyTimer -= 5;
			}
		}
	}
	

	public void loadLevel1Objects() {

		Thread loadingThread = new Thread() {
			@Override
			public void run() {
				initLevel1Objects();
			}

			public void initLevel1Objects() {

				if (!gv.continuedGame) {
					// start as new radical.appwards.colorcatch.game if not continuing
					gv.score = 0;
					gv.gameTimer = 0;
					gv.health = gv.HEALTH;
				}

				gv.continuedGame = false;

				gv.level = 1;
				gv.enemyCount = 0;
				gv.levelEnemies = gv.maxEnemies / 2;

				gv.playerW = gv.screenW / 5;
				gv.playerH = gv.screenW / 5;

				gv.enemyW = gv.playerW / 2;
				gv.enemyH = gv.playerW / 2;

				gv.enemyArray = new Enemy[gv.maxEnemies];

				gv.moveSD = new MovableStraightDownImpl[gv.maxEnemies];
				gv.moveLD = new MovableLeftDownImpl[gv.maxEnemies];
				gv.moveRD = new MovableRightDownImpl[gv.maxEnemies];
				gv.moveL = new MovableLeftImpl[gv.maxEnemies];
				gv.moveR = new MovableRightImpl[gv.maxEnemies];
				gv.movePath = new MovablePathImpl[gv.maxEnemies];

				for (int i = 0; i < gv.maxEnemies; i++) {
					// create all the movables a head of time
					gv.moveSD[i] = (MovableStraightDownImpl) gv.moveImplFact
							.createMovableImpl("down", 0);
					gv.moveLD[i] = (MovableLeftDownImpl) gv.moveImplFact
							.createMovableImpl("leftdown", 0);
					gv.moveRD[i] = (MovableRightDownImpl)gv.moveImplFact
							.createMovableImpl("rightdown", 0);
					gv.moveR[i] = (MovableRightImpl) gv.moveImplFact
							.createMovableImpl("right", 0);
					gv.moveL[i] = (MovableLeftImpl) gv.moveImplFact
							.createMovableImpl("left", 0);
					gv.movePath[i] = (MovablePathImpl) gv.moveImplFact
							.createMovableImpl("path", 0);

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
				}

				gv.player = new Player(0, gv.screenH - gv.playerH, gv.playerW,
						gv.screenH, Color.RED);

				gv.player.setOutlinePaint(gv.getTargetPaint());

				gv.playerShields = new Shield[gv.shieldReserve];
				gv.enemyShields = new Shield[gv.maxEnemies];

				for (int i = 0; i < gv.playerShields.length; i++) {
					gv.playerShields[i] = new Shield();
				}

				for (int i = 0; i < gv.enemyShields.length; i++) {
					gv.enemyShields[i] = new Shield();
				}

				gv.newX = gv.screenW / 2;
				gv.newY = gv.screenH - gv.screenH / 3;

				gv.gameVarsLoaded = true;

			}
		};

		loadingThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
		loadingThread.start();
		gv.gameVarsAreLoading = true;

	}
	
}
