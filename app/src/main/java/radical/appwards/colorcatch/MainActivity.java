package radical.appwards.colorcatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import radical.appwards.colorcatch.audio.Audio;
import radical.appwards.colorcatch.database.SaveStateDb;
import radical.appwards.colorcatch.variables.GameVariables;

public class MainActivity extends AppCompatActivity {

    private static final int MENU_PAUSE = 0;
    private static final int MENU_RESUME = 1;
    private static final int MENU_RESTART = 2;
    private static final int MUSIC_TOGGLE = 3;
    private static final int SOUND_TOGGLE = 4;
    private static final int MUSIC_LINK = 5;

    String music = "http://pinklogik.bandcamp.com/";

    /**
     * The radical.appwards.colorcatch.game thread that will call update and draw methods.
     */
    private ColorCatchView.GameThread gameThread;

    /**
     * A handle to the View that holds the surface that will be drawn on.
     */
    private ColorCatchView colorCatchView;

    Audio audio = Audio.getInstance();

    /**
     * Saving the state of the application in a radical.appwards.colorcatch.database enables recreation of
     * the state at the time of the applications closing
     */
    private SaveStateDb stateDb;

    /**
     * Called a the start of the application and carries a saved instance state,
     * this state is only short lived though.
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        // create new sound radical.appwards.colorcatch.objects
        audio.mp = new MediaPlayer();
        audio.sp = new SoundPool(20, AudioManager.STREAM_MUSIC, 100);
        // create a new view and thread that will run the draw and update
        // physics methods.
        colorCatchView = (ColorCatchView) findViewById(R.id.color);
        gameThread = colorCatchView.getThread();
        colorCatchView.setTextView((TextView) findViewById(R.id.text));
        colorCatchView.setButtonView((Button) findViewById(R.id.begin));

        stateDb = new SaveStateDb();

        gameThread.setState(ColorCatchView.GameThread.STATE_READY);
    }

    /**
     * Called when the start button in the radical.appwards.colorcatch.game menu is pressed.
     *
     * @param v The view that the button lives in at the time of being
     *          pressed. Views are used to contruct the interface shown to the
     *          user.
     */
    public void startGame(View v) {
        colorCatchView.startGame();
    }

    /**
     * Creates the menu items on the cell phone. This is helpful in full radical.appwards.colorcatch.screen
     * graphics applications like this where the entire view is being drawn on
     * with a canvas. Newer Android versions like JellyBean are using tab and
     * swipe and encouraging not using a hidden menu like this. But for full
     * radical.appwards.colorcatch.screen games its stil handy.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.add(0, MUSIC_LINK, 0, R.string.music_link);
        menu.add(0, MUSIC_TOGGLE, 0, R.string.music_toggle);
        menu.add(0, SOUND_TOGGLE, 0, R.string.sound_toggle);
        menu.add(0, MENU_RESUME, 0, R.string.menu_resume);
        menu.add(0, MENU_RESTART, 0, R.string.menu_menu);
        menu.add(0, MENU_PAUSE, 0, R.string.menu_pause);
        return true;
    }

    /**
     * For every item added in the menu there should be something it does. I use
     * it to return to the menu and restart the radical.appwards.colorcatch.game or to pause the radical.appwards.colorcatch.game or to
     * adjust the sound settings with seperate options to toggle music and/or
     * sound.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RESTART:
                gameThread.returnToMenu();
                return true;
            case MENU_PAUSE:
                gameThread.pause();
                return true;
            case MENU_RESUME:
                gameThread.unPause();
                return true;
            case MUSIC_TOGGLE:
                audio.toggleMusic(getApplicationContext());
                return true;
            case SOUND_TOGGLE:
                audio.toggleSound();
                return true;
            case MUSIC_LINK:
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(music));
                startActivity(myIntent);
                return true;
        }
        return false;
    }

    /**
     * Called any time the application closes, whether destroyed or paused.
     */
    @Override
    protected void onPause() {
        super.onPause();

        colorCatchView.pause();

        if (audio.mp != null) {
            if (audio.mp.isPlaying()) {
                audio.mp.pause();
                audio.setSoundStopped();
            }
        }

        if (GameVariables.ACCESS_SAVE_DB)
            stateDb.saveState(getApplicationContext());
    }

    /**
     * Called after re-establishing the application and its ready to go again.
     * This is actually the last thing the app runs before continuing running
     * the app, not the first thing when returning.
     */
    @Override
    protected void onResume() {
        super.onResume();

        try {
            if (GameVariables.ACCESS_SAVE_DB)
                stateDb.getState(getApplicationContext());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        audio.resumeAudio(getApplicationContext());
    }

    /**
     * When the application is destroyed it might still live in memory but when
     * re-opened it will always re start from the beginning of the application
     * as a new instance. Memory intensive resources should be released so they
     * don't waste the phone's battery.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        colorCatchView.pause();

        if (audio.mp != null) {
            if (audio.mp.isPlaying())
                audio.mp.stop();
            audio.mp.release();
            audio.mp = null;
        }

        if (audio.sp != null)
            audio.releaseSoundPool();

        // reset the last place the radical.appwards.colorcatch.audio stopped to start of track
        audio.stoppedAt = 0;
    }
}
