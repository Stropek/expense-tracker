package com.pscurzytek.expensetracker.data

import android.provider.BaseColumns

/**
 * Created by p.s.curzytek on 11/24/2017.
 */
class ExpenseContract {

    class ExpenseEntry: BaseColumns {
        companion object: KBaseColumns() {
            val TABLE_NAME = "expenses"

            val COLUMN_CATEGORY_ID = "category_id"
            val COLUMN_AMOUNT = "amount"
            val COLUMN_CREATED = "created"
        }
    }

    class ExpenseCategory: BaseColumns {
        companion object {
            val TABLE_NAME = "categories"

            val COLUMN_NAME = "name"
            val COLUMN_DESCRIPTION = "description"
            val COLUMN_TYPE = "type"
            val COLUMN_CREATED = "created"
        }
    }
}