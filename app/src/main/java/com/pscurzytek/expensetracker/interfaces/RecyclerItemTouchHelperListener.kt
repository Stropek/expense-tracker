package com.pscurzytek.expensetracker.interfaces

import android.support.v7.widget.RecyclerView

/**
 * Created by p.s.curzytek on 1/4/2018.
 */
interface RecyclerItemTouchHelperListener {
    fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
}