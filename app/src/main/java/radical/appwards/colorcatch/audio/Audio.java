package radical.appwards.colorcatch.audio;

import java.io.IOException;

import radical.appwards.colorcatch.R;
import radical.appwards.colorcatch.variables.GameVariables;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.util.SparseIntArray;
import android.widget.Toast;

/**
 * A class that will run the radical.appwards.colorcatch.logic for the sounds in the radical.appwards.colorcatch.game. I made this a
 * static class so that the radical.appwards.colorcatch.audio can be changed and recored anywhere in the
 * app. I also use it to get the app context anywhere which is very handy when
 * in classes not from the main activity.
 *
 * @author Rick
 */

public class Audio implements MediaPlayer.OnPreparedListener {

    // create one var of the type it is.
    private volatile static Audio instance;
    public MediaPlayer mp;
    public SoundPool sp;
    private float volume;
    private float fSpeed;
    private float prevVolume = 1;
    private float currVolume = 1;
    private SparseIntArray soundsMap;
    private AudioManager mgr;
    private static final int HIT = 1, MISS = 2, POINT = 3;
    public int stoppedAt;
    private boolean musicOn = false, soundOn = false, musicResumed;
    private Uri path;
    public int level;
    private boolean resuming;

    /**
     * Singleton radical.appwards.colorcatch.objects needs private constructor that will help ensure the
     * class is created outside of the class.
     */
    private Audio() {
    }

    /**
     * Returns the one object instance that is created on this first call
     *
     * @return The only Audio instance.
     */
    public static Audio getInstance() {
        if (instance == null)
            synchronized (Audio.class) {
                if (instance == null)
                    instance = new Audio();
            }
        return instance;
    }

    public void setSoundStopped() {
        stoppedAt = mp.getCurrentPosition();
    }

    /**
     * musicResumed is used to flag when resuming the radical.appwards.colorcatch.game so it doesn't start
     * the track over at radical.appwards.colorcatch.game continue.
     */
    public void playMenu(Context context) {
        if (!musicResumed) {
            try {
                path = Uri
                        .parse("android.resource://radical.appwards.colorcatch/"
                                + R.raw.intro);
                mp.reset();
                mp.setDataSource(context, path);
                mp.setOnPreparedListener(this);
                mp.setLooping(true);
                mp.setVolume(currVolume, currVolume);
                mp.setOnPreparedListener(this);
                mp.prepareAsync();
            } catch (IllegalStateException | IllegalArgumentException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Called to toggle music on/off.
     *
     * @param context The application context.
     */
    public void toggleMusic(Context context) {
        if (musicOn) {
            musicOn = false;
            prevVolume = currVolume;
            currVolume = 0;
            mp.setVolume(currVolume, currVolume);
            Toast.makeText(context, "Music OFF", Toast.LENGTH_SHORT).show();
        } else {
            musicOn = true;
            currVolume = prevVolume;
            mp.setVolume(currVolume, currVolume);
            Toast.makeText(context, "Music ON", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Used to toggle sound effects on/off.
     */
    public void toggleSound() {
        //if its already ! it turns true, if true it turns !
        soundOn = !soundOn;
    }

    /**
     * When the app is resumed the radical.appwards.colorcatch.level is used to tell what track to start and
     * the soundStopped at should be set from the radical.appwards.colorcatch.database.
     */
    private void resumeMusic(Context context) {

        level = GameVariables.getInstance().level;

        if (level <= GameVariables.LEVELS / 2)
            path = Uri.parse("android.resource://radical.appwards.colorcatch/"
                    + R.raw.track1);
        else
            path = Uri.parse("android.resource://radical.appwards.colorcatch/"
                    + R.raw.track2);

        try {
            mp.reset();
            mp.setDataSource(context, path);
            mp.setOnPreparedListener(this);
            mp.setLooping(true);
            mp.setVolume(1, 1);
            resuming = true;
            mp.setOnPreparedListener(this);
            mp.prepareAsync();
            musicResumed = true;
        } catch (IllegalStateException | IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to load and start the media player. If not done here it will be done
     * on the main radical.appwards.colorcatch.game thread and it will cause about a 1 second hiccup.
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        if (resuming)
            mp.seekTo(stoppedAt);
        resuming = false;
        if (musicOn)
            mp.start();
    }

    /**
     * If the radical.appwards.colorcatch.game is not resumed from a closed app instance it will play the
     * track from the beginning.
     */
    public void playGame(Context context) {
        if (!musicResumed) {
            try {
                path = Uri
                        .parse("android.resource://radical.appwards.colorcatch/"
                                + R.raw.track1);
                mp.reset();
                mp.setDataSource(context, path);
                mp.setOnPreparedListener(this);
                mp.setLooping(true);
                mp.setVolume(currVolume, currVolume);
                mp.setOnPreparedListener(this);
                mp.prepareAsync();
            } catch (IllegalStateException | IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        musicResumed = false;
    }

    /**
     * Played at the middle of the radical.appwards.colorcatch.game.
     */
    public void playTrack2(Context context) {
        try {
            path = Uri.parse("android.resource://radical.appwards.colorcatch/"
                    + R.raw.track2);
            mp.reset();
            mp.setDataSource(context, path);
            mp.setOnPreparedListener(this);
            mp.setLooping(true);
            mp.setVolume(currVolume, currVolume);
            mp.setOnPreparedListener(this);
            mp.prepareAsync();
        } catch (IllegalStateException | IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Played in the ending score radical.appwards.colorcatch.screen.
     */
    public void playEndTrack(Context context) {
        try {
            path = Uri.parse("android.resource://radical.appwards.colorcatch/"
                    + R.raw.ending);
            mp.reset();
            mp.setDataSource(context, path);
            mp.setOnPreparedListener(this);
            mp.setLooping(true);
            mp.setVolume(currVolume, currVolume);
            mp.setOnPreparedListener(this);
            mp.prepareAsync();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the sound at the appropriate volume relative to the current phone
     * volume.
     *
     * @param sound The mapping of the sound to differentiate what to play.
     */
    public void playSound(Context context, int sound) {
        // plays the sounds effect called
        mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (soundOn) {
            volume = streamVolumeCurrent / streamVolumeMax;
            volume /= 2;
        } else
            volume = 0;
        sp.play(soundsMap.get(sound), volume, volume, 1, 0, fSpeed);
    }

    /**
     * Release memory associated with sound pool.
     */
    public void releaseSoundPool() {
        if (sp != null) {
            sp.release();
            sp = null;
        }
    }

    public void resumeAudio(Context context) {
        AudioManager audioManager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actualVolume / maxVolume;

        fSpeed = 1.0f;
        soundsMap = new SparseIntArray();
        soundsMap.put(HIT, sp.load(context, R.raw.hit, 1));
        soundsMap.put(MISS, sp.load(context, R.raw.miss, 1));
        soundsMap.put(POINT, sp.load(context, R.raw.point, 1));

        if (stoppedAt > 0) {
            resumeMusic(context);
        }
    }
}