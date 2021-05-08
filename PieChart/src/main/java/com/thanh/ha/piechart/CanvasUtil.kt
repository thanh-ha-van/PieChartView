package com.thanh.ha.piechart

import android.graphics.Canvas
import kotlin.math.cos
import kotlin.math.sin

/**
 * Translate canvas by a vector which is a combination of an angle and a distance.
 * @param angle: angle start by 0 degree in mathematics
 * @param distance: in px
 * @return Unit
 * Example: run with by +60° == PI/3 in radian with distance = 100px:
 * will translate x by 50px and y by approximate 86px
 */

fun Canvas.translateBy(angle: Float, distance: Int) {

    val pureDegree = angle % 360

    val degree = Math.toRadians(pureDegree.toDouble())

    val xShift = (cos(degree) * distance).toFloat()

    val yShift = (sin(degree) * distance).toFloat()

    this.translate(xShift, yShift)
}

/**
 * Return x and x of a vector starting at center of coordinate system
 * @param angle: angle start by 0 degree in mathematics
 * @param distance: in px
 * @return Pair<X,Y>
 */
fun getVectorXY(angle: Float, distance: Int): Pair<Float, Float> {

    val pureDegree = angle % 360

    val degree = Math.toRadians(pureDegree.toDouble())

    val xShift = (cos(degree) * distance).toFloat()

    val yShift = (sin(degree) * distance).toFloat()

    return Pair(xShift, yShift)
}
