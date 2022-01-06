package edu.festu.ivan.kuznetsov.spritesamples

import android.content.Context
import android.graphics.*
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View


class GameView(context: Context?) : View(context) {
    private val playerBird: Sprite
    private val enemyBird: Sprite
    private var viewWidth = 0
    private var viewHeight = 0
    private var points = 0
    private val timerInterval = 30
    private val p = Paint()
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawARGB(250, 127, 199, 255)
        playerBird.draw(canvas)
        enemyBird.draw(canvas)

        p.isAntiAlias = true
        p.textSize = 55.0f
        p.color = Color.WHITE
        canvas.drawText(points.toString() + "", (viewWidth - 200).toFloat(), 70f, p)
    }

    protected fun update() {
        playerBird.update(timerInterval)
        enemyBird.update(timerInterval)
        if (playerBird.y + playerBird.frameHeight > viewHeight) {
            playerBird.y = ((viewHeight - playerBird.frameHeight).toDouble())
            playerBird.y = (-playerBird.vy)
            points--
        } else if (playerBird.y < 0) {
            playerBird.y= 0.0
            playerBird.vy = (-playerBird.vy)
            points--
        }
        if (enemyBird.x < -enemyBird.frameWidth) {
            teleportEnemy()
            points += 10
        }
        if (enemyBird.intersect(playerBird)) {
            teleportEnemy()
            points -= 40
        }
        invalidate()
    }



    override fun onTouchEvent(event: MotionEvent): Boolean {
        val eventAction = event.action
        if (eventAction == MotionEvent.ACTION_DOWN) {
            if (event.y < playerBird.boundingBoxRect.top) {
                playerBird.vy = -100.0
                points--
            } else if (event.y > playerBird.boundingBoxRect.bottom) {
                playerBird.vy = 100.0
                points--
            }
        }
        return true
    }

    private fun teleportEnemy() {
        enemyBird.x = (viewWidth + Math.random() * 500)
        enemyBird.y = (Math.random() * (viewHeight - enemyBird.frameHeight))
    }

    internal inner class Timer :
        CountDownTimer(Int.MAX_VALUE.toLong(), timerInterval.toLong()) {
        override fun onTick(millisUntilFinished: Long) {
            update()
        }

        override fun onFinish() {}
    }

    init {
        var b = BitmapFactory.decodeResource(resources, R.drawable.player)
        var w = b.width / 5
        var h = b.height / 3
        var firstFrame = Rect(0, 0, w, h)
        playerBird = Sprite(10.0, 0.0, 0.0, 100.0, firstFrame, b)
        for (i in 0..2) {
            for (j in 0..3) {
                if (i == 0 && j == 0) {
                    continue
                }
                if (i == 2 && j == 3) {
                    continue
                }
                playerBird.addFrame(Rect(j * w, i * h, j * w + w, i * w + w))
            }
        }
        b = BitmapFactory.decodeResource(resources, R.drawable.enemy)
        w = b.width / 5
        h = b.height / 3
        firstFrame = Rect(4 * w, 0, 5 * w, h)
        enemyBird = Sprite(2000.0, 250.0, -300.0, 0.0, firstFrame, b)
        for (i in 0..2) {
            for (j in 4 downTo 0) {
                if (i == 0 && j == 4) {
                    continue
                }
                if (i == 2 && j == 0) {
                    continue
                }
                enemyBird.addFrame(Rect(j * w, i * h, j * w + w, i * w + w))
            }
        }
        val t = Timer()
        t.start()
    }
}