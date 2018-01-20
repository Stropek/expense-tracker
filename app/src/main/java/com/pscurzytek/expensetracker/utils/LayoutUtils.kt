package com.pscurzytek.expensetracker.utils

import android.content.Context

/**
 * Created by p.s.curzytek on 1/20/2018.
 */
object LayoutUtils {

    fun calculateNoOfColumns(context: Context, cellWidth: Int = 175): Int {
        val displayMetrics = context.resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        return (dpWidth / cellWidth).toInt()
    }
}