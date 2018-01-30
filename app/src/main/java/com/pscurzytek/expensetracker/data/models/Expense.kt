package com.pscurzytek.expensetracker.data.models

import android.content.ContentValues
import com.pscurzytek.expensetracker.data.ExpenseContract

/**
 * Created by p.s.curzytek on 1/24/2018.
 */
class Expense(val id: Int, val name: String, val type: String, val category: String, val amount: Int, val date: String?, val created: String?) {
        fun getContentValues(): ContentValues {
            val values = ContentValues()

            values.put(ExpenseContract.ExpenseEntry.ID, id)
            values.put(ExpenseContract.ExpenseEntry.COLUMN_NAME, name)
            values.put(ExpenseContract.ExpenseEntry.COLUMN_TYPE, type)
            values.put(ExpenseContract.ExpenseEntry.COLUMN_CATEGORY, category)
            values.put(ExpenseContract.ExpenseEntry.COLUMN_AMOUNT, amount)
            values.put(ExpenseContract.ExpenseEntry.COLUMN_DATE, date)
            values.put(ExpenseContract.ExpenseEntry.COLUMN_CREATED, created)

            return values
        }
    }
