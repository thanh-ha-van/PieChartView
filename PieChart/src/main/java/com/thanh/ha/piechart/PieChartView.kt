package com.thanh.ha.piechart

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.core.content.res.ResourcesCompat
import kotlin.math.roundToInt

class PieChartView : View, PieChart {

    companion object {
        const val ARC_FULL_ROTATION_DEGREE = 360
        const val PERCENTAGE_VALUE_HOLDER = "percentage"
    }

    private var currentScale = 0
    private var startAngle = 0f
    private var textInterDistance = 0

    private lateinit var mainPaint: Paint
    private lateinit var shadowPaint: Paint
    private lateinit var textPaint: Paint
    private lateinit var erasor: Paint

    private val dataList: MutableList<PieItem> = mutableListOf()

    private var radius: Float = 0f
    private var widthFloat: Float = 0f
    private var heightFloat: Float = 0f
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    private val clearMode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

    private var animationTime = 1000L

    private var animationType = 1
    private var holeRadius = 0f
    private var textColor: Int = 0
    private var itemFont: Int = 0
    private var itemTextSize: Float = 0f
    private var isDrawingHole = false
    private var isDrawingText = false
    private var isMultipleShadowColor = true
    private var shadowRadius = 0f
    private var shadowDx = 0f
    private var shadowDy = 0f
    private var shadowAlpha = 0.3f

    private val oval = RectF()

    constructor(context: Context)
            : this(context, null, 0)

    constructor(
        context: Context,
        attributes: AttributeSet
    ) : this(context, attributes, 0)

    constructor(
        context: Context,
        attributes: AttributeSet?,
        @AttrRes defStyleAttr: Int
    ) : super(
        context,
        attributes,
        defStyleAttr
    ) {
        init(attributes)
    }

    override fun submitList(arrayList: List<PieItem>) {
        dataList.clear()
        dataList.addAll(arrayList)
        invalidate()
    }

    private fun init(attributes: AttributeSet?) {
        attributes?.let { initAttributes(it) }
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        initMainPaint()
        initTextPaint()
        initEraser()
    }

    private fun initAttributes(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PieChartView,
            0, 0
        ).apply {
            try {
                animationTime =
                    getInteger(R.styleable.PieChartView_animationDuration, 1000).toLong()
                isMultipleShadowColor =
                    getBoolean(R.styleable.PieChartView_isMultiColorShadow, true)
                animationType = getInteger(R.styleable.PieChartView_animationType, 1)

                textColor = getColor(R.styleable.PieChartView_itemTextColor, Color.WHITE)
                itemTextSize = getDimension(R.styleable.PieChartView_itemTextSize, 14f)

                holeRadius = getDimension(R.styleable.PieChartView_holeRadius, 0f)
                isDrawingHole = getBoolean(R.styleable.PieChartView_isDrawingHole, false)
                isDrawingText = getBoolean(R.styleable.PieChartView_isDrawingText, false)

                shadowRadius = getDimension(R.styleable.PieChartView_shadowRadius, 10f)
                shadowDx = getDimension(R.styleable.PieChartView_shadowDx, 0f)
                shadowDy = getDimension(R.styleable.PieChartView_shadowDy, 10f)
                shadowAlpha = getFloat(R.styleable.PieChartView_shadowAlpha, 0.3f)
                itemFont = getResourceId(R.styleable.PieChartView_textFontFamily, 0)
            } finally {
                recycle()
            }
        }
    }

    private fun calculateValue() {
        textInterDistance = getTextInterDistance()
    }

    private fun initTextPaint() {
        textPaint = Paint()
        textPaint.apply {
            color = textColor
            isFakeBoldText
            isAntiAlias = true
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
            textSize = itemTextSize
            if (itemFont > 0)
                try {
                    typeface = ResourcesCompat.getFont(context, itemFont)
                } catch (ex: Exception) {

                }
        }
    }

    private fun initEraser() {
        erasor = Paint()
        erasor.apply {
            isAntiAlias = true
            xfermode = clearMode
        }
    }

    private fun initMainPaint() {
        mainPaint = Paint()
        mainPaint.apply {
            isAntiAlias = true
            color = Color.BLACK
            style = Paint.Style.FILL
        }
        shadowPaint = Paint()
        shadowPaint.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            alpha = ((shadowAlpha * 256) % 256).roundToInt()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        radius = calculateRadius(measuredWidth.toFloat(), measuredHeight.toFloat())
        calculateValue()
    }

    private fun calculateRadius(width: Float, height: Float): Float {
        centerX = width / 2
        centerY = height / 2
        widthFloat = width
        heightFloat = height
        return if (width > height) {
            height / 2
        } else {
            width / 2
        } - shadowDx - shadowDy - shadowRadius
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(widthFloat / 2, heightFloat / 2)
        drawShadow(canvas)
        drawHole(canvas)
        drawPieItems(canvas)
        drawTextItems(canvas)
    }

    private fun drawShadow(canvas: Canvas) {
        startAngle = 0f
        oval.set(-radius, -radius, radius, radius)
        for (item in dataList) {
            canvas.drawArc(
                oval,
                startAngle * getCurrentPercent(),
                getItemValue(item),
                true,
                shadowPaint.apply {
                    setShadowLayer(shadowRadius, shadowDx, shadowDy, item.color)
                })
            startAngle += item.value
        }
    }

    private fun drawHole(canvas: Canvas) {
        canvas.drawCircle(0f, 0f, radius, erasor)
    }

    private fun drawPieItems(canvas: Canvas) {
        startAngle = 0f
        oval.set(-radius, -radius, radius, radius)
        for (item in dataList) {
            canvas.drawArc(
                oval,
                startAngle * getCurrentPercent(),
                getItemValue(item),
                true,
                mainPaint.apply {
                    setShadowLayer(0f, 0f, 0f, item.color)
                    color = item.color
                })
            startAngle += item.value
        }
        canvas.drawCircle(0f, 0f, holeRadius, erasor)
    }

    private fun drawTextItems(canvas: Canvas) {
        startAngle = 0f
        for (item in dataList) {
            val angle = getAngleBetween(item)
            canvas.translateBy(angle, textInterDistance)
            val list = item.text.split('\n')
            list.forEachIndexed { index, string ->
                canvas.drawText(string, 0f, itemTextSize * (index + 0.5f), textPaint)
            }
            startAngle += item.value
            canvas.translateBy(angle, -textInterDistance)
        }
    }

    private fun getTextInterDistance() =
        (((radius - holeRadius) / 2 + holeRadius) + itemTextSize / 2).toInt()

    private fun getCurrentPercent() = currentScale.toFloat() / ARC_FULL_ROTATION_DEGREE

    private fun getAngleBetween(item: PieItem) =
        (startAngle + item.value / 2) * getCurrentPercent()

    private fun getItemValue(item: PieItem) = item.value * getCurrentPercent()

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
}
