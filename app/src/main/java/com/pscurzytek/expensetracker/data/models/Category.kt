package com.pscurzytek.expensetracker.data.models

import android.content.ContentValues
import com.pscurzytek.expensetracker.Constants

/**
 * Created by p.s.curzytek on 1/4/2018.
 */
data class Category(val name: String, val description: String, val type: String, val created: String?) {
    fun getContentValues(): ContentValues {
        val values = ContentValues()

        values.put(Constants.ExpenseCategoryTable.Columns.Name, name)
        values.put(Constants.ExpenseCategoryTable.Columns.Description, description)
        values.put(Constants.ExpenseCategoryTable.Columns.Type, type)
        values.put(Constants.ExpenseCategoryTable.Columns.Created, created)

        return values
    }
}
