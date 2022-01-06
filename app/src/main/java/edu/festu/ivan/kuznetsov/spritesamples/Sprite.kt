package edu.festu.ivan.kuznetsov.spritesamples

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import java.util.ArrayList


class Sprite(
    var x: Double,
    var y: Double,
    var vx: Double,
    var vy: Double,
    initialFrame: Rect,
    private val bitmap: Bitmap
) {
    private val frames: MutableList<Rect>
    var frameWidth: Int
    var frameHeight: Int
    private var currentFrame: Int
    private var frameTime: Double
    private var timeForCurrentFrame: Double
    private val padding: Int

    fun addFrame(frame: Rect) {
        frames.add(frame)
    }

//    val framesCount: Int
//        get() = frames.size

    fun update(ms: Int) {
        timeForCurrentFrame += ms.toDouble()
        if (timeForCurrentFrame >= frameTime) {
            currentFrame = (currentFrame + 1) % frames.size
            timeForCurrentFrame -= frameTime
        }
        x += vx * ms / 1000.0
        y += vy * ms / 1000.0
    }

    fun draw(canvas: Canvas) {
        val p = Paint()
        val destination = Rect(
            x.toInt(),
            y.toInt(), (x + frameWidth).toInt(), (y + frameHeight).toInt()
        )
        canvas.drawBitmap(bitmap, frames[currentFrame], destination, p)
    }

    val boundingBoxRect: Rect
        get() = Rect(
            x.toInt() + padding,
            y.toInt() + padding,
            (x + frameWidth - 2 * padding).toInt(),
            (y + frameHeight - 2 * padding).toInt()
        )

    fun intersect(s: Sprite): Boolean {
        return boundingBoxRect.intersect(s.boundingBoxRect)
    }

    init {
        frames = ArrayList()
        frames.add(initialFrame)
        timeForCurrentFrame = 0.0
        frameTime = 25.0
        currentFrame = 0
        frameWidth = initialFrame.width()
        frameHeight = initialFrame.height()
        padding = 20
    }
}
