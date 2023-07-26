package radical.appwards.colorcatch.logic;

import android.graphics.Canvas;

import radical.appwards.colorcatch.variables.GameVariables;

/**
 * A class that helps define different radical.appwards.colorcatch.game draw radical.appwards.colorcatch.logic at different levels and
 * at end radical.appwards.colorcatch.game. Also draws the player, enemy and shield radical.appwards.colorcatch.objects.
 * 
 * @author Rick
 * 
 */

public class GameDraw {

	private GameVariables gv;

	public GameDraw() {
		gv = GameVariables.getInstance();
	}

	/**
	 * Every on touch call at end radical.appwards.colorcatch.game draws a new color square as if drawing
	 * with squares.
	 * 
	 * @param canvas
	 *            The object the squares are drawn on.
	 */
	public void myEndDraw(Canvas canvas) {
		// for each x and y draw a square of a color
		for (int i = 0; i < gv.getEnemyCount(); i++) {
			switch (i % 4) {
			case 0:
				canvas.drawRect(gv.getXs(i), gv.getYs(i),
						gv.getXs(i) + gv.getEnemyWidth(),
						gv.getYs(i) + gv.getEnemyWidth(), gv.getGreenPaint());
				break;
			case 1:
				canvas.drawRect(gv.getXs(i), gv.getYs(i),
						gv.getXs(i) + gv.getEnemyWidth(),
						gv.getYs(i) + gv.getEnemyWidth(), gv.getRedPaint());
				break;
			case 2:
				canvas.drawRect(gv.getXs(i), gv.getYs(i),
						gv.getXs(i) + gv.getEnemyWidth(),
						gv.getYs(i) + gv.getEnemyWidth(), gv.getBluePaint());
				break;
			case 3:
				canvas.drawRect(gv.getXs(i), gv.getYs(i),
						gv.getXs(i) + gv.getEnemyWidth(),
						gv.getYs(i) + gv.getEnemyWidth(), gv.getYellowPaint());
				break;
			}
		}
	}

	public void myDraw(Canvas canvas) {
		gv.player.draw(canvas);
		drawEnemies(canvas);
		drawPlayerShields(canvas);
		drawEnemyShields(canvas);
	}

	public void drawEnemies(Canvas canvas) {
		for (int i = 0; i < gv.getLevelEnemies(); i++) {
			if (gv.enemyArray[i].getExist()) {
				gv.enemyArray[i].draw(canvas);
			}
		}
	}

	public void drawPlayerShields(Canvas canvas) {
		for (int i = 0; i < gv.getPlayerShieldsLen(); i++) {
			if (gv.getPlayerShields(i).getExists()) {
				gv.getPlayerShields(i).update(canvas, gv.getPlayerLeft(),
						gv.getPlayerTop(), gv.getPlayerRight(),
						gv.getPlayerBottom(), gv.player.getPaint());
			}
		}
	}

	public void drawEnemyShields(Canvas canvas) {
		for (int i = 0; i < gv.enemyShields.length; i++) {
			if (gv.getEnemyShields(i).getExists()) {
				gv.getEnemyShields(i).update(canvas);
			}
		}
	}
}
