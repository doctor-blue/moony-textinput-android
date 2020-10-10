package com.doctorblue.noname.textinput

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.appcompat.widget.AppCompatEditText
import com.doctorblue.noname_library.R
import kotlin.math.roundToInt

/**
Create by Nguyen Van Tan 9/2020
 */

class TextInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = R.attr.TextInputLayoutStyle
) :
    LinearLayout(context, attrs, defStyleAttr) {


    var editText: AppCompatEditText? = null
    var hintText = TextView(context)

    @Dimension
    var hintTextSize: Float = (3f * resources.displayMetrics.density)
        set(@Dimension value) {
            field = value
            textInputStyle?.hintTextSize = value
        }


    var hint: String = ("Goodbye, world!")
        set(value) {
            field = value
            textInputStyle?.hint = value
        }


    @ColorInt
    var defaultColor: Int = context.resources.getColor(R.color.colorAccent)
        set(@ColorInt value) {
            field = value
            textInputStyle?.defaultColor = value
        }

    @ColorInt
    var activeColor: Int = context.resources.getColor(R.color.colorPrimary)
        set(@ColorInt value) {
            field = value
            textInputStyle?.activeColor = value
        }

    /**
     * lineHeight is a custom attribute for LineBottomStyle
     */
    @Dimension
    var lineHeight: Int = (2f * resources.displayMetrics.density).roundToInt()
        set(@Dimension value) {
            field = value
            if (textInputStyle != null) {
                if (textInputStyle is LineBottomStyle) {
                    (textInputStyle as LineBottomStyle).lineHeight = value
                } else {
                    throw  IllegalArgumentException("textInputStyle is not LineBottomStyle !")
                }
            }
        }

    private var textInputStyle: TextInputStyle? = null

    private fun obtainStyledAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TextInputLayout,
            defStyleAttr,
            0
        )
        // get custom attribute
        try {
            lineHeight = typedArray.getDimension(
                R.styleable.TextInputLayout_line_height,
                lineHeight.toFloat()
            ).toInt()

            activeColor =
                typedArray.getColor(R.styleable.TextInputLayout_active_color, activeColor)

            defaultColor = typedArray.getColor(
                R.styleable.TextInputLayout_default_color,
                defaultColor
            )

            hintTextSize =
                typedArray.getDimension(
                    R.styleable.TextInputLayout_hint_text_size,
                    hintTextSize
                )

            hint = typedArray.getString(R.styleable.TextInputLayout_android_hint) ?: hint

            textInputStyle = TextInputFactory.getStyle(
                typedArray.getInt(
                    R.styleable.TextInputLayout_text_input_style,
                    1
                ), context, this
            )


        } catch (e: Exception) {
            //for test
            //for make sure show the exception before release libs
            throw e
            // e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }


    init {
        orientation = VERTICAL
        this.setWillNotDraw(false)
        this.setAddStatesFromChildren(true)

        obtainStyledAttributes(attrs, defStyleAttr)

    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child is AppCompatEditText) {
            // set edit text
            setEditText(child)
            super.addView(child, index, params)
            editText!!.onFocusChangeListener = textInputStyle
            textInputStyle!!.addView()

        } else {
            super.addView(child, index, params)
        }
    }

    @JvmName("setLocalEditText")
    private fun setEditText(editText: AppCompatEditText) {
        // If we already have an EditText, throw an exception
        require(this.editText == null) { "We already have an EditText, can only have one" }

        if (editText !is TextInputEditText) {
            Log.i(
                LOG_TAG,
                "EditText added is not a TextInputEditText. Please switch to using that"
                        + " class instead."
            )
        }
        this.editText = editText
    }

    companion object {
        const val LOG_TAG = "TextInputEditText"
    }


}