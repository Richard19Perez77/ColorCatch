package radical.appwards.colorcatch.background;

import java.util.ArrayList;
import java.util.Random;

import radical.appwards.colorcatch.variables.GameVariables;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * A Class used to draw the radical.appwards.colorcatch.background of the radical.appwards.colorcatch.game when being played.
 *
 * @author Rick Perez
 */

public class GameScreenBackground {

    /**
     * Used to get the games radical.appwards.colorcatch.screen radical.appwards.colorcatch.variables.
     */
    private GameVariables gv;
    private ArrayList<MyShapeMovable> myShapes;
    private MyShapeMovable myShape;
    private final int SHAPES = 3;
    private Random rand = new Random();

    public GameScreenBackground() {
        gv = GameVariables.getInstance();
        myShapes = new ArrayList<>();
        for (int i = 0; i < SHAPES; i++) {
            myShape = new MyShapeMovable();
            myShapes.add(myShape);
        }
    }

    /**
     * Used to draw the shapes on radical.appwards.colorcatch.screen. Each shape has a draw method to define
     * how its to be drawn.
     *
     * @param canvas The canvas to draw on.
     */
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        for (MyShapeMovable m : myShapes) {
            m.draw(canvas);
        }

    }

    /**
     * Updates the physics of each shape. Movement is easy, but transforming
     * gradients can cause slowdown. Threading off this main activity thread
     * helps but has been troublesome.
     */
    public void updatePhysics() {
        for (MyShapeMovable m : myShapes) {
            m.updatePhysics();
        }
    }

    /**
     * Called at each radical.appwards.colorcatch.level change to resize shapes and shading gradient.
     */
    public void refresh() {
        myShapes.clear();
        for (int i = 0; i < SHAPES; i++) {
            myShape = new MyShapeMovable();
            myShapes.add(myShape);
        }
    }

    public void updateColors(int newColor) {
        for (int i = 0; i < SHAPES; i++) {
            myShapes.get(i).updateColor(newColor);
        }
    }

    /**
     * A class that defines a shape that can be moved on the canvas. Each shape
     * has a randomly generated size and a shading gradient.
     *
     * @author Rick
     */
    class MyShapeMovable {
        private Paint circlePaint = new Paint();
        private int x, y, r;
        // private int colora, colorb;
        private int speed;

        public MyShapeMovable() {
            x = rand.nextInt(gv.screenW);
            y = rand.nextInt(gv.screenH);
            r = rand.nextInt(gv.screenW / 2);

            circlePaint.setColor(Color.WHITE);
            speed = rand.nextInt(gv.getSpeedModifier()) + 1;
        }

        public void updateColor(int c) {
            circlePaint.setColor(c);
        }

        public void draw(Canvas canvas) {
            canvas.drawCircle(x, y, r, circlePaint);
        }

        /**
         * Move the object to the left untill offscreen and reset it on the
         * right.
         */
        public void updatePhysics() {
            x -= speed;
            if (x + r < 0) {
                x = gv.screenW + r;
            }
        }
    }
}
