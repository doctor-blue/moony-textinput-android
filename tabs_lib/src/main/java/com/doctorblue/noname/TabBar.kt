package com.doctorblue.noname

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * Create by Nguyen Van Tan 9/2020
 */
class TabBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.TabLayoutStyle,
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = HORIZONTAL
    }
}