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

        val CREATE_TABLE_EXPENSE_ENTRIES = ""

        val CREATE_TABLE_EXPENSE_CATEGORIES = ""
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUpgrade(sqlLiteDatabase: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}