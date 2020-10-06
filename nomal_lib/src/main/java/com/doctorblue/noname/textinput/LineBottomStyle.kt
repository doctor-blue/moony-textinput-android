package com.doctorblue.noname.textinput

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import kotlin.math.roundToInt

class LineBottomStyle(context: Context, textInputLayout: TextInputLayout) :
    TextInputStyle(context, textInputLayout) {


    @Dimension
    private var _hintTextSize: Float = (3f * context.resources.displayMetrics.density)

    @Dimension
    override var hintTextSize: Float = _hintTextSize
        set(@Dimension value) {
            _hintTextSize = value
            hintText.textSize = value
            field = value
        }


    override var hint: String = ("Goodbye, world!")
        set(value) {
            field = value
            hintText.text = value
        }


    @ColorInt
    override var defaultColor: Int = super.defaultColor
        set(@ColorInt value) {
            field = value
            if (!hasFocus) {
                if (line != null) {
                    line!!.setBackgroundColor(value)
                }
                hintText.setTextColor(value)
            }
        }

    @ColorInt
    override var activeColor: Int = super.activeColor
        set(@ColorInt value) {
            field = value
            if (hasFocus) {
                if (line != null) {
                    line!!.setBackgroundColor(value)
                }
                hintText.setTextColor(value)
            }
        }

    @Dimension
    var lineHeight: Int = (2f * context.resources.displayMetrics.density).roundToInt()
        set(@Dimension value) {
            field = value
            if (line != null) {
                lineLP.height = value
            }
        }


    private var line: View? = null
    private val lineLP =
        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, lineHeight * 2)
    private var defaultHintY = 0f
    private var defaultLineY = 0f
    private var hasHint = false

    init {
        line = View(context)
        line!!.setBackgroundColor(defaultColor)
        line!!.layoutParams = lineLP
        lineLP.topMargin = (2f * context.resources.displayMetrics.density).roundToInt()
    }


    override fun addView() {
        //add line and hint
        if (!hasHint) {
            initHintAndLine()
            textInputLayout.addView(line)
            textInputLayout.addView(hintText)
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        defaultHintY = if (defaultHintY == 0f) hintText.y else defaultHintY
        defaultLineY = line!!.y
        this.hasFocus = hasFocus

        if (hasFocus) {
            onFocus()
        } else {
            onUnFocus()
        }
    }

    private fun initHintAndLine() {
        hintText.text = hint
        val a = Color.GRAY
        hintText.setTextColor(defaultColor)
        hintText.textSize = _hintTextSize
        hintText.typeface = Typeface.DEFAULT_BOLD
    }

    private fun onFocus() {
        // start animation
        startAnim(hintText.y, hintText.y - hintText.height).apply {
            doOnStart {
                animateAlpha(1f, 0f).start()
            }
            doOnEnd {
                startAnim(line!!.y + hintText.height, line!!.y + lineHeight).apply {
                    doOnStart {
                        animateAlpha(0f, 1f).start()
                        //Change text and line color
                        lineLP.height = lineHeight * 2
                        line!!.layoutParams = lineLP
                        line!!.setBackgroundColor(activeColor)
                        hintText.setTextColor(activeColor)
                    }
                    start()
                }
            }
            start()
        }
    }


    private fun onUnFocus() {
        startAnim(hintText.y, hintText.y - hintText.height).apply {
            doOnStart {
                animateAlpha(1f, 0f).start()
            }
            doOnEnd {
                startAnim(line!!.y + hintText.height, line!!.y).apply {
                    doOnStart {
                        animateAlpha(0f, 1f).start()

                        // reset to default
                        lineLP.height = lineHeight
                        line!!.layoutParams = lineLP
                        line!!.setBackgroundColor(defaultColor)
                        hintText.setTextColor(defaultColor)
                    }
                    start()
                }
            }
            start()
        }
    }


    private fun startAnim(start: Float, end: Float): ValueAnimator =
        ValueAnimator.ofFloat(start, end).apply {
            duration = 150
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                hintText.y = it.animatedValue as Float
            }
        }


    private fun animateAlpha(start: Float, end: Float): ValueAnimator =
        ValueAnimator.ofFloat(start, end).apply {
            duration = 75
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                hintText.alpha = it.animatedValue as Float
            }
        }
}