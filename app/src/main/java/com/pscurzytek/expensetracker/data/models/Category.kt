package com.pscurzytek.expensetracker.data.models

import android.content.ContentValues
import com.pscurzytek.expensetracker.Constants
import com.pscurzytek.expensetracker.data.ExpenseContract

/**
 * Created by p.s.curzytek on 1/4/2018.
 */
data class Category(val id: Int, val name: String, val description: String, val type: String, val created: String?) {
    fun getContentValues(): ContentValues {
        val values = ContentValues()

        values.put(ExpenseContract.ExpenseCategory.ID, id)
        values.put(ExpenseContract.ExpenseCategory.COLUMN_NAME, name)
        values.put(ExpenseContract.ExpenseCategory.COLUMN_DESCRIPTION, description)
        values.put(ExpenseContract.ExpenseCategory.COLUMN_TYPE, type)
        values.put(ExpenseContract.ExpenseCategory.COLUMN_CREATED, created)

        return values
    }
}
