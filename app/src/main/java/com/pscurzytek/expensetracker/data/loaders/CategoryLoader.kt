package com.pscurzytek.expensetracker.data.loaders

import android.content.Context
import android.database.Cursor
import android.support.v4.content.AsyncTaskLoader
import android.util.Log
import com.pscurzytek.expensetracker.data.ExpenseContract

/**
 * Created by p.s.curzytek on 12/26/2017.
 */
class CategoryLoader(context: Context, private var selection: String? = null, private var selectionArgs: Array<String>? = null):
        AsyncTaskLoader<Cursor>(context) {

    private var mCategories: Cursor? = null

    override fun onStartLoading() {
        if (mCategories != null)
            deliverResult(mCategories)
        else
            forceLoad()
    }

    override fun loadInBackground(): Cursor? {
        return try {
            val sortOrder = "${ExpenseContract.ExpenseCategory.COLUMN_TYPE}, ${ExpenseContract.ExpenseCategory.COLUMN_NAME}"
            context.contentResolver.query(ExpenseContract.ExpenseCategory.CONTENT_URI,
                    null,
                    selection,
                    selectionArgs,
                    sortOrder)
        }
        catch (ex: Exception) {
            Log.e(TAG, "Failed to load categories")
            ex.printStackTrace()
            null
        }
    }

    override fun deliverResult(data: Cursor?) {
        mCategories = data
        super.deliverResult(data)
    }

    companion object {
        val TAG = CategoryLoader::class.java.simpleName
    }
}