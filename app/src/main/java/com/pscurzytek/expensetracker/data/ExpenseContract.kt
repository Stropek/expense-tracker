package com.pscurzytek.expensetracker.data

import android.net.Uri
import android.provider.BaseColumns
import com.pscurzytek.expensetracker.Constants

/**
 * Created by p.s.curzytek on 11/24/2017.
 */
class ExpenseContract {

    companion object {
        val AUTHORITY = "com.pscurzytek.expensetracker"

        val BASE_CONTENT_URI = Uri.parse("content://$AUTHORITY")

        val PATH_EXPENSE_ENTRIES = "expense_entries"
        val PATH_EXPENSE_CATEGORIES = "expense_categories"
    }

    class ExpenseEntry: BaseColumns {
        companion object: KBaseColumns() {
            val CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXPENSE_ENTRIES).build()

            val TABLE_NAME = "expenses"

            val COLUMN_NAME = "name"
            val COLUMN_TYPE = "type"
            val COLUMN_CATEGORY = "category"
            val COLUMN_AMOUNT = "amount"
            val COLUMN_DATE = "date"
            val COLUMN_CREATED = "created"
        }
    }

    class ExpenseCategory: BaseColumns {
        companion object: KBaseColumns() {
            val CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXPENSE_CATEGORIES).build()

            val TABLE_NAME = "categories"

            val COLUMN_NAME = "name"
            val COLUMN_DESCRIPTION = "description"
            val COLUMN_TYPE = "type"
            val COLUMN_CREATED = "created"
        }
    }
}