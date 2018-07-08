package com.cesarchretien.timecopapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout

class StopwatchView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)

        val paint = Paint().apply {
            color = Color.RED
        }

        val radius = Math.min(width, height).toFloat() / 2f

        canvas?.drawCircleLine(radius, radius, radius, 20f, paint)
    }

    private fun Canvas.drawCircleLine(cx: Float, cy: Float, radius: Float, thickness: Float, paint: Paint) {
        drawCircle(cx, cy, radius, paint)
        drawCircle(cx, cy, radius - thickness, Paint().apply { color = Color.BLACK })
    }
}