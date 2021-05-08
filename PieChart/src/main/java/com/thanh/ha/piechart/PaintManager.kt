package com.thanh.ha.piechart

import android.content.Context
import android.graphics.*
import androidx.core.content.res.ResourcesCompat
import kotlin.math.roundToInt

//TODO : review this object
object PaintManager {

    /**
     * Return main text for the chart.
     * I don't know if i should place this func here... inside and object.
     */
    fun initTextPaint(context: Context, textColor: Int, textSize: Float, font: Int): Paint {
        val textPaint = Paint()
        textPaint.apply {
            color = textColor
            isFakeBoldText
            isAntiAlias = true
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
            this.textSize = textSize
            if (font > 0)
                try {
                    typeface = ResourcesCompat.getFont(context, font)
                } catch (ex: Exception) {

                }
        }
        return textPaint
    }

    /**
     * Return main Eraser for the chart.
     * I don't know if i should place this func here... inside and object.
     */

    fun initEraser(): Paint {
        val clearMode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        val eraser = Paint()
        eraser.apply {
            isAntiAlias = true
            xfermode = clearMode
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }
        return eraser
    }

    fun initMainPaint(strokeWidth: Float): Paint {

        val mainPaint = Paint()
        mainPaint.apply {
            isAntiAlias = true
            color = Color.BLACK
            style = Paint.Style.STROKE
            this.strokeWidth = strokeWidth
            strokeCap = Paint.Cap.BUTT
            strokeJoin = Paint.Join.ROUND
            pathEffect = CornerPathEffect(strokeWidth / 2)
        }
        return mainPaint
    }

    fun initShadowPaint(shadowAlpha: Float, width: Float): Paint {
        val shadowPaint = Paint()
        shadowPaint.apply {
            isAntiAlias = true
            alpha = ((shadowAlpha * 256) % 256).roundToInt()
            style = Paint.Style.STROKE
            strokeWidth = width
        }
        return shadowPaint
    }
}