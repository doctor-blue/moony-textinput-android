package com.doctorblue.noname.textinput

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.appcompat.widget.AppCompatEditText
import com.doctorblue.noname_library.R

abstract class TextInputStyle constructor(
    context: Context,
    protected val textInputLayout: TextInputLayout
) : View.OnFocusChangeListener {


    var editText: AppCompatEditText? = null

    protected var hintText = textInputLayout.hintText

    open var hasFocus: Boolean = false

    @Dimension
    open var hintTextSize: Float = textInputLayout.hintTextSize

    open var hint: String = textInputLayout.hint

    @ColorInt
    open var defaultColor: Int = textInputLayout.defaultColor

    @ColorInt
    open var activeColor: Int = textInputLayout.activeColor


    abstract fun addView()

}