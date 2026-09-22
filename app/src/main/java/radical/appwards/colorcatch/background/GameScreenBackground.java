package radical.appwards.colorcatch.background;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;

import radical.appwards.colorcatch.variables.GameVariables;

/**
 * The play screen uses the same pulsing radial as the opening.
 */
public class GameScreenBackground {

    private final GameVariables gv;
    private final Paint circlePaint = new Paint();
    private final Matrix shaderMatrix = new Matrix();
    private RadialGradient radialG;
    private int x;
    private int y;
    private int startR;
    private int endR;
    private int r;
    private boolean incR = true;
    private boolean ready;

    public GameScreenBackground() {
        gv = GameVariables.getInstance();
    }

    public void draw(Canvas canvas) {
        ensureReady();
        canvas.drawColor(Color.BLACK);
        if (!ready) {
            return;
        }
        canvas.drawCircle(x, y, endR, circlePaint);
    }

    public void updatePhysics() {
        ensureReady();
        if (!ready) {
            return;
        }
        if (r < startR) {
            incR = true;
        }
        if (r > endR) {
            incR = false;
        }
        if (incR) {
            r += 7;
        } else {
            r -= 7;
        }
        if (startR > 0) {
            shaderMatrix.setScale(r / (float) startR, r / (float) startR, x, y);
            radialG.setLocalMatrix(shaderMatrix);
        }
    }

    private void ensureReady() {
        if (ready || gv.screenW <= 0 || gv.screenH <= 0) {
            return;
        }
        x = gv.screenW / 2;
        y = gv.screenH / 2;
        startR = r = gv.screenH / 4;
        endR = r * 4;
        radialG = new RadialGradient(x, y, startR, Color.RED, Color.BLUE,
                android.graphics.Shader.TileMode.CLAMP);
        circlePaint.setShader(radialG);
        ready = startR > 0;
    }
}
