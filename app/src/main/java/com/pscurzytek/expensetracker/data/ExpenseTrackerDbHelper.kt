package com.pscurzytek.expensetracker.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by p.s.curzytek on 12/10/2017.
 */
class ExpenseTrackerDbHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {
    companion object {
        val DATABASE_NAME = "expenseTrackerDb.db"
        val VERSION = 3
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {
        val createExpenseEntriesTable = "CREATE TABLE ${ExpenseContract.ExpenseEntry.TABLE_NAME} (" +
                "${ExpenseContract.ExpenseEntry.ID} INTEGER PRIMARY KEY" +
                ");"

        val createExpenseCategoriesTable = "CREATE TABLE ${ExpenseContract.ExpenseCategory.TABLE_NAME} (" +
                "${ExpenseContract.ExpenseCategory.ID} INTEGER PRIMARY KEY," +
                "${ExpenseContract.ExpenseCategory.COLUMN_NAME} TEXT NOT NULL" +
                ");"

        sqLiteDatabase?.execSQL(createExpenseEntriesTable)
        sqLiteDatabase?.execSQL(createExpenseCategoriesTable)
    }

    override fun onUpgrade(sqlLiteDatabase: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        sqlLiteDatabase?.execSQL("DROP TABLE IF EXISTS ${ExpenseContract.ExpenseEntry.TABLE_NAME}")
        sqlLiteDatabase?.execSQL("DROP TABLE IF EXISTS ${ExpenseContract.ExpenseCategory.TABLE_NAME}")

        onCreate(sqlLiteDatabase)
    }
}