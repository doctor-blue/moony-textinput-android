package com.doctorblue.noname.textinput

import android.animation.ValueAnimator
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.animation.doOnStart
import kotlin.math.roundToInt

class SwipeLeftIconStyle(context: Context, textInputLayout: TextInputLayout) :
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
                hintText.setTextColor(value)
            }
        }

    @ColorInt
    override var activeColor: Int = super.activeColor
        set(@ColorInt value) {
            field = value
            if (hasFocus) {
                hintText.setTextColor(value)
            }
        }

    @Dimension
    var iconSize: Int = (24f * context.resources.displayMetrics.density).roundToInt()
        set(value) {
            field = value
            leftIconLP.width = value
            leftIconLP.height = value
            leftIcon.layoutParams = leftIconLP
        }

    var icon: Int = textInputLayout.leftIcon
        set(value) {
            field = value
            leftIcon.setImageResource(value)
        }

    private val hintFrame = RelativeLayout(context)
    private var hintFrameLP = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT
    )
    private val leftIcon = ImageView(context)
    private val leftIconLP = RelativeLayout.LayoutParams(iconSize, iconSize)

    init {
        hintFrameLP.weight = 3.5F
        hintFrame.layoutParams = hintFrameLP
        (inputFrame as LinearLayout).apply {
            orientation = LinearLayout.HORIZONTAL
        }
        // hintFrame.setBackgroundColor(Color.BLACK)
    }


    override fun onAddEditText() {
        super.onAddEditText()
        inputFrame.addView(hintFrame)
        initIconAndHint()
    }

    override fun onEditTextAdded() {
        super.onEditTextAdded()
        val editTextLP = editText!!.layoutParams as LinearLayout.LayoutParams
        editTextLP.weight = 0.8F
        editText!!.layoutParams = editTextLP
    }

    private fun initIconAndHint() {
        hintText.text = hint
        hintText.setTextColor(defaultColor)
        hintText.textSize = hintTextSize
        hintText.gravity = Gravity.CENTER

        hintFrame.addView(hintText)
        val lp = (hintText.layoutParams as RelativeLayout.LayoutParams)
        lp.addRule(RelativeLayout.CENTER_IN_PARENT)
        hintText.layoutParams = lp
        hintText.visibility = View.INVISIBLE


        leftIcon.setImageResource(icon)
        leftIcon.setColorFilter(activeColor)
        leftIconLP.addRule(RelativeLayout.CENTER_IN_PARENT)
        leftIcon.layoutParams = leftIconLP
        hintFrame.addView(leftIcon)
    }


    private val defaultHintX by lazy {
        hintText.x
    }
    private val defaultIconX by lazy {
        leftIcon.x
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        this.hasFocus = hasFocus

        if (hasFocus) {
            onFocus()
        } else {
            onUnFocus()
        }
    }

    private fun onFocus() {
        // start animation
        if (hintText.visibility == View.INVISIBLE)
            hintText.visibility = View.VISIBLE


        startAnim(0f - hintText.width, defaultHintX, hintText).apply {
            doOnStart {
                animateAlpha(0f, 1f, hintText).start()
            }
            start()
        }

        startAnim(defaultIconX, hintFrame.width + leftIcon.width.toFloat(), leftIcon).apply {
            doOnStart {
                animateAlpha(1f, 0f, leftIcon).start()
            }
            start()
        }
    }


    private fun onUnFocus() {
        startAnim(hintText.x, 0f - hintText.width, hintText).apply {
            doOnStart {
                animateAlpha(1f, 0f, hintText).start()
            }
            start()
        }
        startAnim(hintFrame.width + leftIcon.width.toFloat(), defaultIconX, leftIcon).apply {
            doOnStart {
                animateAlpha(0f, 1f, leftIcon).start()
            }
            start()
        }
    }

    private fun startAnim(start: Float, end: Float, view: View): ValueAnimator =
        ValueAnimator.ofFloat(start, end).apply {
            duration = 150
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                view.x = it.animatedValue as Float
            }
        }


    private fun animateAlpha(start: Float, end: Float, view: View): ValueAnimator =
        ValueAnimator.ofFloat(start, end).apply {
            duration = 75
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                view.alpha = it.animatedValue as Float
            }
        }

}