package radical.appwards.colorcatch.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;

/**
 * Fills a square with a radial gradient of its own color, pulsing the way the
 * opening-screen circle does. Callers keep the original paint color for
 * catching and mixing.
 */
public final class GradientSquare {

    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static float pulse = 0f;
    private static int pulseDir = 1;

    private GradientSquare() {
    }

    public static void advancePulse() {
        pulse += 0.018f * pulseDir;
        if (pulse >= 1f) {
            pulse = 1f;
            pulseDir = -1;
        } else if (pulse <= 0f) {
            pulse = 0f;
            pulseDir = 1;
        }
    }

    public static void draw(Canvas canvas, Rect rect, int color) {
        draw(canvas, rect.left, rect.top, rect.right, rect.bottom, color);
    }

    public static void draw(Canvas canvas, float left, float top, float right, float bottom, int color) {
        if (right <= left || bottom <= top) {
            return;
        }
        float cx = (left + right) / 2f;
        float cy = (top + bottom) / 2f;
        float half = Math.min(right - left, bottom - top) / 2f;
        float radius = Math.max(1f, half * (0.45f + 1.25f * pulse));
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(new RadialGradient(
                cx,
                cy,
                radius,
                color,
                darken(color),
                Shader.TileMode.CLAMP));
        canvas.drawRect(left, top, right, bottom, paint);
    }

    private static int darken(int color) {
        return mix(color, Color.BLACK, 0.72f);
    }

    private static int mix(int from, int to, float amount) {
        float keep = 1f - amount;
        return Color.rgb(
                (int) (Color.red(from) * keep + Color.red(to) * amount),
                (int) (Color.green(from) * keep + Color.green(to) * amount),
                (int) (Color.blue(from) * keep + Color.blue(to) * amount));
    }
}
