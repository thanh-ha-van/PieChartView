package com.thanh.ha.piechart

import android.graphics.Canvas
import kotlin.math.cos
import kotlin.math.sin

/**
 * Translate canvas by a vector which is a combination of an angle and a distance.
 * @param angle: angle start by 0 degree in mathematics
 * @param distance: in px
 * @return Unit
 * @sample 1.percentWith(10) = 0.1f
 */
fun Canvas.translateBy(angle: Float, distance: Int) {

    val pureDegree = angle % 360

    val degree = Math.toRadians(pureDegree.toDouble())

    val xShift = (cos(degree) * distance).toFloat()

    val yShift = (sin(degree) * distance).toFloat()

    this.translate(xShift, yShift)
}

fun getVectorXY(angle: Float, distance: Int): Pair<Float, Float> {

    val pureDegree = angle % 360

    val degree = Math.toRadians(pureDegree.toDouble())

    val xShift = (cos(degree) * distance).toFloat()

    val yShift = (sin(degree) * distance).toFloat()

    return Pair(xShift, yShift)
}
