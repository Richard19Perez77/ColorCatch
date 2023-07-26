package radical.appwards.colorcatch;

import radical.appwards.colorcatch.game.ColorCatchGame;
import radical.appwards.colorcatch.variables.GameVariables;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * The class that creates the view to be seen and the thread to update the view
 * as well as draw the radical.appwards.colorcatch.objects to be shown. Most important for the radical.appwards.colorcatch.game radical.appwards.colorcatch.logic is
 * the doDraw and updatePhysics methods for the Game, as well as the onTouch
 * event handling.
 *
 * @author Rick Perez
 */
public class ColorCatchView extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * Used to display messages to the user when the application is paused.
     */
    private TextView mStatusText;
    /**
     * Used to start the application and is usually hidden during gameplay.
     */
    private Button mStartButton;
    /**
     * Create to run the draw and update methods.
     */
    private GameThread thread;

    /**
     * The radical.appwards.colorcatch.game radical.appwards.colorcatch.logic of Color Catch.
     */
    private ColorCatchGame colorCatch;

    /***
     * allows for saving of radical.appwards.colorcatch.variables across classes
     */
    GameVariables gv;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message m) {
            int viz = m.getData().getInt("viz");
            if (viz == 0)
                mStatusText.setVisibility(View.VISIBLE);
            else
                mStatusText.setVisibility(View.INVISIBLE);

            mStatusText.setText(m.getData().getString("text"));

            return true;
        }
    });

    /**
     * Creates the View the user will see.
     *
     * @param context Contains the state of the radical.appwards.colorcatch.game.
     * @param attrs   Needed for instantiation from xml
     */
    public ColorCatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // before making the radical.appwards.colorcatch.game touch ready I load the intro radical.appwards.colorcatch.screen
        setFocusable(false);

        // basically, allows the canvas to be drawn on
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        gv = GameVariables.getInstance();

        // the start of the app is paused with a message to unPause to start
        thread = new GameThread(holder, context, handler);

        colorCatch = new ColorCatchGame(context);

        // initializes the radical.appwards.colorcatch.screen radical.appwards.colorcatch.variables but they may change is we remove the
        // status bar which i will do but for now its good enough to size the
        // splash radical.appwards.colorcatch.screen. DisplayMetrics contains info about the phone.
        // Resources
        // like strings, draw able and raw data files can be accessed now.
        colorCatch.init(getResources().getDisplayMetrics());

        // now we can begin to accept touch events, but we might need to load
        // other radical.appwards.colorcatch.variables specific to the radical.appwards.colorcatch.game.
        setFocusable(true); // make sure we get key events
    }

    /**
     * Used to get the thread, mostly from the activity class so we can pause
     * and resume the thread when the phone's home or back button is pressed.
     *
     * @return The radical.appwards.colorcatch.game thread.
     */
    public GameThread getThread() {
        return thread;
    }

    /**
     * When the start button is pressed its heard first in the Activity class
     * and then this method is called.
     */
    public void startGame() {
        colorCatch.startGame(getContext());
        removeStartButton();
        thread.unPause();
    }

    public void showStartButton() {
        mStartButton.setVisibility(VISIBLE);
    }

    public void removeStartButton() {
        mStartButton.setVisibility(INVISIBLE);
    }

    /**
     * Random events that close the app or steal the view will call this and
     * pause the radical.appwards.colorcatch.game.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus)
            thread.pause();
    }

    /**
     * A reference to the text box created in the activity class.
     *
     * @param textView The text box to be shown.
     */
    public void setTextView(TextView textView) {
        mStatusText = textView;
    }

    /**
     * A reference to the start button created in the activity class.
     *
     * @param button The button to be shown.
     */
    public void setButtonView(Button button) {
        mStartButton = button;
    }

    /**
     * Callback invoked when the surface dimensions change. I have locked the
     * radical.appwards.colorcatch.game as portrait in the manifest.xml so it won't be used.
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        thread.setSurfaceSize(width, height);
    }

    /**
     * Callback invoked when the Surface has been created and is ready to be
     * used. If the thread is destroyed or is killed for some reason we need to
     * create a new one or the radical.appwards.colorcatch.game will crash. SetRunning allows the code to be
     * run. Starting the thread runs it.
     */
    public void surfaceCreated(SurfaceHolder holder) {
        // start the thread here so that we don't busy-wait in run()
        // waiting for the surface to be created
        if (thread == null || thread.getState() == Thread.State.TERMINATED) {
            thread = new GameThread(holder, getContext(), handler);
        }

        thread.setRunning(true);
        thread.start();
    }

    /**
     * When the application is closing we can end the current thread.
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Pauses the radical.appwards.colorcatch.game thread.
     */
    public void pause() {
        thread.pause();
    }

    public boolean performClick() {
        return super.performClick();
    }

    /**
     * Here is the radical.appwards.colorcatch.screen touch event radical.appwards.colorcatch.logic. The event carried the coordinates
     * and the type of gesture made. I pass this to the color catch radical.appwards.colorcatch.game to
     * handle the events if the radical.appwards.colorcatch.game is started. If its the loading radical.appwards.colorcatch.screen it
     * moves the radical.appwards.colorcatch.game to the menu radical.appwards.colorcatch.screen. If in the menu radical.appwards.colorcatch.screen the only thing
     * else it can do is unPause it and wait for the start button to be pressed.
     * Odd situations where the radical.appwards.colorcatch.game has paused from actual phone events, the
     * radical.appwards.colorcatch.game will unPause.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (getThread().getSurfaceHolder()) {
            performClick();
            if (mStatusText.isShown()) {
                mStatusText.setVisibility(INVISIBLE);

                if (colorCatch.getLoadingPlaying()) {
                    // go from loading radical.appwards.colorcatch.screen to menu radical.appwards.colorcatch.screen
                    colorCatch.setMenuScreen(getContext());
                    showStartButton();
                    thread.unPause();
                } else if (colorCatch.getMenuPlaying()) {
                    // if in menu radical.appwards.colorcatch.screen show start button
                    showStartButton();
                    thread.unPause();
                } else {
                    thread.unPause();
                }

                return false;
            } else if (colorCatch.getGameVarsLoaded()) {
                // store touch point and do some action
                colorCatch.setNewOnTouchCoords((int) event.getX(),
                        (int) event.getY());
                colorCatch.eventAction(event);

                Thread.State tempState = thread.getState();
                if (tempState == Thread.State.RUNNABLE) {
                    thread.unPause();
                }
            }else{
                colorCatch.eventAction(event);
            }

            return true;
        }
    }

    /**
     * A Class to contain the thread that will be used to run the radical.appwards.colorcatch.game's radical.appwards.colorcatch.logic.
     *
     * @author Rick
     */
    class GameThread extends Thread {

        static final int STATE_LOSE = 1;
        static final int STATE_PAUSE = 2;
        static final int STATE_READY = 3;
        static final int STATE_RUNNING = 4;
        static final int STATE_WIN = 5;

        /**
         * I don't use difficult but we could modify the radical.appwards.colorcatch.game to add more
         * squares or speed.
         */
        private static final String KEY_DIFFICULTY = "mDifficulty";

        private int mDifficulty;

        /**
         * used to tell if the radical.appwards.colorcatch.game is paused or running.
         */
        private int mMode;

        private boolean mRun = false;

        private final SurfaceHolder mSurfaceHolder;

        private Handler mHandler;

        private Context context;

        GameThread(SurfaceHolder surfaceHolder, Context c,
                   Handler handler) {
            // get handles to some important radical.appwards.colorcatch.objects
            mSurfaceHolder = surfaceHolder;
            mHandler = handler;
            context = c;
        }

        void pause() {
            synchronized (mSurfaceHolder) {
                if (mMode == STATE_RUNNING) {
                    setState(STATE_PAUSE);
                }
            }
        }

        void unPause() {
            synchronized (mSurfaceHolder) {
                setState(STATE_RUNNING);
            }
        }

        /**
         * Contains the calls to update the GamePhysics and then draw the radical.appwards.colorcatch.screen
         * radical.appwards.colorcatch.objects.
         */
        @Override
        public void run() {
            while (mRun) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder) {
                        if (mMode == STATE_RUNNING) {
                            if (c != null) {
                                updatePhysics();
                                doDraw(c);
                            }
                        } else {
                            if (c != null) {
                                c.drawColor(Color.BLACK);
                            }
                        }
                    }
                } finally {
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        void setRunning(boolean b) {
            synchronized (mSurfaceHolder) {
                mRun = b;
            }
        }

        void setState(int mode) {
            synchronized (mSurfaceHolder) {
                setState(mode, null);
            }
        }

        void setState(int mode, CharSequence message) {
            synchronized (mSurfaceHolder) {
                mMode = mode;
                if (mMode == STATE_RUNNING) {
                    Message msg = mHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("text", "");
                    b.putInt("viz", View.INVISIBLE);
                    msg.setData(b);
                    mHandler.sendMessage(msg);
                } else {
                    Resources res = context.getResources();
                    CharSequence str = "";
                    if (mMode == STATE_READY)
                        str = res.getText(R.string.mode_ready);
                    else if (mMode == STATE_PAUSE)
                        str = res.getText(R.string.mode_pause);
                    else if (mMode == STATE_LOSE)
                        str = res.getText(R.string.mode_lose);
                    else if (mMode == STATE_WIN)
                        str = res.getString(R.string.mode_win_prefix)
                                + res.getString(R.string.mode_win_suffix);

                    if (message != null) {
                        str = message + "\n" + str;
                    }

                    Message msg = mHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("text", str.toString());
                    b.putInt("" +
                            "", View.VISIBLE);
                    msg.setData(b);
                    mHandler.sendMessage(msg);
                }
            }
        }

        SurfaceHolder getSurfaceHolder() {
            return mSurfaceHolder;
        }

        /* Callback invoked when the surface dimensions change. */
        void setSurfaceSize(int width, int height) {
            // synchronized to make sure these all change atomically
            synchronized (mSurfaceHolder) {
                colorCatch.setSurfaceSize(width, height);
            }
        }

        /**
         * Send the radical.appwards.colorcatch.game radical.appwards.colorcatch.logic to the radical.appwards.colorcatch.game to draw.
         *
         * @param canvas the object to be drawn on.
         */
        private void doDraw(Canvas canvas) {
            colorCatch.myDraw(canvas);
            canvas.save();
            canvas.restore();
        }

        /**
         * Send so the radical.appwards.colorcatch.game and have it update its radical.appwards.colorcatch.game state.
         */
        private void updatePhysics() {
            colorCatch.updatePhysics();
        }

        /**
         * If the menu button is pressed and restart is selected the radical.appwards.colorcatch.game resets
         * and returns to the menu.
         */
        void returnToMenu() {
            synchronized (mSurfaceHolder) {
                setState(STATE_RUNNING);
            }
            GameVariables.getInstance().resetGame();
            colorCatch.setMenuScreen(getContext());
            showStartButton();
            thread.unPause();
        }
    }
}
