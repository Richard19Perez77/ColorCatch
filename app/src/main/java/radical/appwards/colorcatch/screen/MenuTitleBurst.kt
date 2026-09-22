package radical.appwards.colorcatch.screen

import android.graphics.Canvas
import android.graphics.Paint
import android.os.SystemClock

/**
 * Extra Color banners right and extra Catch banners left. A second pair
 * does the same path at double speed. Original titles stay in MenuScreenImpl.
 */
class MenuTitleBurst {

    private val wordFill = Paint(Paint.ANTI_ALIAS_FLAG)

    private var lastMs = 0L
    private var screenW = 0
    private var colorX = Float.NaN
    private var catchX = Float.NaN
    private var colorFastX = Float.NaN
    private var catchFastX = Float.NaN
    private var colorWidth = 0f
    private var catchWidth = 0f

    fun setTarget(x: Float, y: Float) {
        // Touch still retargets the background squares; banners keep their path.
    }

    fun update() {
        val now = SystemClock.uptimeMillis()
        if (lastMs == 0L) {
            lastMs = now
            return
        }
        val dt = ((now - lastMs).coerceAtMost(50L)) / 1000f
        lastMs = now
        if (screenW <= 0) return

        val speed = screenW * 0.74f
        val fast = speed * 2f
        colorX += speed * dt
        catchX -= speed * dt
        colorFastX += fast * dt
        catchFastX -= fast * dt

        val colorLimit = screenW + colorWidth
        val catchLimit = screenW + catchWidth
        if (colorX > colorLimit) {
            colorX = -colorWidth
        }
        if (catchX < -catchWidth) {
            catchX = catchLimit
        }
        if (colorFastX > colorLimit) {
            colorFastX = -colorWidth
        }
        if (catchFastX < -catchWidth) {
            catchFastX = catchLimit
        }
    }

    fun draw(canvas: Canvas, width: Int, height: Int, outline: Paint, fill: Paint) {
        if (width <= 0 || height <= 0) return
        screenW = width
        colorWidth = fill.measureText("Color")
        catchWidth = fill.measureText("Catch")

        if (colorX.isNaN()) {
            colorX = width / 2f
            colorFastX = width / 2f
        }
        if (catchX.isNaN()) {
            catchX = width / 2f
            catchFastX = width / 2f
        }

        val colorY = height / 3f
        val catchY = height - height / 4f

        drawBanner(canvas, "Color", colorX, colorY, outline, fill)
        drawBanner(canvas, "Color", colorFastX, colorY, outline, fill)
        drawBanner(canvas, "Catch", catchX, catchY, outline, fill)
        drawBanner(canvas, "Catch", catchFastX, catchY, outline, fill)
    }

    private fun drawBanner(
        canvas: Canvas,
        word: String,
        x: Float,
        y: Float,
        outline: Paint,
        fill: Paint
    ) {
        wordFill.set(fill)
        canvas.drawText(word, x, y, outline)
        canvas.drawText(word, x, y + 5f, wordFill)
    }
}
