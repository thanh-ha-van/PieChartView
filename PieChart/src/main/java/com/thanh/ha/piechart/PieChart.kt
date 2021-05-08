package com.thanh.ha.piechart

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import kotlin.math.max
import kotlin.math.roundToInt

class PieChart : View {

    companion object {
        const val ARC_FULL_ROTATION_DEGREE = 360
        const val PERCENTAGE_VALUE_HOLDER = "percentage"
    }

    // for animation properties
    private var currentScale = 360
    private var initAngle = 0f
    private var currentAngle = 0f
    private var textInterDistance = 0

    private lateinit var mainPaint: Paint
    private lateinit var shadowPaint: Paint
    private lateinit var textPaint: Paint
    private lateinit var erasor: Paint

    private val dataList: MutableList<PieItem> = mutableListOf()

    private var radius: Float = 0f
    private var widthFloat: Float = 0f
    private var heightFloat: Float = 0f

    private var animationTime = 1000L

    private var animationType = 0
    private var textColor: Int = 0
    private var itemFont: Int = 0
    private var itemTextSize: Float = 0f
    private var isMultipleShadowColor = true
    private var shadowRadius = 0f
    private var shadowDx = 0f
    private var shadowDy = 0f
    private var shadowAlpha = 0.3f
    private var strokeWidth = 0f
    private var clockWiseMultiplier = 1

    private val oval = RectF()

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attributes: AttributeSet) : this(context, attributes, 0)

    constructor(context: Context, attributes: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attributes,
        defStyleAttr
    ) {
        init(attributes)
        if (isInEditMode) {
            val fakeList2 = listOf(
                PieItem(120f, Color.RED),
                PieItem(120f, Color.GREEN),
                PieItem(120f, Color.BLUE),
            )
            submitList(fakeList2)
        }
    }

    fun submitList(arrayList: List<PieItem>) {
        dataList.clear()
        if (clockWiseMultiplier == -1) {
            dataList.addAll(arrayList.map {
                it.copy(
                    value = -it.value
                )
            })
        }
        dataList.addAll(arrayList)
        invalidate()
    }

    fun animateProgress(from: Int, to: Int) {
        val valuesHolder = PropertyValuesHolder.ofInt(PERCENTAGE_VALUE_HOLDER, from, to)
        val animator = ValueAnimator().apply {
            setValues(valuesHolder)
            duration = animationTime
            addUpdateListener {
                val percentage = it.getAnimatedValue(PERCENTAGE_VALUE_HOLDER) as Int
                currentScale = percentage
                invalidate()
            }
        }
        animator.start()
    }

    private fun init(attributes: AttributeSet?) {
        attributes?.let { initAttributes(it) }
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        initPaints()
    }

    private fun initAttributes(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PieChart,
            0, 0
        ).apply {
            try {
                animationTime =
                    getInteger(R.styleable.PieChart_animationDuration, 1000).toLong()
                isMultipleShadowColor =
                    getBoolean(R.styleable.PieChart_isMultiColorShadow, true)
                animationType = getInteger(R.styleable.PieChart_animationType, 0)
                initAngle = getInteger(R.styleable.PieChart_initAngle, 0).toFloat()
                textColor = getColor(R.styleable.PieChart_itemTextColor, Color.WHITE)
                itemTextSize = getDimension(R.styleable.PieChart_itemTextSize, 14f)

                shadowRadius = getDimension(R.styleable.PieChart_shadowRadius, 0f)
                shadowDx = getDimension(R.styleable.PieChart_shadowDx, 0f)
                shadowDy = getDimension(R.styleable.PieChart_shadowDy, 0f)
                shadowAlpha = getFloat(R.styleable.PieChart_shadowAlpha, 0.3f)
                itemFont = getResourceId(R.styleable.PieChart_textFontFamily, 0)
                strokeWidth = getDimension(R.styleable.PieChart_pieStrokeWidth, 100f)
                clockWiseMultiplier = getInteger(R.styleable.PieChart_animateDirection, 1)
            } finally {
                recycle()
            }
        }
    }

    private fun calculateValues() {
        widthFloat = measuredWidth.toFloat()
        heightFloat = measuredWidth.toFloat()
        radius = calculateRadius(measuredWidth.toFloat(), measuredHeight.toFloat())
        strokeWidth = strokeWidth.coerceAtMost(radius)
        val ovalRadius = radius - strokeWidth / 2
        textInterDistance = (radius - strokeWidth / 2).toInt()
        oval.set(
            -ovalRadius,
            -ovalRadius,
            ovalRadius,
            ovalRadius
        )
    }

    private fun initPaints() {
        with(PaintManager) {
            mainPaint = initMainPaint(strokeWidth)
            shadowPaint = initShadowPaint(shadowAlpha, strokeWidth)
            textPaint = initTextPaint(context, textColor, itemTextSize, itemFont)
            erasor = initEraser()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        calculateValues()
    }

    private fun calculateRadius(width: Float, height: Float): Float {
        return if (width > height) {
            height / 2
        } else {
            width / 2
        } - (max(shadowDx, shadowDy) + shadowRadius)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // translate into center of view
        canvas.translate(widthFloat / 2, heightFloat / 2)
        if (isMultipleShadowColor) {
            drawShadow(canvas)
        } else {
            drawSingleShadow(canvas)
        }
        drawPieItems(canvas)
        drawTextItems(canvas)
    }

    private fun drawSingleShadow(canvas: Canvas) {
        resetAngle()
        canvas.drawArc(
            oval,
            getStarAngle(),
            currentScale.toFloat() * clockWiseMultiplier,
            false,
            shadowPaint.apply {
                alpha = ((shadowAlpha * 256) % 256).roundToInt()
                setShadowLayer(shadowRadius, shadowDx, shadowDy, Color.BLACK)
            })
    }

    private fun drawShadow(canvas: Canvas) {
        resetAngle()
        for (item in dataList) {
            canvas.drawArc(
                oval,
                getStarAngle(),
                getItemValue(item),
                false,
                shadowPaint.apply {
                    setShadowLayer(shadowRadius, shadowDx, shadowDy, item.color)
                })
            currentAngle += item.value
        }
    }

    private fun drawPieItems(canvas: Canvas) {
        resetAngle()
        for (item in dataList) {
            canvas.drawArc(
                oval,
                getStarAngle(),
                getItemValue(item),
                false,
                mainPaint.apply {
                    setShadowLayer(0f, 0f, 0f, item.color)
                    color = item.color
                })
//            val xyStart =
//                getVectorXY(getStarAngle() + getItemValue(item), (radius - strokeWidth).toInt())
//            val xyEnd = getVectorXY(getStarAngle() + getItemValue(item), radius.toInt())
            //  canvas.drawLine(xyStart.first, xyStart.second, xyEnd.first, xyEnd.second, erasor)
            currentAngle += item.value


        }
    }

    private fun drawTextItems(canvas: Canvas) {
        resetAngle()
        for (item in dataList) {
            val angle = getAngleBetween(item)
            canvas.translateBy(angle, textInterDistance)
            val list = item.text.split('\n')
            list.forEachIndexed { index, string ->
                canvas.drawText(string, 0f, itemTextSize * (index + 0.5f), textPaint)
            }
            currentAngle += item.value
            canvas.translateBy(angle, -textInterDistance)
        }
    }

    private fun getStarAngle() =
        currentAngle * getCurrentPercent() + (1 - getCurrentPercent()) * initAngle

    private fun resetAngle() {
        currentAngle = initAngle
    }

    private fun getCurrentPercent() = currentScale.toFloat() / ARC_FULL_ROTATION_DEGREE

    private fun getAngleBetween(item: PieItem) =
        getStarAngle() + item.value / 2 * getCurrentPercent()

    private fun getItemValue(item: PieItem) = item.value * getCurrentPercent()

}
