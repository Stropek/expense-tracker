package com.pscurzytek.expensetracker.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

/**
 * Created by p.s.curzytek on 12/10/2017.
 */
class ExpenseContentProvider: ContentProvider() {

    companion object {
        val EXPENSE_ENTRIES = 100
        val EXPENSE_ENTRY_WITH_ID = 101

        val EXPENSE_CATEGORIES = 200
        val EXPENSE_CATEGORY_WITH_ID = 201

        val sUriMatcher = UriMatcher()

        fun buildUriMatcher(): UriMatcher {
            val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

            uriMatcher.addURI(ExpenseContract.AUTHORITY, ExpenseContract.PATH_EXPENSE_ENTRIES, EXPENSE_ENTRIES)
            uriMatcher.addURI(ExpenseContract.AUTHORITY, "${ExpenseContract.PATH_EXPENSE_ENTRIES}/#", EXPENSE_ENTRY_WITH_ID)

            uriMatcher.addURI(ExpenseContract.AUTHORITY, ExpenseContract.PATH_EXPENSE_CATEGORIES, EXPENSE_CATEGORIES)
            uriMatcher.addURI(ExpenseContract.AUTHORITY, "${ExpenseContract.PATH_EXPENSE_CATEGORIES}/#", EXPENSE_CATEGORY_WITH_ID)

            return uriMatcher
        }
    }

    override fun onCreate(): Boolean {
        return false
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selction: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(uri: Uri?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}