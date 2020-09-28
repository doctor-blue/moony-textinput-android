package com.doctorblue.noname.textinput

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
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
    defStyleAttr: Int = R.attr.LineBottomTextInputLayoutStyle,
) : TextInputLayout(context, attrs, defStyleAttr) {


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

    @Dimension
    private var _hintTextSize: Float = 0f

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

    private var hintText = TextView(context)
    private var line = View(context)
    private val lineLP = LayoutParams(LayoutParams.MATCH_PARENT, lineHeight * 2)
    private var hasFocus: Boolean = false

    init {
        lineLP.topMargin = (2f * resources.displayMetrics.density).roundToInt()
        obtainStyledAttributes(attrs, defStyleAttr)
    }


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
                typedArray.getDimension(R.styleable.LineBottomTextInputLayout_hint_text_size, 0f)

            hint = typedArray.getString(R.styleable.LineBottomTextInputLayout_hint) ?: hint

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }

        line.setBackgroundColor(defaultColor)

        line.layoutParams = lineLP
        addView(line)


        hintText.text = hint
        hintText.setTextColor(defaultColor)
        hintText.textSize = _hintTextSize
        hintText.typeface = Typeface.DEFAULT_BOLD
        addView(hintText)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        editText?.layoutParams = FrameLayout.LayoutParams(w, h * 2 / 3)

        if (_hintTextSize == 0f) {
            hintText.textSize = h / 10f
            _hintTextSize = hintText.textSize
        }

    }

    private var defaultHintPos = 0f
    private var defaultLinePos = 0f
    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        defaultHintPos = if (defaultHintPos == 0f) hintText.y else defaultHintPos
        defaultLinePos = if (defaultLinePos == 0f) line.y else defaultLinePos
        this.hasFocus = hasFocus

        if (hasFocus) {
            onFocus()
        } else {
            onUnFocus()
        }

    }


    private fun onFocus() {
        // start animation
        startAnim(defaultHintPos, defaultLinePos - hintText.height).apply {
            doOnStart {
                animateAlpha(1f, 0f).start()
            }
            doOnEnd {
                startAnim(defaultHintPos + hintText.height, defaultHintPos + lineHeight * 3).apply {
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
        startAnim(defaultHintPos, defaultHintPos + hintText.height).apply {
            doOnStart {
                animateAlpha(1f, 0f).start()
            }
            doOnEnd {
                startAnim(defaultLinePos - hintText.height, defaultHintPos + lineHeight).apply {
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

}