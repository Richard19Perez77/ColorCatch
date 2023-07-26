package radical.appwards.colorcatch.screen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.view.MotionEvent;

import java.util.Random;
import java.util.concurrent.Callable;

import radical.appwards.colorcatch.movements.PlotPoints;
import radical.appwards.colorcatch.variables.GameVariables;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * A class that defines the menu radical.appwards.colorcatch.screen. A button, some falling squares and
 * intro text as well as a moving gradient for show.
 *
 * @author Rick
 */
class MenuScreenImpl implements Screen {

    private final PublishSubject<MotionEvent> mTouchSubject = PublishSubject.create();
    private final Observable<MotionEvent> mTouches = mTouchSubject.asObservable();
    private final Observable<MotionEvent> mDownObservable = mTouches.filter(ev -> ev.getActionMasked() == MotionEvent.ACTION_DOWN);
    private final Observable<MotionEvent> mUpObservable = mTouches.filter(ev -> ev.getActionMasked() == MotionEvent.ACTION_UP);
    private final Observable<MotionEvent> mMovesObservable = mTouches.filter(ev -> ev.getActionMasked() == MotionEvent.ACTION_MOVE);

    private GameVariables gv = GameVariables.getInstance();

    private final Thread loadingThread;
    private Paint circlePaint = new Paint(), yellowPaint = new Paint(),
            greenPaint = new Paint(), blackPaint = new Paint();
    private RadialGradient radialG;
    private String radical, music;
    private int x, y, startR, endR, r, colora, colorb, squares, slen, h, w;
    private Random random = new Random();
    private boolean menuVarsLoaded, incR;
    private Square[] squareList;
    private PlotPoints plotPoints;

    private Callable<RadialGradient> radialGradientCallable = new Callable<RadialGradient>() {
        @Override
        public RadialGradient call() throws Exception {
            return new RadialGradient(x, y, r, colora, colorb,
                    android.graphics.Shader.TileMode.CLAMP);
        }
    };

    private boolean doUpdateSquares;
    private float downX;
    private float downY;

    MenuScreenImpl() {
        menuVarsLoaded = false;
        loadingThread = new Thread() {
            @Override
            public void run() {
                mDownObservable.subscribe(downEvent -> {
                    mMovesObservable
                            .takeUntil(mUpObservable
                                    .doOnNext(upEvent -> {
                                        if (upEvent.getX() < w && upEvent.getX() > 0) {
                                            if (upEvent.getY() < h && upEvent.getY() > 0) {
                                                downX = upEvent.getX();
                                                downY = upEvent.getY();
                                                doUpdateSquares = true;
                                            }
                                        }
                                    }))
                            .subscribe(motionEvent -> {
                                if (motionEvent.getX() < w && motionEvent.getX() > 0) {
                                    if (motionEvent.getY() < h && motionEvent.getY() > 0) {
                                        downX = motionEvent.getX();
                                        downY = motionEvent.getY();
                                        doUpdateSquares = true;
                                    }
                                }
                            });
                });

                w = gv.screenW;
                h = gv.screenH;
                squares = (h * w) / 10000;

                greenPaint.setColor(Color.GREEN);
                greenPaint.setStrokeWidth(3);
                greenPaint.setStyle(Paint.Style.STROKE);
                yellowPaint.setColor(Color.YELLOW);
                yellowPaint.setStrokeWidth(3);
                yellowPaint.setStyle(Paint.Style.STROKE);
                blackPaint.setColor(Color.BLACK);
                blackPaint.setStrokeWidth(3);
                blackPaint.setStyle(Paint.Style.STROKE);
                radical = "Radical\u2605Appwards";
                music = "\u2669pinklogik.bandcamp.com\u2669";
                x = w / 2;
                y = h / 2;
                startR = r = h / 4;
                endR = r * 4;
                colora = Color.RED;
                colorb = Color.BLUE;
                radialG = new RadialGradient(x, y, r, colora, colorb,
                        android.graphics.Shader.TileMode.CLAMP);
                circlePaint.setShader(radialG);

                slen = h / 40;

                squareList = new Square[squares];
                plotPoints = new PlotPoints();
                for (int i = 0; i < squares; i++) {
                    squareList[i] = new Square();
                }

                menuVarsLoaded = true;
            }
        };

        loadingThread.start();

    }

    @Override
    public void myDraw(Context context, Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        if (menuVarsLoaded && !loadingThread.isAlive()) {
            canvas.drawCircle(x, y, endR, circlePaint);

            for (int i = 0; i < squares - 1; i++) {
                int nextPoint = squareList[i].nextPoint();
                int offset = squareList[i].offset;
                switch (squareList[i].side) {
                    case 0:
                        canvas.drawRect(squareList[i].movePoints[nextPoint].x,
                                squareList[i].movePoints[nextPoint].y + offset,
                                squareList[i].movePoints[nextPoint].x + slen,
                                squareList[i].movePoints[nextPoint].y + slen,
                                blackPaint);

                        canvas.drawRect(squareList[i].movePoints[nextPoint].x,
                                squareList[i].movePoints[nextPoint].y + 4 + offset,
                                squareList[i].movePoints[nextPoint].x + slen,
                                squareList[i].movePoints[nextPoint].y + 4 + slen,
                                greenPaint);
                        break;
                    case 1:
                        canvas.drawRect(squareList[i].movePoints[nextPoint].x + offset,
                                squareList[i].movePoints[nextPoint].y,
                                squareList[i].movePoints[nextPoint].x + slen,
                                squareList[i].movePoints[nextPoint].y + slen,
                                blackPaint);

                        canvas.drawRect(squareList[i].movePoints[nextPoint].x + offset,
                                squareList[i].movePoints[nextPoint].y + 4,
                                squareList[i].movePoints[nextPoint].x + slen,
                                squareList[i].movePoints[nextPoint].y + 4 + slen,
                                greenPaint);
                        break;
                    case 2:
                        canvas.drawRect(squareList[i].movePoints[nextPoint].x,
                                squareList[i].movePoints[nextPoint].y + offset,
                                squareList[i].movePoints[nextPoint].x + slen,
                                squareList[i].movePoints[nextPoint].y + slen,
                                blackPaint);

                        canvas.drawRect(squareList[i].movePoints[nextPoint].x,
                                squareList[i].movePoints[nextPoint].y + 4 + offset,
                                squareList[i].movePoints[nextPoint].x + slen,
                                squareList[i].movePoints[nextPoint].y + 4 + slen,
                                greenPaint);
                        break;
                    case 3:
                        canvas.drawRect(squareList[i].movePoints[nextPoint].x + offset,
                                squareList[i].movePoints[nextPoint].y,
                                squareList[i].movePoints[nextPoint].x + slen,
                                squareList[i].movePoints[nextPoint].y + slen,
                                blackPaint);

                        canvas.drawRect(squareList[i].movePoints[nextPoint].x + offset,
                                squareList[i].movePoints[nextPoint].y + 4,
                                squareList[i].movePoints[nextPoint].x + slen,
                                squareList[i].movePoints[nextPoint].y + 4 + slen,
                                greenPaint);
                        break;
                }
            }

            canvas.drawText(radical, w / 2, h / 10, gv.blackPaint);
            canvas.drawText(radical, w / 2, h / 10 + 3, gv.randPaint);

            canvas.drawText(music, w / 2, h - h / 10, gv.blackPaint);
            canvas.drawText(music, w / 2, h - h / 10 - 3, gv.randPaint);

            canvas.drawText("Color", w / 2, h / 3, gv.getBlackBoldPaint());
            canvas.drawText("Color", w / 2, h / 3 + 5, gv.getYellowBoldPaint());

            canvas.drawText("Catch", w / 2, h - h / 4, gv.getBlackBoldPaint());
            canvas.drawText("Catch", w / 2, h - h / 4 - 5,
                    gv.getYellowBoldPaint());
        }
    }

    @Override
    public void updatePhysics(Context context) {
        if (menuVarsLoaded) {
            gv.cyclePaint();

            if (r < startR)
                incR = true;

            if (r > endR)
                incR = false;

            if (incR) {
                r += 7;
            } else {
                r -= 7;
            }

            try {
                radialG = radialGradientCallable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
            circlePaint.setShader(radialG);

            if (doUpdateSquares) {
                doUpdateSquares = false;
                for (int i = 0; i < squares - 1; i++) {
                    squareList[i].updateNewStart(downX, downY);
                }
            }

            for (int i = 0; i < squares - 1; i++) {
                squareList[i].update();
            }
        }
    }

    @Override
    public void eventAction(MotionEvent event) {
        mTouchSubject.onNext(event);
    }

    private class Square {
        Point[] movePoints = null;
        int nextMove = 0;
        int nextReduceMove = 0;
        int offset = 0;
        int side = 0;

        Square() {
            side = random.nextInt(4);
            movePoints = plotPoints.plotLine(getBorderPoint(side), new Point(gv.screenW / 2, gv.screenH / 2));
            nextReduceMove = movePoints.length / slen;
            nextMove = random.nextInt(movePoints.length - 1);
        }

        int nextPoint() {
            nextMove += gv.speedModifier;
            if (nextMove >= movePoints.length) {
                nextMove = 0;
                offset = 0;
            }
            return nextMove;
        }

        void update() {
            if (nextReduceMove != 0) {
                if (nextMove % nextReduceMove == 0) {
                    if (offset < nextReduceMove)
                        offset++;
                }
            }
        }

        private Point getBorderPoint(int s) {
            switch (s) {
                case 0:
                    return getTopPoint();
                case 1:
                    return getLeftPoint();
                case 2:
                    return getBottomPoint();
                case 3:
                    return getRightPoint();
            }
            return null;
        }

        void updateNewStart(float downX, float downY) {
            movePoints = plotPoints.plotLine(getBorderPoint(side), new Point((int) downX, (int) downY));
            nextReduceMove = movePoints.length / slen;
            nextMove = random.nextInt(movePoints.length - 1);
        }
    }

    private Point getRightPoint() {
        int randH = random.nextInt(gv.screenH);
        return new Point(gv.screenW, randH);
    }

    private Point getBottomPoint() {
        int randW = random.nextInt(gv.screenW);
        return new Point(randW, gv.screenH);
    }

    private Point getLeftPoint() {
        int randH = random.nextInt(gv.screenH);
        return new Point(0 - slen, randH);
    }

    private Point getTopPoint() {
        int randW = random.nextInt(gv.screenW);
        return new Point(randW, 0 - slen);
    }
}