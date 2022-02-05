package com.devcomentry.textinputlibrary

import android.content.Context

object TextInputFactory {

    @JvmStatic
    fun getStyle(type: Int, context: Context, textInputLayout: TextInputLayout): TextInputStyle =
        when (type) {
            1 -> LineBottomStyle(context, textInputLayout)
            3 -> SwipeLeftIconStyle(context, textInputLayout)
            4 -> ActiveBottomLineStyle(context, textInputLayout)
            else -> LineBottomStyle(context, textInputLayout)
        }
}