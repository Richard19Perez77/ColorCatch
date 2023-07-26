package radical.appwards.colorcatch.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class that contains a number of squares either hidden as one or to make a
 * larger object.
 * 
 * @author Rick
 * 
 */

public class MultipleBlockEnemy implements Serializable {

	private static final long serialVersionUID = 1L;

	private ArrayList<Enemy> enemies;
	/**
	 * Used to tell if the object is visibly split into different movement
	 * patterns.
	 */
	private boolean split;

	public MultipleBlockEnemy() {
		enemies = new ArrayList<>();
	}

	public void addEnemy(Enemy e) {
		enemies.add(e);
	}

	public void clearEnemies() {
		enemies.clear();
	}

	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public boolean isSplit() {
		return split;
	}

	public void setSplit(boolean s) {
		split = s;
	}

}
