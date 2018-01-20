package com.pscurzytek.expensetracker.data.loaders

import android.content.Context
import android.database.Cursor
import android.support.v4.content.AsyncTaskLoader
import android.util.Log
import com.pscurzytek.expensetracker.data.ExpenseContract

/**
 * Created by p.s.curzytek on 1/15/2018.
 */
class ExpenseLoader(context: Context, private var selection: String? = null, private var selectionArgs: Array<String>? = null):
        AsyncTaskLoader<Cursor>(context) {

    private var mExpenses: Cursor? = null

    override fun onStartLoading() {
        if (mExpenses!= null)
            deliverResult(mExpenses)
        else
            forceLoad()
    }

    override fun loadInBackground(): Cursor? {
        return try {
            val sortOrder = ExpenseContract.ExpenseEntry.COLUMN_CREATED
            context.contentResolver.query(ExpenseContract.ExpenseEntry.CONTENT_URI,
                    null,
                    selection,
                    selectionArgs,
                    sortOrder)
        }
        catch (ex: Exception) {
            Log.e(TAG, "Failed to load expenses")
            ex.printStackTrace()
            null
        }
    }

    override fun deliverResult(data: Cursor?) {
        mExpenses = data
        super.deliverResult(data)
    }

    companion object {
        val TAG = ExpenseLoader::class.java.simpleName
    }
}