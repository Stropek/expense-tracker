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
        val VERSION = 1
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {

        val createExpenseEntriesTable = "CREATE TABLE ${ExpenseContract.ExpenseEntry.TABLE_NAME} (" +
                "${ExpenseContract.ExpenseEntry.ID} INTEGER PRIMARY KEY," +
                "${ExpenseContract.ExpenseEntry.COLUMN_NAME} TEXT NOT NULL," +
                "${ExpenseContract.ExpenseEntry.COLUMN_TYPE} TEXT NOT NULL," +
                "${ExpenseContract.ExpenseEntry.COLUMN_CATEGORY} TEXT NOT NULL," +
                "${ExpenseContract.ExpenseEntry.COLUMN_AMOUNT} INTEGER NOT NULL," +
                "${ExpenseContract.ExpenseEntry.COLUMN_DATE} TEXT NOT NULL," +
                "${ExpenseContract.ExpenseEntry.COLUMN_CREATED} TEXT NOT NULL" +
                ");"

        val createExpenseCategoriesTable = "CREATE TABLE ${ExpenseContract.ExpenseCategory.TABLE_NAME} (" +
                "${ExpenseContract.ExpenseCategory.ID} INTEGER PRIMARY KEY," +
                "${ExpenseContract.ExpenseCategory.COLUMN_NAME} TEXT NOT NULL," +
                "${ExpenseContract.ExpenseCategory.COLUMN_DESCRIPTION} TEXT NULL," +
                "${ExpenseContract.ExpenseCategory.COLUMN_TYPE} TEXT NOT NULL," +
                "${ExpenseContract.ExpenseCategory.COLUMN_CREATED} TEXT NOT NULL," +
                "UNIQUE (${ExpenseContract.ExpenseCategory.COLUMN_NAME} COLLATE NOCASE, ${ExpenseContract.ExpenseCategory.COLUMN_TYPE}) ON CONFLICT FAIL);"

        sqLiteDatabase?.execSQL(createExpenseEntriesTable)
        sqLiteDatabase?.execSQL(createExpenseCategoriesTable)
    }

    override fun onUpgrade(sqlLiteDatabase: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
//            sqlLiteDatabase?.execSQL(DATABASE_ALTER_TABLE_1)
        }
    }
}