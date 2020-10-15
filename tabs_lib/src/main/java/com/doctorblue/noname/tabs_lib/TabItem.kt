package com.doctorblue.noname.tabs_lib

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
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
    var textSize: Float = (12f * resources.displayMetrics.density).roundToInt().toFloat()
        set(@Dimension value) {
            field = value
            textView.textSize = value
        }

    var text: String = "NoNameTabsdjjhk"
        set(value) {
            field = value
            textView.text = value
        }

    private var textView: TextView = TextView(context)
    private var icon: ImageView = ImageView(context)

    init {
        textView.text = text
        textView.maxLines = 2
        textView.gravity = Gravity.CENTER
        addView(textView)
        gravity = Gravity.CENTER_HORIZONTAL
        setBackgroundColor(Color.BLUE)
    }


    companion object {
        private const val INVALID_RES = -1
        private const val DEFAULT_INDICATOR_COLOR = Color.GRAY
        private const val OPAQUE = 255
        private const val TRANSPARENT = 0
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        val w = width - width/3
        textView.width = w
        val  tw = textView.width
        val  a =0

    }
}