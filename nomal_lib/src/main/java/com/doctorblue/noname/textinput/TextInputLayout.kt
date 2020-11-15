package com.doctorblue.noname.textinput

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
    var hintTextSize: Float = (4f * resources.displayMetrics.density)
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
    var defaultColor: Int = context.resources.getColor(R.color.colorPrimaryDark)
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
     * lineHeight is a custom attribute for LineBottomStyle, ActiveBottomLineStyle
     */
    @Dimension
    var lineHeight: Int = (2f * resources.displayMetrics.density).roundToInt()
        set(@Dimension value) {
            field = value
            if (textInputStyle != null) {
                when (textInputStyle) {
                    is ActiveBottomLineStyle ->
                        (textInputStyle as ActiveBottomLineStyle).lineHeight = value
                    is LineBottomStyle ->
                        (textInputStyle as LineBottomStyle).lineHeight = value
                    else -> throw  IllegalArgumentException("textInputStyle is not LineBottomStyle or ActiveBottomLineStyle !")

                }
            }
        }

    var leftIcon: Int = -1
        set(value) {
            field = value
            if (textInputStyle != null) {
                if (textInputStyle is SwipeLeftIconStyle) {
                    (textInputStyle as SwipeLeftIconStyle).icon = value
                } else {
                    throw  IllegalArgumentException("textInputStyle has no leftIcon property !")
                }
            }
        }

    var text: String = ""

    private var textInputStyle: TextInputStyle? = null
    var hasInputFrame = false

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

            leftIcon = typedArray.getResourceId(R.styleable.TextInputLayout_leftIcon, leftIcon)

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

        /**
         * Check if TextInputLayout has an inputFrame or not, if so, add child to inputFrame.
         *
         */
        if (hasInputFrame) {
            textInputStyle!!.addView(child, index, params)
        } else {
            super.addView(child, index, params)
        }
    }

    @JvmName("setLocalEditText")
    private fun setEditText(editText: EditText) {
        // If we already have an EditText, throw an exception
        require(this.editText == null) { "We already have an EditText, can only have one" }

        if (editText !is TextInputEditText) {
            Log.i(
                LOG_TAG,
                "EditText added is not a TextInputEditText. Please switch to using that"
                        + " class instead."
            )
        }
        this.editText = editText as AppCompatEditText
    }

    companion object {
        const val LOG_TAG = "TextInputEditText"
    }

    override fun onSaveInstanceState(): Parcelable? {
        return textInputStyle?.onSaveInstanceState(super.onSaveInstanceState())
    }

    override fun onRestoreInstanceState(state: Parcelable?) {

        super.onRestoreInstanceState(state)

    }


}