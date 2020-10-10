package com.doctorblue.noname

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.HorizontalScrollView
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.util.Pools
import androidx.core.util.Pools.SynchronizedPool
import androidx.viewpager.widget.ViewPager

/**
 * Create by Nguyen Van Tan 9/2020
 */
class TabLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.TabLayoutStyle,
) : HorizontalScrollView(context, attrs, defStyleAttr) {

    @ColorInt
    var color: Int = Color.BLACK
        set(value) {
            field = value
            //TODO
        }

    @ColorInt
    var colorActive: Int = Color.BLACK
        set(value) {
            field = value
            //TODO
        }

    @Dimension(unit = Dimension.SP)
    var textSize: Float = (16f * resources.displayMetrics.density)
        set(@Dimension value) {
            field = value
            //TODO
        }

    @Dimension(unit = Dimension.DP)
    var iconSize: Float = (24f * resources.displayMetrics.density)
        set(@Dimension value) {
            field = value
            //TODO
        }

    private val defaultWidthItem = (64f * resources.displayMetrics.density)

    private var tabBar = TabBar(context)

    var indicatorDuration: Long = 200L

    private var indicatorPosition = null

    private val tabPool: Pools.Pool<TabItem> = SynchronizedPool(16)

    var viewPager: ViewPager? = null

    private var tabs: MutableList<Tab> = mutableListOf()

    init {
        //addView(tabBar)
    }


}