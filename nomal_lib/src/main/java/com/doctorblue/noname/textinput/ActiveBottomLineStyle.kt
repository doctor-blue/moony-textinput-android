package com.doctorblue.noname.textinput

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart

class ActiveBottomLineStyle(context: Context, textInputLayout: TextInputLayout) :
    TextInputStyle(context, textInputLayout) {

    @Dimension
    override var hintTextSize: Float = super.hintTextSize
        set(@Dimension value) {
            field = value
            hintText.textSize = value
        }


    override var hint: String = super.hint
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
                if (activeLine != null) {
                    activeLine!!.setBackgroundColor(value)
                }
                hintText.setTextColor(value)
            }
        }

    @Dimension
    var lineHeight: Int = textInputLayout.lineHeight
        set(@Dimension value) {
            field = value
            lineLP.height = value
            activeLineLP.height = value * 2

            activeLine?.let {
                it.layoutParams = activeLineLP
            }

            line?.let {
                it.layoutParams = lineLP
            }

        }


    private var line: View? = null
    private val lineLP =
        RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, lineHeight)

    private var activeLine: View? = null
    private val activeLineLP =
        RelativeLayout.LayoutParams(0, lineHeight * 2)

    init {

    }

    override fun setupInputFrame() {
        inputFrame = RelativeLayout(context)
    }

    override fun onEditTextAdded() {
        super.onEditTextAdded()
        val editTextLP = editText?.layoutParams as RelativeLayout.LayoutParams

        editTextLP.topMargin = (hintTextSize * 3).toInt()
        editText!!.layoutParams = editTextLP

        //add Line hint and Active Line
        initHintAndLine()
        inputFrame.addView(hintText)
        inputFrame.addView(line)
        inputFrame.addView(activeLine)
    }

    private fun initHintAndLine() {
        lineLP.addRule(RelativeLayout.BELOW, editText?.id!!)
        lineLP.topMargin = lineHeight * 2

        activeLineLP.addRule(RelativeLayout.BELOW, editText?.id!!)
        activeLineLP.topMargin = lineHeight * 2

        line = View(context).apply {
            layoutParams = lineLP
            setBackgroundColor(defaultColor)
        }

        activeLine = View(context).apply {
            layoutParams = activeLineLP
            setBackgroundColor(activeColor)
        }

        hintText.text = hint
        hintText.setTextColor(defaultColor)
        hintText.textSize = hintTextSize * 1.5f

        hintText.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            // addRule(RelativeLayout.CENTER_VERTICAL)
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        this.hasFocus = hasFocus
        if (hasFocus) {
            onFocus()
        } else {
            onUnFocus()
        }
    }

    private val defaultHintX by lazy {
        hintText.x
    }
    private val defaultHintY by lazy {
        hintText.y
    }

    private fun onFocus() {
        defaultHintY
        animateBottomLine(0f, line?.width?.toFloat() ?: inputFrame.width.toFloat()).apply {
            doOnStart {
                animateHint(defaultHintX, hintText.x + hintText.width).apply {
                    animateAlpha(1f, 0f).start()
                    doOnEnd {
                        animateHint(0f, defaultHintX + defaultHintX / 2f).apply {
                            doOnStart {
                                hintText.setTextColor(activeColor)
                                hintText.textSize = hintTextSize / 1.1f
                                hintText.y = 0f
                                animateAlpha(0f, 1f).start()
                            }
                            start()
                        }
                    }
                    start()
                }
            }

            start()
        }
    }

    private fun onUnFocus() {
        animateBottomLine(activeLine!!.width.toFloat(), 0f).apply {
            doOnStart {
                animateHint(hintText.x, 0f).apply {
                    animateAlpha(1f, 0f).start()
                    doOnEnd {
                        animateHint(0f - hintText.width, defaultHintX).apply {
                            doOnStart {
                                hintText.textSize = hintTextSize * 1.5f
                                hintText.y = defaultHintY
                                hintText.setTextColor(defaultColor)
                                animateAlpha(0f, 1f).start()
                            }
                            start()
                        }
                    }
                    start()
                }
            }

            start()
        }
    }

    private fun animateBottomLine(start: Float, end: Float) =
        ValueAnimator.ofFloat(start, end).apply {
            duration = 150
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                activeLineLP.width = (it.animatedValue as Float).toInt()
                activeLine?.layoutParams = activeLineLP
            }
        }

    private fun animateAlpha(start: Float, end: Float): ValueAnimator =
        ValueAnimator.ofFloat(start, end).apply {
            duration = 200
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                hintText.alpha = it.animatedValue as Float
            }
        }

    private fun animateHint(start: Float, end: Float) =
        ValueAnimator.ofFloat(start, end).apply {
            duration = 200
            addUpdateListener {
                hintText.x = it.animatedValue as Float
            }
        }
}