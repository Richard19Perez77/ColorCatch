package radical.appwards.colorcatch.objects;

import java.io.Serializable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

/**
 * A class that defines the player's object.
 *
 * @author Rick
 */

public class Player implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Used for collision detection and keeping the player on radical.appwards.colorcatch.screen.
     */
    private Rect rect;
    private Paint paint, outline;
    public Paint mixedPlayerPaint;

    public Player(int left, int top, int right, int bottom, int startColor) {
        rect = new Rect();
        rect.left = left;
        rect.top = top;
        rect.right = right;
        rect.bottom = bottom;
        paint = new Paint();
        mixedPlayerPaint = new Paint();
        outline = new Paint();
        outline.setColor(Color.WHITE);
        outline.setStyle(Style.STROKE);
        outline.setStrokeWidth(3);
        setColorToPaint(startColor);
    }

    private void setColorToPaint(int color) {
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(rect, paint);
        canvas.drawRect(rect, outline);
    }

    public void move(int left, int top, int right, int bottom) {
        rect.left = left;
        rect.top = top;
        rect.right = right;
        rect.bottom = bottom;
    }

    public Rect getRect() {
        return rect;
    }

    /**
     * Used to color the player the color that the player has mixed into.
     *
     * @param p The paint object to paint the player.
     */
    public int addPaint(Paint p) {

        int r1 = Color.red(p.getColor());
        int g1 = Color.green(p.getColor());
        int b1 = Color.blue(p.getColor());

        int r2 = Color.red(paint.getColor());
        int g2 = Color.green(paint.getColor());
        int b2 = Color.blue(paint.getColor());

        int r3 = (r1 + r2) / 2;
        int g3 = (g1 + g2) / 2;
        int b3 = (b1 + b2) / 2;

        setColorToPaint(Color.rgb(r3, g3, b3));

        return Color.rgb(r3, g3, b3);

    }

    public Paint getPaint() {
        return paint;
    }

    /**
     * The color the player has targeted to collect or "catch".
     *
     * @param targetPaint target color to catch
     */
    public void setOutlinePaint(Paint targetPaint) {
        outline.setColor(targetPaint.getColor());
    }

}
