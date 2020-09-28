package com.doctorblue.noname.textinput

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.RestrictTo

/**
Create by Nguyen Van Tan 9/2020
 */

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
abstract class TextInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int,
) : LinearLayout(context, attrs, defStyleAttr), View.OnFocusChangeListener {
    private var inputFrame: FrameLayout
    private var textInputContext: Context
    var editText: EditText? = null

    init {
        orientation = VERTICAL
        this.setWillNotDraw(false)
        this.setAddStatesFromChildren(true)

        textInputContext = context
        inputFrame = FrameLayout(textInputContext)
        inputFrame.setAddStatesFromChildren(true)
        this.addView(inputFrame)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child is EditText) {
            this.editText = child
            inputFrame.addView(child, params)
            editText!!.onFocusChangeListener = this
        } else {
            super.addView(child, index, params)
        }
    }

}