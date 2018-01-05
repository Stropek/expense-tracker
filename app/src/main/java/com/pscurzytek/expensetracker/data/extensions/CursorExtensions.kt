package com.pscurzytek.expensetracker.data.extensions

import android.database.Cursor
import com.pscurzytek.expensetracker.Constants
import com.pscurzytek.expensetracker.data.models.Category

/**
 * Created by p.s.curzytek on 12/29/2017.
 */
fun Cursor.getStringByColumn(columnName: String): String {
    return getString(getColumnIndex(columnName)) ?: ""
}

fun Cursor.getCategory(): Category {
    val name = this.getStringByColumn(Constants.CategoryProperties.Name)
    val desc = this.getStringByColumn(Constants.CategoryProperties.Description)
    val type = this.getStringByColumn(Constants.CategoryProperties.Type)

    return Category(name, desc, type)
}

