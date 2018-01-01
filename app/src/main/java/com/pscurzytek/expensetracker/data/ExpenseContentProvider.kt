package com.pscurzytek.expensetracker.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.net.Uri

/**
 * Created by p.s.curzytek on 12/10/2017.
 */
class ExpenseContentProvider: ContentProvider() {
    private var mExpenseTrackerDbHelper: ExpenseTrackerDbHelper? = null

    companion object {
        val EXPENSE_ENTRIES = 100
        val EXPENSE_ENTRY_WITH_ID = 101

        val EXPENSE_CATEGORIES = 200
        val EXPENSE_CATEGORY_WITH_ID = 201

        private val sUriMatcher = buildUriMatcher()

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
        mExpenseTrackerDbHelper = ExpenseTrackerDbHelper(context)
        return true
    }

    override fun insert(uri: Uri, values: ContentValues): Uri {
        val returnUri: Uri?
        val db = mExpenseTrackerDbHelper!!.writableDatabase

        when(sUriMatcher.match(uri)) {
            EXPENSE_CATEGORIES -> {
                val categoryId = db.insert(ExpenseContract.ExpenseCategory.TABLE_NAME, null, values)
                if (categoryId > -1) {
                    returnUri = uri.buildUpon().appendPath(categoryId.toString()).build()
                } else {
                    throw SQLException("Failed to insert a new category into: $uri")
                }
            }
            else -> throw UnsupportedOperationException("Unknown operation URI: $uri")
        }

        context.contentResolver.notifyChange(uri, null)
        return returnUri
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        val db = mExpenseTrackerDbHelper!!.readableDatabase
        val cursor: Cursor?

        when (sUriMatcher.match(uri)) {
            EXPENSE_CATEGORIES -> {
                cursor = db.query(ExpenseContract.ExpenseCategory.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder)
            }
            EXPENSE_CATEGORY_WITH_ID -> {
                val id = uri.lastPathSegment

                cursor = db.query(ExpenseContract.ExpenseCategory.TABLE_NAME,
                        projection,
                        "${ExpenseContract.ExpenseCategory.ID}=?",
                        arrayOf(id),
                        null,
                        null,
                        null)
                cursor?.moveToFirst()
            }
            else -> throw UnsupportedOperationException("Unknown operation URI: $uri")
        }

        cursor!!.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = mExpenseTrackerDbHelper!!.writableDatabase

        when(sUriMatcher.match(uri)) {
            EXPENSE_CATEGORY_WITH_ID -> {
                val id = uri.lastPathSegment
                return db.delete(ExpenseContract.ExpenseCategory.TABLE_NAME,
                        "${ExpenseContract.ExpenseCategory.ID}=?",
                        arrayOf(id))
            }
            else -> throw UnsupportedOperationException("Unknown operation URI: $uri")
        }
    }

    override fun getType(uri: Uri): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}