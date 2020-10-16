package com.doctorblue.noname.tabs_lib

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import androidx.annotation.XmlRes
import kotlin.math.roundToInt


/**
 * Create by Nguyen Van Tan 9/2020
 */
class TabItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.TabLayoutStyle,
) : LinearLayout(context, attrs, defStyleAttr) {
    @ColorInt
    var barBackground: Int = Color.WHITE
        set(@ColorInt value) {
            field = value
            invalidate()
        }

    @XmlRes
    var itemMenuRes: Int = INVALID_RES
        set(@XmlRes value) {
            field = value
            if (value != INVALID_RES) {
                invalidate()
            }
        }

    @Dimension
    var itemIconSize: Float = (24f * resources.displayMetrics.density).roundToInt().toFloat()
        set(@Dimension value) {
            field = value
            invalidate()
        }

    @ColorInt
    var activeColor: Int = Color.BLACK
        set(@ColorInt value) {
            field = value
            invalidate()
        }

    @ColorInt
    var defaultColor: Int = Color.GRAY
        set(@ColorInt value) {
            field = value
            invalidate()
        }

    @Dimension(unit = Dimension.SP)
    var textSize: Float = 14f
        set(@Dimension value) {
            field = value
            textView.textSize = value
        }

    var text: String = ""
        set(value) {
            field = value
            textView.text = shortenText(value)
        }

    @DrawableRes
    var imageRes: Int = -1
        set(value) {
            field = value
            icon.setImageResource(value)
        }

    var tab: Tab? = null
    var position = -1
    var isClicked = false

    private var textView: TextView = TextView(context)
    private var icon: ImageView = ImageView(context)
    private val tvLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    private val iconLayoutParams =
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)


    // Listeners
    var onItemSelectedListener: OnItemSelectedListener? = null

    var onItemReselectedListener: OnItemReselectedListener? = null

    var onItemSelected: ((Int?) -> Unit)? = null

    var onItemReselected: ((Int?) -> Unit)? = null

    init {
        obtainStyledAttributes(attrs, defStyleAttr)
        initTextView()
        addView(textView)
        initIcon()
        addView(icon)
        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        setBackgroundColor(Color.GREEN)

        //Handle Item Selected or ReSelected
        setOnClickListener {
            if (!isClicked) {
                onSelected()
                (parent as TabBar).resetTabStatus(position)
                onItemSelected?.invoke(position)
                onItemSelectedListener?.onItemSelect(tab?.id, position)
                isClicked=true
            } else {
                onItemReselected?.invoke(position)
                onItemReselectedListener?.onItemReselect(tab?.id, position)
            }
        }
    }

    private fun obtainStyledAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TabItem,
            defStyleAttr,
            0
        )
        // get custom attribute
        try {
            imageRes = typedArray.getResourceId(R.styleable.TabItem_android_src, imageRes)
            text = typedArray.getString(R.styleable.TabItem_title) ?: text


        } catch (e: Exception) {
            //for test
            //for make sure show the exception before release libs
            throw e
            // e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    private fun initIcon() {
        icon.setColorFilter(defaultColor)
        icon.setImageResource(imageRes)
    }

    private fun initTextView() {
        textView.text = shortenText(text)
        textView.maxLines = 2
        textView.gravity = Gravity.CENTER
        textView.setTextColor(activeColor)
        textView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, textSize)
    }

    private fun shortenText(text: String): String =
        if (text.length > 10) text.substring(0, 10) + "..." else text


    var isChildReady = false
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (!isChildReady) {
            tvLayoutParams.width = width - width / 3
            textView.layoutParams = tvLayoutParams
            textView.y = 0f - textView.height

            val iconSize = width / 4
            iconLayoutParams.width = iconSize
            iconLayoutParams.height = iconSize
            icon.layoutParams = iconLayoutParams
            icon.y = height / 4f

        }
    }


    private var defaultIconY = 0f
    private var textViewY = 0f
    private fun onSelected() {
        if (defaultIconY == 0f) defaultIconY = icon.y
        if (textViewY == 0f) textViewY = height / 4f - height / 6f
        isChildReady = true
        ValueAnimator.ofFloat(textView.y, textViewY).apply {
            duration = 150
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                textView.y = it.animatedValue as Float
            }
        }.start()
        ValueAnimator.ofFloat(icon.y, height.toFloat()).apply {
            duration = 150
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                icon.y = it.animatedValue as Float
            }
        }.start()


    }

    fun onReset() {
        if (isClicked) {
            ValueAnimator.ofFloat(textViewY, 0f-textView.height).apply {
                duration = 150
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    textView.y = it.animatedValue as Float
                }
            }.start()
            ValueAnimator.ofFloat(height.toFloat(), defaultIconY).apply {
                duration = 150
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    icon.y = it.animatedValue as Float
                }
            }.start()
            isClicked=false
        }
    }


    companion object {
        private const val INVALID_RES = -1
    }

}