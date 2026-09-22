package radical.appwards.colorcatch.screen

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.SystemClock
import kotlin.math.abs
import kotlin.math.exp

/**
 * One extra Color banner travels right and another travels down. Both wrap,
 * speed up, and fade until nearly invisible, then start again. A dim shadow
 * sits behind each and shows where the word is as the bright text fades.
 * The centered Color and Catch titles stay put in MenuScreenImpl.
 */
class MenuTitleBurst {

    private companion object {
        const val START_SPEED = 1.48f
        const val INVISIBLE_SPEED = 48f
        const val ACCEL_PER_SEC = 1.1f
    }

    private val wordFill = Paint(Paint.ANTI_ALIAS_FLAG)
    private val wordOutline = Paint(Paint.ANTI_ALIAS_FLAG)
    private val wordShadow = Paint(Paint.ANTI_ALIAS_FLAG)

    private var lastMs = 0L
    private var screenW = 0
    private var screenH = 0
    private var colorX = Float.NaN
    private var colorY = Float.NaN
    private var colorWidth = 0f
    private var colorHeight = 0f
    private var speedFactor = START_SPEED

    fun setTarget(x: Float, y: Float) {
        // Touch still retargets the background squares; the banner keeps its path.
    }

    fun update() {
        val now = SystemClock.uptimeMillis()
        if (lastMs == 0L) {
            lastMs = now
            return
        }
        val dt = ((now - lastMs).coerceAtMost(50L)) / 1000f
        lastMs = now
        if (screenW <= 0 || screenH <= 0) return

        speedFactor *= exp(ACCEL_PER_SEC * dt)
        if (speedFactor >= INVISIBLE_SPEED) {
            speedFactor = START_SPEED
            colorX = -colorWidth
            colorY = -colorHeight
        }

        colorX += screenW * speedFactor * dt
        colorY += screenH * speedFactor * dt

        val span = screenW + colorWidth
        if (span > 0f) {
            while (colorX > screenW) {
                colorX -= span
            }
        }
        val verticalSpan = screenH + colorHeight
        if (verticalSpan > 0f) {
            while (colorY > screenH) {
                colorY -= verticalSpan
            }
        }
    }

    fun draw(canvas: Canvas, width: Int, height: Int, outline: Paint, fill: Paint) {
        if (width <= 0 || height <= 0) return
        screenW = width
        screenH = height
        colorWidth = fill.measureText("Color")
        colorHeight = fill.textSize

        if (colorX.isNaN()) {
            colorX = width / 2f
        }
        if (colorY.isNaN()) {
            colorY = height / 3f
        }

        val fade = (START_SPEED / speedFactor).coerceIn(0.03f, 1f)
        val titleY = height / 3f
        val awayAcross = colorAwayFromTitle(colorX, width / 2f, colorWidth, width / 2f)
        val awayDown = colorAwayFromTitle(colorY, titleY, colorHeight, height / 2f)
        drawBanner(canvas, "Color", colorX, titleY, outline, fill, fade, awayAcross)
        drawBanner(canvas, "Color", width / 2f, colorY, outline, fill, fade, awayDown)
    }

    /**
     * 0 while the moving word still overlaps the centered title, then a smooth
     * 0–1 ramp out to [fullRedAt] so yellow eases into red as it leaves.
     */
    private fun colorAwayFromTitle(x: Float, titleX: Float, overlap: Float, fullRedAt: Float): Float {
        val gap = (fullRedAt - overlap).coerceAtLeast(1f)
        val t = ((abs(x - titleX) - overlap) / gap).coerceIn(0f, 1f)
        return t * t * (3f - 2f * t)
    }

    private fun drawBanner(
        canvas: Canvas,
        word: String,
        x: Float,
        y: Float,
        outline: Paint,
        fill: Paint,
        fade: Float,
        redAmount: Float
    ) {
        val color = blendYellowToRed(redAmount)
        val shadowStrength = (1f - fade).coerceIn(0f, 1f)

        wordShadow.set(fill)
        wordShadow.color = darken(color, 0.45f)
        wordShadow.alpha = (fill.alpha * shadowStrength).toInt().coerceIn(0, 255)
        canvas.drawText(word, x + 8f, y + 12f, wordShadow)

        wordOutline.set(outline)
        wordFill.set(fill)
        wordOutline.alpha = (outline.alpha * fade).toInt().coerceIn(0, 255)
        wordFill.color = color
        wordFill.alpha = (fill.alpha * fade).toInt().coerceIn(0, 255)
        canvas.drawText(word, x, y, wordOutline)
        canvas.drawText(word, x, y + 5f, wordFill)
    }

    private fun darken(color: Int, amount: Float): Int {
        val keep = amount.coerceIn(0f, 1f)
        return Color.rgb(
            (Color.red(color) * keep).toInt(),
            (Color.green(color) * keep).toInt(),
            (Color.blue(color) * keep).toInt()
        )
    }

    private fun blendYellowToRed(amount: Float): Int {
        val green = (255f * (1f - amount.coerceIn(0f, 1f))).toInt()
        return Color.rgb(255, green, 0)
    }
}
