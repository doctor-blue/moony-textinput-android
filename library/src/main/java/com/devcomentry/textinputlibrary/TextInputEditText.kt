package com.devcomentry.textinputlibrary

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText


/**
 * Create by Nguyen Van Tan 9/2020
 */
class TextInputEditText constructor(
    context: Context,
    attributes: AttributeSet
) : AppCompatEditText(context, attributes) {


    init {
        setBackgroundColor(Color.TRANSPARENT)
    }

    val textInputLayout: TextInputLayout?
        get() {
            var parent = parent
            while (parent is View) {
                if (parent is TextInputLayout) {
                    return parent
                }
                parent = this.parent.parent
            }
            return null
        }


    override fun getHint(): CharSequence? {
        // Certain test frameworks expect the actionable element to expose its hint as a label. When
        // TextInputLayout is providing our hint, retrieve it from the parent layout.
        return if (textInputLayout != null) {
            textInputLayout!!.hint
        } else super.getHint()
    }


}