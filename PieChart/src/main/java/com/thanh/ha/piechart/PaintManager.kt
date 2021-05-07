package com.thanh.ha.piechart

import android.content.Context
import android.graphics.*
import androidx.core.content.res.ResourcesCompat
import kotlin.math.roundToInt

object PaintManager {

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

    fun initEraser(): Paint {
        val clearMode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        val eraser = Paint()
        eraser.apply {
            isAntiAlias = true
            xfermode = clearMode
        }
        return eraser
    }

    fun initMainPaint(pieStyle: Int, strokeWidth: Float): Paint {

        val mainPaint = Paint()
        mainPaint.apply {
            isAntiAlias = true
            color = Color.BLACK
            when (pieStyle) {
                1 -> {
                    style = Paint.Style.FILL
                }
                else -> {
                    style = Paint.Style.STROKE
                    this.strokeWidth = strokeWidth
                    strokeCap = Paint.Cap.ROUND
                    strokeJoin = Paint.Join.MITER
                    pathEffect = CornerPathEffect(strokeWidth/2)
                }
            }
        }
        return mainPaint
    }

    fun initShadowPaint(shadowAlpha: Float, pieStyle: Int, width: Float): Paint {
        val shadowPaint = Paint()
        shadowPaint.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            alpha = ((shadowAlpha * 256) % 256).roundToInt()
            when (pieStyle) {
                1 -> {
                    style = Paint.Style.FILL
                }
                else -> {
                    style = Paint.Style.STROKE
                    strokeWidth = width*0.9f
                }
            }
        }
        return shadowPaint
    }
}