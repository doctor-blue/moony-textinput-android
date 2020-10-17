package com.doctorblue.noname.textinput

import android.content.Context

object TextInputFactory {

    @JvmStatic
    fun getStyle(type: Int, context: Context, textInputLayout: TextInputLayout): TextInputStyle =
        when (type) {
            1 -> LineBottomStyle(context, textInputLayout)
            3-> KohanaStyle(context, textInputLayout)
            else -> LineBottomStyle(context, textInputLayout)
        }
}