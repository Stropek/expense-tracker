package com.pscurzytek.expensetracker.data.extensions

import android.database.Cursor
import com.pscurzytek.expensetracker.Constants
import com.pscurzytek.expensetracker.data.ExpenseContract
import com.pscurzytek.expensetracker.data.models.Category

/**
 * Created by p.s.curzytek on 12/29/2017.
 */
// TODO: write unit tests

fun Cursor.getIntByColumn(columnName: String): Int {
    return this.getInt(getColumnIndex(columnName))
}

fun Cursor.getStringByColumn(columnName: String): String {
    return this.getString(getColumnIndex(columnName)) ?: ""
}

fun Cursor.getCategory(): Category {
    val name = this.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_NAME)
    val desc = this.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_DESCRIPTION)
    val type = this.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_TYPE)
    val created = this.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_CREATED)

    return Category(name, desc, type, created)
}

