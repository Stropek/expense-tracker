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
        val CREATE_TABLE_EXPENSE_ENTRIES = "CREATE TABLE ${ExpenseContract.ExpenseEntry.TABLE_NAME} (" +
                "${ExpenseContract.ExpenseEntry.ID} INTEGER PRIMARY KEY AUTOINCREMENT" +
                ");"

        val CREATE_TABLE_EXPENSE_CATEGORIES = "CREATE TABLE ${ExpenseContract.ExpenseCategory.TABLE_NAME} (" +
                "${ExpenseContract.ExpenseCategory.ID} INTEGER PRIMARY KEY AUTOINCREMENT" +
                ");"

        sqLiteDatabase?.execSQL(CREATE_TABLE_EXPENSE_ENTRIES)
        sqLiteDatabase?.execSQL(CREATE_TABLE_EXPENSE_CATEGORIES)
    }

    override fun onUpgrade(sqlLiteDatabase: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}