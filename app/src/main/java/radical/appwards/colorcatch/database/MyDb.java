package radical.appwards.colorcatch.database;

import radical.appwards.colorcatch.variables.GameVariables;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * A class to save the high score radical.appwards.colorcatch.database. Currently only the highest score is
 * returned for display but an array list of all scores can be created and
 * returned to form a table of scores.
 * 
 * @author Rick
 * 
 */

public class MyDb {

	private static final String DATABASE_NAME = "highscores.db";
	private static final String HIGH_SCORE_TABLE = "highscore";
	private static final String COLUMN_ID = "ID";
	private static final String COLUMN_SCORE = "SCORE";
	private String highScore;

	public MyDb(Context context, long newScore) {
		GameVariables gv = GameVariables.getInstance();
		SQLiteDatabase scoreDB = context.openOrCreateDatabase(DATABASE_NAME,
				Context.MODE_PRIVATE, null);
		scoreDB.execSQL("CREATE TABLE IF NOT EXISTS " + HIGH_SCORE_TABLE + " ("
				+ COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_SCORE + " INT)");

		// Add the values
		ContentValues values = new ContentValues();
		values.put(COLUMN_SCORE, newScore);
		scoreDB.insert(HIGH_SCORE_TABLE, null, values);

		Cursor c = scoreDB.query(HIGH_SCORE_TABLE,
				new String[] { COLUMN_SCORE }, null, null, null, null,
				COLUMN_SCORE);

		// get the highest score
		c.moveToLast();

		highScore = c.getString(0);

		c.close();

		if (scoreDB.isOpen()) {
			scoreDB.close();
		}
	}

	/**
	 * Called to get the highScore
	 * 
	 * @return The highest score in the radical.appwards.colorcatch.database.
	 */
	public String getHighScore() {
		return highScore;
	}
}
