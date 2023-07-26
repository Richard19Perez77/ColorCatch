package radical.appwards.colorcatch.database;

import radical.appwards.colorcatch.audio.Audio;
import radical.appwards.colorcatch.variables.GameVariables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Called to store the radical.appwards.colorcatch.game state, currently doesn't store all radical.appwards.colorcatch.objects just
 * player data, place and health.
 * 
 * @author Rick
 * 
 */

public class SaveStateDb {

	private SQLiteDatabase stateDB;

	private static final String DATABASE_NAME = "state.db";
	private static final String STATE_TABLE = "state";

	private static final String COLUMN_ID = "ID";
	private static final String COLUMN_LEVEL = "LEVEL";
	private static final String COLUMN_HEALTH = "HEALTH";
	private static final String COLUMN_ENEMYCOUNT = "ENEMYCOUNT";
	private static final String COLUMN_GAMETIMER = "GAMETIMER";
	private static final String COLUMN_SOUNDSTOPPED = "SOUNDSTOPPED";
	private static final String COLUMN_SCORE = "SCORE";

	GameVariables gv;

	public SaveStateDb() {

	}

	public void saveState(Context context) {
		// Add the values

		gv = GameVariables.getInstance();

		try {

			if (gv.getGamePlaying()) {
				// if in radical.appwards.colorcatch.game then save state
				stateDB = context.openOrCreateDatabase(DATABASE_NAME,
						Context.MODE_PRIVATE, null);

				stateDB.execSQL("CREATE TABLE IF NOT EXISTS " + STATE_TABLE
						+ " (" + COLUMN_ID + " INTEGER PRIMARY KEY, "
						+ COLUMN_LEVEL + " INTEGER," + COLUMN_HEALTH
						+ " INTEGER," + COLUMN_ENEMYCOUNT + " INTEGER,"
						+ COLUMN_GAMETIMER + " INTEGER," + COLUMN_SOUNDSTOPPED
						+ " INTEGER," + COLUMN_SCORE + " INTEGER" + ")");

				ContentValues values = new ContentValues();

				values.put(COLUMN_LEVEL, gv.level);
				values.put(COLUMN_HEALTH, gv.getHealth());
				values.put(COLUMN_ENEMYCOUNT, gv.getEnemyCount());
				values.put(COLUMN_GAMETIMER, gv.gameTimer);
				values.put(COLUMN_SOUNDSTOPPED, Audio.getInstance().stoppedAt);
				values.put(COLUMN_SCORE, gv.score);

				stateDB.insert(STATE_TABLE, null, values);
			}
		} catch (Exception e) {
			// if exception occurred in saving delete faulty db
			delete();
		} finally {
			if (stateDB.isOpen()) {
				stateDB.close();
			}
		}
	}

	public boolean getState(Context context) throws  ClassNotFoundException {

		gv = GameVariables.getInstance();

		try {

			stateDB = context.openOrCreateDatabase(DATABASE_NAME,
					SQLiteDatabase.CREATE_IF_NECESSARY, null);

			stateDB.execSQL("CREATE TABLE IF NOT EXISTS " + STATE_TABLE + " ("
					+ COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_LEVEL
					+ " INTEGER," + COLUMN_HEALTH + " INTEGER,"
					+ COLUMN_ENEMYCOUNT + " INTEGER," + COLUMN_GAMETIMER
					+ " INTEGER," + COLUMN_SOUNDSTOPPED + " INTEGER,"
					+ COLUMN_SCORE + " INTEGER" + ")");

			Cursor c = stateDB.query(STATE_TABLE, null, null, null, null, null,
					null);

			if (c.moveToLast()) {

				// radical.appwards.colorcatch.game will restart with old radical.appwards.colorcatch.objects
				gv.level = c.getInt(1);
				gv.setHealth(c.getInt(2));
				gv.enemyCount = c.getInt(3);
				gv.gameTimer = c.getLong(4);
				Audio.getInstance().stoppedAt = c.getInt(5);
				gv.score = c.getLong(6);

				// used to tell the radical.appwards.colorcatch.game at points to use the next set of
				// radical.appwards.colorcatch.objects
				// and not recreate new ones
				gv.continuedGame = true;
			}

			c.close();

			delete();

		} catch (Exception e) {
			// if error out delete bad db... no outer join
			delete();
		} finally {
			if (stateDB.isOpen()) {
				stateDB.close();
			}
		}
		return true;
	}

	/**
	 * There should only be one save state. If the player closes the app in the
	 * menu or radical.appwards.colorcatch.game over radical.appwards.colorcatch.screen there shouldn't be the previous record to
	 * re-reload.
	 */
	public void delete() {
		// delete previous save record now that it is reloaded
		stateDB.execSQL("DROP TABLE IF EXISTS " + STATE_TABLE);
	}
}
