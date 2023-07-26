package radical.appwards.colorcatch.logic;

import java.util.Random;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

import radical.appwards.colorcatch.audio.Audio;
import radical.appwards.colorcatch.movements.MovableLeftImpl;
import radical.appwards.colorcatch.movements.MovablePathImpl;
import radical.appwards.colorcatch.movements.MovableRightImpl;
import radical.appwards.colorcatch.objects.Enemy;
import radical.appwards.colorcatch.objects.MultipleBlockEnemy;
import radical.appwards.colorcatch.variables.GameVariables;

/**
 * A class to help define the radical.appwards.colorcatch.game radical.appwards.colorcatch.logic such as interactions between radical.appwards.colorcatch.objects.
 * Helps organize how radical.appwards.colorcatch.objects are stored when stores as a grouping of radical.appwards.colorcatch.objects.
 * 
 * @author Rick Perez
 * 
 */

public class GameLogic {

	private GameVariables gv;
	private Random rand;
	private int tempTop, decHealth, acc;
	private static final int HIT = 1, MISS = 2;
	private MultipleBlockEnemy multiBlock;
	private MovablePathImpl pathImpl;

	public GameLogic() {
		multiBlock = new MultipleBlockEnemy();
		gv = GameVariables.getInstance();
		rand = new Random();
		decHealth = 1;
	}

	/**
	 * Used when a point is scored or at new levels.
	 * 
	 * @param c
	 *            The drawable canvas.
	 */
	public void destroyAllEnemies(Canvas c) {
		int tempLeft;
		// destroy all enemies on radical.appwards.colorcatch.screen
		for (int i = 0; i < gv.maxEnemies; i++) {

			if (gv.enemyArray[i].getExist()) {
				for (int k = 0; k < gv.enemyShields.length; k++) {
					if (!gv.enemyShields[k].getExists()) {
						gv.enemyShields[k].start(
								gv.enemyArray[i].getRect().left,
								gv.enemyArray[i].getRect().top,
								gv.enemyArray[i].getRect().right,
								gv.enemyArray[i].getRect().bottom,
								gv.enemyArray[i].getPaint());
						break;
					}
				}
			}

			if (gv.enemyArray[i].getMovable() instanceof MovablePathImpl) {
				gv.enemyArray[i].setExist(false);
				pathImpl = (MovablePathImpl) gv.enemyArray[i].getMovable();
				pathImpl.position = 0;
				gv.enemyArray[i].move();
			} else if (gv.enemyArray[i].getMovable() instanceof MovableRightImpl) {
				tempTop = 5 + rand.nextInt(gv.screenH - gv.getEnemyHeight()
						- 10);
				gv.enemyArray[i].destroy(-gv.getEnemyWidth(), tempTop, 0,
						tempTop + gv.getEnemyHeight(),
						gv.enemyArray[i].getSpeed());
			} else {
				tempLeft = rand.nextInt((gv.screenW - gv.getEnemyWidth())
						- (gv.getEnemyWidth()) + 1);
				gv.enemyArray[i].destroy(tempLeft, 0 - gv.getEnemyHeight(),
						tempLeft + gv.getEnemyWidth(), 0,
						gv.enemyArray[i].getSpeed());
			}
		}
		gv.enemyCount = 0;
	}

	/**
	 * Sets the player locations and prevents the player from moving off radical.appwards.colorcatch.screen.
	 */
	public void calculatePlayerLocation() {
		gv.setPlayerLeft(gv.getNewX() - (gv.playerW / 2));
		gv.setPlayerRight(gv.getNewX() + (gv.playerW / 2));
		gv.setPlayerTop(gv.getNewY() - (gv.playerH / 2));
		gv.setPlayerBottom(gv.getNewY() + (gv.playerH / 2));

		if (gv.getPlayerLeft() <= 0) {
			gv.setPlayerLeft(1);
			gv.setPlayerRight(gv.playerW + 1);
		}

		if (gv.getPlayerRight() >= gv.screenW) {
			gv.setPlayerLeft(gv.screenW - gv.playerW - 1);
			gv.setPlayerRight(gv.screenW - 1);
		}

		if (gv.getPlayerTop() <= 0) {
			gv.setPlayerTop(0);
			gv.setPlayerBottom(gv.playerH);
		}

		if (gv.getPlayerBottom() >= gv.screenH) {
			gv.setPlayerTop(gv.screenH - gv.playerH);
			gv.setPlayerBottom(gv.screenH);
		}

		gv.player.move(gv.getPlayerLeft(), gv.getPlayerTop(),
				gv.getPlayerRight(), gv.getPlayerBottom());
	}

	/**
	 * If an enemy exists we should move it to its new location.
	 */
	public void moveEnemies() {
		for (int i = 0; i < gv.getLevelEnemies(); i++) {
			if (gv.enemyArray[i].getExist()) {
				gv.enemyArray[i].move();
			}
		}
	}

	/**
	 * Replace enemies at the top of the radical.appwards.colorcatch.screen if they have passed the bottom.
	 * 
	 * @param enemy
	 *            The enemy object to check on.
	 */
	public void replaceEnemyAtTop(Enemy enemy) {
		gv.enemyCount = (gv.getEnemyCount() - 1);
		tempTop = rand.nextInt((gv.screenW - gv.getEnemyWidth())
				- (gv.getEnemyWidth()) + 1);
		enemy.destroy(tempTop, (-1 * gv.getEnemyHeight()),
				tempTop + gv.getEnemyWidth(), 0, enemy.getSpeed());
	}

	/**
	 * Replace enemies at the right side of the radical.appwards.colorcatch.screen if they have passed the
	 * left side.
	 * 
	 * @param enemy
	 *            The enemy object to check on.
	 */
	public void replaceEnemyAtRightSide(Enemy enemy) {
		gv.enemyCount = (gv.getEnemyCount() - 1);
		tempTop = 5 + rand.nextInt(gv.screenH - gv.getEnemyHeight() - 10);
		enemy.destroy(-gv.getEnemyWidth(), tempTop, 0,
				tempTop + gv.getEnemyHeight(), enemy.getSpeed());
	}

	/**
	 * Replace enemies at the left side of the radical.appwards.colorcatch.screen if they have passed the
	 * right side.
	 * 
	 * @param enemy
	 *            The enemy object to check on.
	 */
	public void replaceEnemyAtLeftSide(Enemy enemy) {
		gv.enemyCount = (gv.getEnemyCount() - 1);
		tempTop = 5 + rand.nextInt(gv.screenH - gv.getEnemyHeight() - 10);
		enemy.destroy(gv.screenW, tempTop, gv.screenW + gv.getEnemyWidth(),
				tempTop + gv.getEnemyHeight(), enemy.getSpeed());
	}

	/**
	 * Replace enemies at the left side of the radical.appwards.colorcatch.screen if they have passed the
	 * right side.
	 * 
	 * @param enemy
	 *            The enemy object to check on.
	 */
	public void replaceEnemyAtPathStart(Enemy enemy) {
		gv.enemyCount = (gv.getEnemyCount() - 1);
		enemy.setExist(false);
		pathImpl = (MovablePathImpl) enemy.getMovable();
		pathImpl.position = 0;
		pathImpl.move();
	}

	/**
	 * Updates if an enemy has fallen off the radical.appwards.colorcatch.screen.
	 */
	public void updateEnemies() {
		// enemy may be off radical.appwards.colorcatch.screen or on the wall
		for (int i = 0; i < gv.getLevelEnemies(); i++) {
			if (gv.enemyArray[i].getExist()) {
				if (gv.enemyArray[i].getRect().top > gv.screenH) {
					replaceEnemyAtTop(gv.enemyArray[i]);
				}

				if (gv.enemyArray[i].getRect().left < 0
						|| gv.enemyArray[i].getRect().right > gv.screenW) {
					gv.enemyArray[i].changeLateralDirection();
				}
			}
		}
	}

	/**
	 * Updates if an enemy has moved off side of radical.appwards.colorcatch.screen
	 */
	public void updateEnemies7() {
		int newSide;
		// enemy may be off radical.appwards.colorcatch.screen, if so set to other side at random location
		for (int i = 0; i < gv.getLevelEnemies(); i++) {
			if (gv.enemyArray[i].getExist()) {
				if (gv.enemyArray[i].getMovable() instanceof MovableRightImpl) {
					// right moving check for off right side of radical.appwards.colorcatch.screen
					if (gv.enemyArray[i].getRect().left > gv.screenW) {
						// get new height for lateral moving enemies
						newSide = 5 + rand.nextInt(gv.screenH
								- gv.getEnemyHeight() - 10);
						gv.enemyArray[i].setNewSideLocation(
								-gv.getEnemyWidth(), newSide, 0,
								newSide + gv.getEnemyHeight());
					}
				} else {
					// left moving check for off left side of radical.appwards.colorcatch.screen
					if (gv.enemyArray[i].getRect().right < 0) {
						// get new height for lateral moving enemies
						newSide = 5 + rand.nextInt(gv.screenH
								- gv.getEnemyHeight() - 10);
						gv.enemyArray[i].setNewSideLocation(gv.screenW,
								newSide, gv.screenW + gv.getEnemyWidth(),
								newSide + gv.getEnemyHeight());
					}
				}
			}
		}
	}

	/**
	 * Update of enemies for radical.appwards.colorcatch.level 8
	 */
	public void updateEnemies8() {
		// enemy may be off radical.appwards.colorcatch.screen, if so set to other side at random location
		for (int i = 0; i < gv.getLevelEnemies(); i++) {
			if (gv.enemyArray[i].getExist()) {
				if (gv.enemyArray[i].getMovable() instanceof MovablePathImpl) {
					// right moving check for off right side of radical.appwards.colorcatch.screen
					if (gv.enemyArray[i].getRect().left > gv.screenW) {
						pathImpl = (MovablePathImpl) gv.enemyArray[i]
								.getMovable();
						pathImpl.position = 0;
					}
					// check for off of left side of radical.appwards.colorcatch.screen
					else if (gv.enemyArray[i].getRect().left < 0) {
						// get new height for lateral moving enemies
						pathImpl = (MovablePathImpl) gv.enemyArray[i]
								.getMovable();
						pathImpl.position = 0;
					}
					// check for off of top side of radical.appwards.colorcatch.screen
					else if (gv.enemyArray[i].getRect().bottom < 0) {
						// get new height for lateral moving enemies
						pathImpl = (MovablePathImpl) gv.enemyArray[i]
								.getMovable();
						pathImpl.position = 0;
					}
					// check for off of bottom side of radical.appwards.colorcatch.screen
					else if (gv.enemyArray[i].getRect().top > gv.screenH) {
						// get new height for lateral moving enemies
						pathImpl = (MovablePathImpl) gv.enemyArray[i]
								.getMovable();
						pathImpl.position = 0;
					}
				}
			}
		}
	}

	/**
	 * Updates the multi block enemies to be off radical.appwards.colorcatch.screen. Or keep them on radical.appwards.colorcatch.screen
	 * laterally.
	 */
	public void updateMultiBlockEnemies() { // enemy may be off radical.appwards.colorcatch.screen or on the
											// wall
		for (int i = 0; i < gv.getLevelEnemies(); i++) {
			if (gv.enemyArray[i].getExist()) {
				if (gv.enemyArray[i].getRect().top > gv.screenH) {
					gv.enemyArray[i].setExist(false);
					gv.enemyCount = (gv.getEnemyCount() - 1);
				}

				if (gv.enemyArray[i].getRect().left < 0
						|| gv.enemyArray[i].getRect().right > gv.screenW) {
					gv.enemyArray[i].changeLateralDirection();
				}
			}
		}
	}

	/**
	 * Checks the collision of an enemy witht the player. Method also sets a
	 * flag to update the radical.appwards.colorcatch.background to the color currently mixed.
	 */
	public void checkForHits(Context context) {
		// for every enemy
		gv.setNewMixedColorInt = false;
		for (int j = 0; j < gv.getLevelEnemies(); j++) {
			if (gv.enemyArray[j].getExist()) {

				// check for enemy hitting player
				if (Rect.intersects(gv.player.getRect(),
						gv.enemyArray[j].getRect())) {

					gv.mixedColorInt = gv.player.addPaint(gv.enemyArray[j]
							.getPaint());
					gv.setNewMixedColorInt = true;

					// start player shield
					for (int k = 0; k < gv.getPlayerShieldsLen(); k++) {
						if (!gv.getPlayerShields(k).getExists()) {
							gv.getPlayerShields(k).start(
									gv.player.getRect().left,
									gv.player.getRect().top,
									gv.player.getRect().right,
									gv.player.getRect().bottom);
							break;
						}
					}

					// start enemy shield
					for (int k = 0; k < gv.enemyShields.length; k++) {
						if (!gv.getEnemyShields(k).getExists()) {
							gv.getEnemyShields(k).start(
									gv.enemyArray[j].getRect().left,
									gv.enemyArray[j].getRect().top,
									gv.enemyArray[j].getRect().right,
									gv.enemyArray[j].getRect().bottom,
									gv.enemyArray[j].getPaint());
							break;
						}
					}

					// if block is not target color decrement score
					if (gv.getTargetPaint().getColor() != gv.enemyArray[j]
							.getPaint().getColor()) {
						gv.setHealth(gv.getHealth() - decHealth);
						Audio.getInstance().playSound(context, MISS);
					} else {
						Audio.getInstance().playSound(context, HIT);
					}

					if (gv.enemyArray[j].getMovable() instanceof MovableRightImpl)
						replaceEnemyAtRightSide(gv.enemyArray[j]);
					else if (gv.enemyArray[j].getMovable() instanceof MovableLeftImpl)
						replaceEnemyAtLeftSide(gv.enemyArray[j]);
					else if (gv.enemyArray[j].getMovable() instanceof MovablePathImpl)
						replaceEnemyAtPathStart(gv.enemyArray[j]);
					else
						replaceEnemyAtTop(gv.enemyArray[j]);

				}
			}
		}
	}

	/**
	 * Collision detection for a multiblock of enemies.
	 */
	public void checkForHitsFiveBlock(Context context) {

		gv.setNewMixedColorInt = false;

		for (int j = 0; j < gv.getLevelEnemies(); j++) {
			if (gv.enemyArray[j].getExist()) {

				// check for enemy hitting player
				if (Rect.intersects(gv.player.getRect(),
						gv.enemyArray[j].getRect())) {

					// gv.player.addPaint(gv.enemyArray[j].getPaint());

					gv.mixedColorInt = gv.player.addPaint(gv.enemyArray[j]
							.getPaint());
					gv.setNewMixedColorInt = true;

					// start player shield
					for (int k = 0; k < gv.getPlayerShieldsLen(); k++) {
						if (!gv.getPlayerShields(k).getExists()) {
							gv.getPlayerShields(k).start(
									gv.player.getRect().left,
									gv.player.getRect().top,
									gv.player.getRect().right,
									gv.player.getRect().bottom);
							break;
						}
					}

					// start enemy shield
					for (int k = 0; k < gv.enemyShields.length; k++) {
						if (!gv.getEnemyShields(k).getExists()) {
							gv.getEnemyShields(k).start(
									gv.enemyArray[j].getRect().left,
									gv.enemyArray[j].getRect().top,
									gv.enemyArray[j].getRect().right,
									gv.enemyArray[j].getRect().bottom,
									gv.enemyArray[j].getPaint());
							break;
						}
					}

					// if block is not target color decrement score
					if (gv.getTargetPaint().getColor() != gv.enemyArray[j]
							.getPaint().getColor()) {
						gv.setHealth(gv.getHealth() - decHealth);
						Audio.getInstance().playSound(context, MISS);
					} else {
						Audio.getInstance().playSound(context, HIT);
					}

					gv.enemyArray[j].setExist(false);
					gv.enemyCount = (gv.getEnemyCount() - 1);

				}
			}
		}
	}

	public void updatePhysics(Context context) {
		calculatePlayerLocation();
		moveEnemies();
		updateEnemies();
		checkForHits(context);
	}

	public void updatePhysics7(Context context) {
		calculatePlayerLocation();
		moveEnemies();
		updateEnemies7();
		checkForHits(context);
	}

	public void updatePhysics8(Context context) {
		// physics update for radical.appwards.colorcatch.level 8
		calculatePlayerLocation();
		moveEnemies();
		updateEnemies8();
		checkForHits(context);
	}

	public void updatePhysics9(Context context) {
		// physics update for radical.appwards.colorcatch.level 9
		calculatePlayerLocation();
		moveEnemies();
		updateEnemies9();
		checkForHits(context);
	}

	public void updateMultiBlockPhysics(Context context) {
		calculatePlayerLocation();
		moveEnemies();
		updateMultiBlockEnemies();
		checkForHitsFiveBlock(context);
	}

	/**
	 * Check for an enemy to exist or create a new one.
	 */
	public void createSingleEnemyBlock() {
		if (gv.getEnemyCount() < gv.getLevelEnemies()) {
			for (int i = 0; i < gv.getLevelEnemies(); i++) {
				if (!gv.enemyArray[i].getExist()) {
					gv.enemyArray[i].setExist(true);
					gv.incEnemyCount();
					break;
				}
			}
		}
	}

	/**
	 * Check for the first enemy not used and give it new paint as well as set
	 * exist to be true
	 */
	public void createSingleEnemyBlockWithNewRandomPaint() {
		if (gv.getEnemyCount() < gv.getLevelEnemies()) {
			for (int i = 0; i < gv.getLevelEnemies(); i++) {
				if (!gv.enemyArray[i].getExist()) {
					gv.enemyArray[i].setExist(true);
					gv.enemyArray[i].setPaint(gv.getRandomPaint());
					gv.incEnemyCount();
					break;
				}
			}
		}
	}

	/**
	 * Cycle to new colors if needed.
	 */
	public void newTargetColor() {
		if (gv.getTargetPaint().getColor() == Color.RED) {
			switch (rand.nextInt(2)) {
			case 0:
				gv.setTargetPaint(Color.BLUE);
				break;
			case 1:
				gv.setTargetPaint(Color.GREEN);
				break;
			}
		} else if (gv.getTargetPaint().getColor() == Color.BLUE) {
			switch (rand.nextInt(2)) {
			case 0:
				gv.setTargetPaint(Color.RED);
				break;
			case 1:
				gv.setTargetPaint(Color.GREEN);
				break;
			}
		} else if (gv.getTargetPaint().getColor() == Color.GREEN) {
			switch (rand.nextInt(2)) {
			case 0:
				gv.setTargetPaint(Color.RED);
				break;
			case 1:
				gv.setTargetPaint(Color.BLUE);
				break;
			}
		}

		gv.player.setOutlinePaint(gv.getTargetPaint());
	}

	public void createMultiEnemyBlock() {
		if (gv.getEnemyCount() == 0) {
			multiBlock.setSplit(false);
			multiBlock.clearEnemies();
			acc = 0;
			tempTop = rand.nextInt((gv.screenW - gv.getEnemyWidth())
					- (gv.getEnemyWidth()) + 1);
			for (int i = 0; i < gv.getLevelEnemies(); i++) {
				if (!gv.enemyArray[i].getExist()) {
					gv.enemyArray[i].destroy(tempTop,
							(-1 * gv.getEnemyHeight()),
							tempTop + gv.getEnemyWidth(), 0,
							gv.getSpeedModifier());
					gv.enemyArray[i].setExist(true);
					multiBlock.addEnemy(gv.enemyArray[i]);
					gv.incEnemyCount();
					acc++;
				}
			}
		}
	}

	/**
	 * Split a block of enemies that appear as one.
	 */
	public void splitMultiBlock() {
		if (multiBlock.isSplit()) {
		} else {
			multiBlock.setSplit(true);
			acc = 0;
			for (Enemy e : multiBlock.getEnemies()) {
				switch (acc) {
				case 0:
					e.setMovable(gv.getRMovable(acc));
					break;
				case 1:
					e.setMovable(gv.getRMovable(acc));
					e.incSpeed();
					break;
				case 2:
					e.setMovable(gv.getRMovable(acc));
					e.incSpeed();
					e.incSpeed();
					break;
				case 3:
					e.setMovable(gv.getLMovable(acc));
					break;
				case 4:
					e.setMovable(gv.getLMovable(acc));
					e.incSpeed();
					break;
				case 5:
					e.setMovable(gv.getLMovable(acc));
					e.incSpeed();
					e.incSpeed();
					break;
				case 6:
					break;
				case 7:
					e.incSpeed();
					break;
				case 8:
					e.incSpeed();
					e.incSpeed();
					break;
				}
				acc++;
			}
		}
	}

	/**
	 * Set a new movable routine for a block that mixes it up from time to time.
	 */
	public void getNewMovables() {
		for (int i = 0; i < gv.getLevelEnemies(); i++) {
			if (gv.enemyArray[i].getExist()) {
				gv.enemyArray[i].setMovable(gv.getRandomMovable(i));
			}
		}
	}

	private void updateEnemies9() {
		updateEnemies8();
	}
}
