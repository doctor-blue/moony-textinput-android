package com.doctorblue.noname.textinput

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.doctorblue.noname_library.R
import kotlin.math.roundToInt

/**
Create by Nguyen Van Tan 9/2020
 */

class LineBottomTextInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.LineBottomTextInputLayoutStyle
) :
    LinearLayout(context, attrs, defStyleAttr), View.OnFocusChangeListener {
    var editText: AppCompatEditText? = null
    private var hintText = TextView(context)

    @Dimension
    private var _hintTextSize: Float = (3f * resources.displayMetrics.density)

    @Dimension
    var hintTextSize: Float = _hintTextSize
        set(@Dimension value) {
            _hintTextSize = value
            hintText.textSize = value
            field = value
        }


    var hint: String = ("Goodbye, world")
        set(value) {
            field = value
            hintText.text = value
        }


    @ColorInt
    var defaultColor: Int = Color.GRAY
        set(@ColorInt value) {
            field = value
            if (!hasFocus) {
                line.setBackgroundColor(value)
                hintText.setTextColor(value)
            }
        }

    @ColorInt
    var activeColor: Int = context.resources.getColor(R.color.colorPrimary)
        set(@ColorInt value) {
            field = value
            if (hasFocus) {
                line.setBackgroundColor(value)
                hintText.setTextColor(value)
            }
        }

    @Dimension
    var lineHeight: Int = (2f * resources.displayMetrics.density).roundToInt()
        set(@Dimension value) {
            field = value
            lineLP.height = value
        }

    private var line = View(context)
    private val lineLP = LayoutParams(LayoutParams.MATCH_PARENT, lineHeight * 2)
    private var hasFocus: Boolean = false
    private var defaultHintY = 0f
    private var defaultLineY = 0f
    private var hasHint = false


    private fun obtainStyledAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LineBottomTextInputLayout,
            defStyleAttr,
            0
        )
        // get custom attribute
        try {
            lineHeight = typedArray.getDimension(
                R.styleable.LineBottomTextInputLayout_line_height,
                lineHeight.toFloat()
            ).toInt()

            activeColor =
                typedArray.getColor(R.styleable.LineBottomTextInputLayout_active_color, activeColor)

            defaultColor = typedArray.getColor(
                R.styleable.LineBottomTextInputLayout_default_color,
                defaultColor
            )

            _hintTextSize =
                typedArray.getDimension(
                    R.styleable.LineBottomTextInputLayout_hint_text_size,
                    _hintTextSize
                )

            hint = typedArray.getString(R.styleable.LineBottomTextInputLayout_hint) ?: hint

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }

        line.setBackgroundColor(defaultColor)

        line.layoutParams = lineLP
    }


    init {
        orientation = VERTICAL
        this.setWillNotDraw(false)
        this.setAddStatesFromChildren(true)

        lineLP.topMargin = (2f * resources.displayMetrics.density).roundToInt()

        obtainStyledAttributes(attrs, defStyleAttr)

    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child is AppCompatEditText) {
            // set edit text
            this.editText = child
            super.addView(child, index, params)
            child.setOnFocusChangeListener(this)

            //add line and hint
            if (!hasHint) {
                initHintAndLine()
                addView(line)
                addView(hintText)
            }
        } else {
            super.addView(child, index, params)
        }
    }

    private fun initHintAndLine() {
        hintText = TextView(context)
        hintText.text = hint
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
                startAnim(line.y + hintText.height, line.y).apply {
                    doOnStart {
                        animateAlpha(0f, 1f).start()
                        //Change text and line color
                        lineLP.height = lineHeight * 2
                        line.layoutParams = lineLP
                        line.setBackgroundColor(activeColor)
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
                startAnim(line.y + hintText.height, line.y).apply {
                    doOnStart {
                        animateAlpha(0f, 1f).start()

                        // reset to default
                        lineLP.height = lineHeight
                        line.layoutParams = lineLP
                        line.setBackgroundColor(defaultColor)
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

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        defaultHintY = if (defaultHintY == 0f) hintText.y else defaultHintY
        defaultLineY = line.y
        this.hasFocus = hasFocus

        if (hasFocus) {
            onFocus()
        } else {
            onUnFocus()
        }
    }

}