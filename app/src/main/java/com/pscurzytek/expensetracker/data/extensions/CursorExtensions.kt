package com.pscurzytek.expensetracker.data.extensions

import android.database.Cursor
import com.pscurzytek.expensetracker.data.ExpenseContract
import com.pscurzytek.expensetracker.data.models.Category
import com.pscurzytek.expensetracker.data.models.Expense

/**
 * Created by p.s.curzytek on 12/29/2017.
 */
fun Cursor.getIntByColumn(columnName: String): Int {
    return this.getInt(getColumnIndex(columnName))
}

fun Cursor.getStringByColumn(columnName: String): String {
    return this.getString(getColumnIndex(columnName)) ?: ""
}

fun Cursor.getCategory(): Category {
    val id = this.getIntByColumn(ExpenseContract.ExpenseCategory.ID)
    val name = this.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_NAME)
    val desc = this.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_DESCRIPTION)
    val type = this.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_TYPE)
    val created = this.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_CREATED)

    return Category(id, name, desc, type, created)
}

fun Cursor.getExpense(): Expense {
    val id = this.getIntByColumn(ExpenseContract.ExpenseEntry.ID)
    val name = this.getStringByColumn(ExpenseContract.ExpenseEntry.COLUMN_NAME)
    val type = this.getStringByColumn(ExpenseContract.ExpenseEntry.COLUMN_TYPE)
    val category = this.getStringByColumn(ExpenseContract.ExpenseEntry.COLUMN_CATEGORY)
    val amount = this.getIntByColumn(ExpenseContract.ExpenseEntry.COLUMN_AMOUNT)
    val date = this.getStringByColumn(ExpenseContract.ExpenseEntry.COLUMN_DATE)
    val created = this.getStringByColumn(ExpenseContract.ExpenseEntry.COLUMN_CREATED)

    return Expense(id, name, type, category, amount, date, created)
}
