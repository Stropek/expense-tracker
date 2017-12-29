package com.pscurzytek.expensetracker.data.extensions

import android.database.Cursor

/**
 * Created by p.s.curzytek on 12/29/2017.
 */
fun Cursor.getStringByColumn(columnName: String): String {
    return this.getString(getColumnIndex(columnName))
}
