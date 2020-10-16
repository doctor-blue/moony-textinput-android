package com.doctorblue.noname.tabs_lib

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout

/**
 * Create by Nguyen Van Tan 9/2020
 */
class TabBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.TabLayoutStyle,
) : LinearLayout(context, attrs, defStyleAttr) {

    var  tabItems: MutableList<TabItem> = mutableListOf()

    init {
        orientation = HORIZONTAL
    }

    fun resetTabStatus(position:Int){
        for ((i,item) in tabItems.withIndex()){
            if (i==position){
                continue
            }
            item.onReset()
            Log.d("HAHAHAHHA","Change")
        }
    }


}