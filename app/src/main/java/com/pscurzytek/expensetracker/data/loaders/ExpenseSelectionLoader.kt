package com.pscurzytek.expensetracker.data.loaders

import android.content.Context
import android.database.Cursor
import android.support.v4.content.AsyncTaskLoader
import android.util.Log
import com.pscurzytek.expensetracker.data.ExpenseContract

/**
 * Created by p.s.curzytek on 1/15/2018.
 */
class ExpenseSelectionLoader(context: Context, private val category: String):
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
            val uri = ExpenseContract.ExpenseEntry.CONTENT_URI.buildUpon()
                    .appendPath("names").appendPath(category).build()

            context.contentResolver.query(uri,
                    null,
                    null,
                    null,
                    null)
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
        val TAG = ExpenseSelectionLoader::class.java.simpleName
    }
}