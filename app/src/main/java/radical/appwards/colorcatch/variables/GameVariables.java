package radical.appwards.colorcatch.variables;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import radical.appwards.colorcatch.movements.Movable;
import radical.appwards.colorcatch.movements.MovableImplFactory;
import radical.appwards.colorcatch.movements.MovableLeftDownImpl;
import radical.appwards.colorcatch.movements.MovableLeftImpl;
import radical.appwards.colorcatch.movements.MovablePathImpl;
import radical.appwards.colorcatch.movements.MovableRightDownImpl;
import radical.appwards.colorcatch.movements.MovableRightImpl;
import radical.appwards.colorcatch.movements.MovableStraightDownImpl;
import radical.appwards.colorcatch.objects.Enemy;
import radical.appwards.colorcatch.objects.Player;
import radical.appwards.colorcatch.objects.Shield;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class GameVariables implements Serializable {

    // set to true to quick play to a certain radical.appwards.colorcatch.level
    public boolean DEBUG_MODE = false;
    // set the leve to play at first, you might have to restart for it to take
    // effect and by pass radical.appwards.colorcatch.database continue
    public int DEBUG_LEVEL = 8;

    public static int LEVELS = 10;
    public static int SCORE_INC = 100;
    public int HEALTH = 100;

    public static boolean ACCESS_SAVE_DB = true;

    public int NEW_LEVEL = 2000;
    public int LEVEL_BREAK = NEW_LEVEL / 5;
    public int INTRO_PAUSE_TIME = NEW_LEVEL / 15;

    public static final long serialVersionUID = -5881459669528802675L;

    private volatile static GameVariables instance;

    public ArrayList<Integer> xs, ys;
    public int playerH, enemyCount, playerLeft, playerRight, playerTop,
            playerBottom, levelEnemies, newX, newY, currLevel = 1,
            health = HEALTH, screenH, screenW, playerW, enemyW, enemyH,
            maxEnemies, shieldReserve = 10, speedModifier, tempLeft, level,
            tempside, mixedColorInt;
    public boolean gameVarsAreLoading, gameVarsLoaded, enemyCreation,
            loadingPlaying, menuPlaying, gamePlaying, continuedGame,
            setNewMixedColorInt;
    public Paint whitePaint = new Paint(),
            whitePaintRightAligned = new Paint(),
            whitePaintCenterAlign = new Paint(), blackPaint = new Paint(),
            randPaint = new Paint(),
            bluePaint = new Paint(), redPaint = new Paint(),
            yellowPaint = new Paint(), blueFillPaint = new Paint(),
            greenPaint = new Paint(), redFillPaint = new Paint(),
            whiteFillPaint = new Paint(), blackBoldPaint = new Paint(),
            yellowBoldPaint = new Paint();

    private Paint targetPaint = new Paint();
    public DisplayMetrics metrics;
    public long gameTimer, score = 0;
    public Player player;
    public Enemy[] enemyArray;
    public Shield[] playerShields, enemyShields;
    public Random rand = new Random();
    public MovableImplFactory moveImplFact = new MovableImplFactory();
    public MovableStraightDownImpl[] moveSD;
    public MovableLeftDownImpl[] moveLD;
    public MovableRightDownImpl[] moveRD;
    public MovableRightImpl[] moveR;
    public MovableLeftImpl[] moveL;
    public MovablePathImpl[] movePath;

    // singleton needs private constructor
    private GameVariables() {
        loadingPlaying = true;
    }

    // returns the one object instance
    public static GameVariables getInstance() {
        if (instance == null)
            synchronized (GameVariables.class) {
                if (instance == null)
                    instance = new GameVariables();
            }
        return instance;
    }

    public void setMetrics(DisplayMetrics m) {
        metrics = m;
        screenH = metrics.heightPixels;
        screenW = metrics.widthPixels;
        initPaint();
    }

    public void initPaint() {

        Thread loadingThread = new Thread() {
            @Override
            public void run() {
                initPaintObjects();
            }

            public void initPaintObjects() {
                bluePaint.setColor(Color.BLUE);
                bluePaint.setStrokeWidth(3);
                bluePaint.setStyle(Style.FILL);

                redPaint.setColor(Color.RED);
                redPaint.setStrokeWidth(3);
                redPaint.setStyle(Style.FILL);

                yellowPaint.setColor(Color.YELLOW);
                yellowPaint.setStrokeWidth(3);
                yellowPaint.setStyle(Style.FILL);

                greenPaint.setColor(Color.GREEN);
                greenPaint.setStrokeWidth(3);
                greenPaint.setStyle(Style.FILL);

                redFillPaint.setColor(Color.RED);
                redFillPaint.setStyle(Style.FILL);

                blueFillPaint.setColor(Color.BLUE);
                blueFillPaint.setStyle(Style.FILL);

                whiteFillPaint.setColor(Color.WHITE);
                whiteFillPaint.setStyle(Style.FILL);

                blackBoldPaint.setColor(Color.BLACK);
                blackBoldPaint.setTextSize(TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 48, metrics));
                blackBoldPaint.setTextAlign(Paint.Align.CENTER);
                blackBoldPaint.setTypeface(Typeface.DEFAULT_BOLD);

                yellowBoldPaint.setColor(Color.YELLOW);
                yellowBoldPaint.setTextSize(TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 48, metrics));
                yellowBoldPaint.setTextAlign(Paint.Align.CENTER);
                yellowBoldPaint.setTypeface(Typeface.DEFAULT_BOLD);

                whitePaint.setColor(Color.WHITE);
                whitePaint.setTextSize(TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 20, metrics));

                whitePaintRightAligned.setColor(Color.WHITE);
                whitePaintRightAligned.setTextSize(TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 20, metrics));
                whitePaintRightAligned.setTextAlign(Paint.Align.RIGHT);

                whitePaintCenterAlign.setColor(Color.WHITE);
                whitePaintCenterAlign.setTextSize(TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 20, metrics));
                whitePaintCenterAlign.setTextAlign(Paint.Align.CENTER);

                blackPaint.setColor(Color.BLACK);
                blackPaint.setTextSize(TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 20, metrics));
                blackPaint.setTextAlign(Paint.Align.CENTER);

                randPaint.setColor(Color.RED);
                randPaint.setTextSize(TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 20, metrics));
                randPaint.setTextAlign(Paint.Align.CENTER);
                randPaint.setStyle(Style.FILL);

                targetPaint.setStrokeWidth(10);
                targetPaint.setStyle(Style.STROKE);

                switch (rand.nextInt(2)) {
                    case 0:
                        targetPaint.setColor(Color.BLUE);
                        break;
                    case 1:
                        targetPaint.setColor(Color.GREEN);
                        break;
                }
            }
        };

        loadingThread.start();
    }

    public void cyclePaint() {
        if (randPaint.getColor() == Color.GREEN) {
            randPaint.setColor(Color.WHITE);
        } else if (randPaint.getColor() == Color.WHITE) {
            randPaint.setColor(Color.BLACK);
        } else if (randPaint.getColor() == Color.BLACK) {
            randPaint.setColor(Color.RED);
        } else if (randPaint.getColor() == Color.RED) {
            randPaint.setColor(Color.BLUE);
        } else if (randPaint.getColor() == Color.BLUE) {
            randPaint.setColor(Color.GREEN);
        }
    }

    public int getEnemyWidth() {
        return enemyW;
    }

    public int getEnemyHeight() {
        return enemyH;
    }

    public boolean getMenuPlaying() {
        return menuPlaying;
    }

    public void setMenuPlaying(boolean mp) {
        menuPlaying = mp;
    }

    public void setGameVarsLoaded(boolean b) {
        gameVarsLoaded = b;
    }

    public boolean getGameVarsLoaded() {
        return gameVarsLoaded;
    }

    public void movePlayerToPoint() {
        playerLeft = newX - playerW / 2;
        playerRight = newX + playerW / 2;
        playerTop = newY - playerH / 2;
        playerBottom = newY + playerH / 2;
    }

    public int getNewX() {
        return newX;
    }

    public void setScreenVarsSizes() {
        if (screenH >= 1000 && screenW >= 600) {
            speedModifier = 4;
            maxEnemies = 38;
        } else if (screenH >= 700 && screenW >= 400) {
            speedModifier = 3;
            maxEnemies = 30;
        } else if (screenH >= 400 && screenW >= 300) {
            speedModifier = 2;
            maxEnemies = 22;
        } else if (screenH >= 200 && screenW >= 200) {
            speedModifier = 1;
            maxEnemies = 20;
        } else {
            speedModifier = 1;
            maxEnemies = 18;
        }
    }

    public int getLevelEnemies() {
        return levelEnemies;
    }

    public void incEnemyArraySpeeed() {
        for (int i = 0; i < levelEnemies; i++) {
            enemyArray[i].incSpeed();
        }
    }

    public int getCurrLevel() {
        return currLevel;
    }

    public void incCurrentLevel() {
        currLevel++;
    }

    public void setPlayerLeft(int pl) {
        playerLeft = pl;
    }

    public void setPlayerRight(int pi) {
        playerRight = pi;
    }

    public int getNewY() {
        return newY;
    }

    public void setPlayerTop(int pt) {
        playerTop = pt;
    }

    public void setPlayerBottom(int pb) {
        playerBottom = pb;
    }

    public int getPlayerLeft() {
        return playerLeft;
    }

    public int getPlayerRight() {
        return playerRight;
    }

    public int getPlayerTop() {
        return playerTop;
    }

    public int getPlayerBottom() {
        return playerBottom;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public Shield getEnemyShields(int k) {
        return enemyShields[k];
    }

    public int getPlayerShieldsLen() {
        return playerShields.length;
    }

    public Shield getPlayerShields(int k) {
        return playerShields[k];
    }

    public void setEnemyCreation(boolean ec) {
        enemyCreation = ec;
    }

    public boolean getEnemyCreation() {
        return enemyCreation;
    }

    public void incEnemyCount() {
        enemyCount++;
    }

    public void decEnemyCount() {
        enemyCount--;
    }

    public int getSpeedModifier() {
        return speedModifier;
    }

    public int getNewLevel() {
        return NEW_LEVEL;
    }

    public int getLevelBreak() {
        return LEVEL_BREAK;
    }

    public Paint getTargetPaint() {
        return targetPaint;
    }

    public Paint getWhitePaintRightAlinged() {
        return whitePaintRightAligned;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int h) {
        if (h >= 0 && h < health) {
            health = h;
        }
    }

    public Movable getRandomMovable(int i) {
        switch (rand.nextInt(3)) {
            case 0:
                return moveSD[i];
            case 1:
                return moveLD[i];
            case 2:
                return moveRD[i];
            default:
                return null;
        }
    }

    public void addEndX(int endx) {
        if (xs != null) {
            xs.add(endx);
        }
    }

    public void addEndY(int endy) {
        if (ys != null) {
            ys.add(endy);
        }
    }

    public void removeEndXY() {
        if (ys != null && !ys.isEmpty()) {
            ys.remove(0);
        }

        if (xs != null && !xs.isEmpty()) {
            xs.remove(0);
        }
    }

    public float getXs(int i) {
        return xs.get(i);
    }

    public float getYs(int i) {
        return ys.get(i);
    }

    public Paint getRedPaint() {
        return redPaint;
    }

    public Paint getBluePaint() {
        return bluePaint;
    }

    public void clearXsYs() {
        ys.clear();
        xs.clear();
    }

    public Movable getLMovable(int i) {
        return moveLD[i];
    }

    public Movable getRMovable(int i) {
        return moveRD[i];
    }

    public void setCurrLevel(int cl) {
        currLevel = cl;
    }

    public Paint getGreenPaint() {
        return greenPaint;
    }

    public Paint getYellowPaint() {
        return yellowPaint;
    }

    public void setGamePlaying(boolean b) {
        gamePlaying = b;
    }

    public boolean getGamePlaying() {
        return gamePlaying;
    }

    public Paint getBlackBoldPaint() {
        return blackBoldPaint;
    }

    public Paint getYellowBoldPaint() {
        return yellowBoldPaint;
    }

    public Paint getRandomPaint() {
        switch (rand.nextInt(3)) {
            case 0:
                return redPaint;
            case 1:
                return bluePaint;
            default:
                return greenPaint;
        }
    }

    /**
     * Set up radical.appwards.colorcatch.game while in debug mode sets up radical.appwards.colorcatch.objects to be ready for any radical.appwards.colorcatch.level
     */
    public void setUpGame() {
        playerW = screenW / 5;
        playerH = screenW / 5;

        enemyW = playerW / 2;
        enemyH = playerW / 2;

        enemyArray = new Enemy[maxEnemies];

        moveSD = new MovableStraightDownImpl[maxEnemies];
        moveLD = new MovableLeftDownImpl[maxEnemies];
        moveRD = new MovableRightDownImpl[maxEnemies];
        moveL = new MovableLeftImpl[maxEnemies];
        moveR = new MovableRightImpl[maxEnemies];
        movePath = new MovablePathImpl[maxEnemies];

        for (int i = 0; i < maxEnemies; i++) {
            // create all the movable a head of time
            moveSD[i] = (MovableStraightDownImpl) moveImplFact
                    .createMovableImpl("down", 0);
            moveLD[i] = (MovableLeftDownImpl) moveImplFact.createMovableImpl(
                    "leftdown", 0);
            moveRD[i] = (MovableRightDownImpl) moveImplFact.createMovableImpl(
                    "rightdown", 0);
            moveR[i] = (MovableRightImpl) moveImplFact.createMovableImpl(
                    "right", 0);
            moveL[i] = (MovableLeftImpl) moveImplFact.createMovableImpl("left",
                    0);
            movePath[i] = (MovablePathImpl) moveImplFact.createMovableImpl(
                    "path", 0);

            tempLeft = rand.nextInt((screenW - enemyW) - (enemyW) + 1);
            enemyArray[i] = new Enemy(tempLeft, 0 - enemyH, tempLeft + enemyW,
                    0, speedModifier);

            switch (i % 3) {
                case 0:
                    enemyArray[i].setPaint(redPaint);
                    break;
                case 1:
                    enemyArray[i].setPaint(bluePaint);
                    break;
                case 2:
                    enemyArray[i].setPaint(greenPaint);
                    break;
            }
        }

        player = new Player(0, screenH - playerH, playerW, screenH, Color.RED);

        player.setOutlinePaint(getTargetPaint());

        playerShields = new Shield[shieldReserve];
        enemyShields = new Shield[maxEnemies];

        for (int i = 0; i < playerShields.length; i++) {
            playerShields[i] = new Shield();
        }

        for (int i = 0; i < enemyShields.length; i++) {
            enemyShields[i] = new Shield();
        }

        newX = screenW / 2;
        newY = screenH - screenH / 3;
    }

    public void resetGame() {
        currLevel = 1;
        gameTimer = 0;
        score = 0;
        health = HEALTH;
        continuedGame = false;
    }

    public void setTargetPaint(int color) {
        targetPaint.setColor(color);
    }
}
