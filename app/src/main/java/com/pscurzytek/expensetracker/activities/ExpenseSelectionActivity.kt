package com.pscurzytek.expensetracker.activities

import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.pscurzytek.expensetracker.CategoryTypes
import com.pscurzytek.expensetracker.Constants
import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.adapters.ExpenseSelectionAdapter
import com.pscurzytek.expensetracker.data.ExpenseContract
import com.pscurzytek.expensetracker.data.loaders.ExpenseLoader
import com.pscurzytek.expensetracker.utils.LayoutUtils

class ExpenseSelectionActivity : AppCompatActivity(),
        LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var mType: CategoryTypes
    private lateinit var mCategory: String
    private lateinit var mExpenseSelectionAdapter: ExpenseSelectionAdapter
    private lateinit var mExpensesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_selection)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val type = sharedPreferences.getString(Constants.CategoryProperties.Type, "")
        mType = CategoryTypes.valueOf(type)
        mCategory = sharedPreferences.getString(Constants.CategoryProperties.Name, "")

        mExpenseSelectionAdapter = ExpenseSelectionAdapter(this)
        mExpensesRecyclerView = findViewById(R.id.rv_expenses)
        mExpensesRecyclerView.adapter = mExpenseSelectionAdapter
        mExpensesRecyclerView.layoutManager  = GridLayoutManager(this, LayoutUtils.calculateNoOfColumns(this))

        supportLoaderManager.initLoader(EXPENSE_LOADER_ID, null, this)
    }

    // TODO: open expense details from expenseSelectionActivity - either on click on 'NEXT' or on selection of any of expense tiles
//        val intent = Intent(this, ExpenseDetailsActivity::class.java)
//
//        intent.putExtra(Constants.CategoryProperties.Type, mType.name)
//        intent.putExtra(Constants.CategoryProperties.Name, mCategory)
//
//        startActivity(intent)

    override fun onResume() {
        super.onResume()
        supportLoaderManager.restartLoader(EXPENSE_LOADER_ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val selection = "${ExpenseContract.ExpenseEntry.COLUMN_CATEGORY}=?"
        val selectionArgs = arrayOf(mCategory)

        return ExpenseLoader(this, selection, selectionArgs)
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        mExpenseSelectionAdapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        mExpenseSelectionAdapter.swapCursor(null)
    }

    companion object {
        private val EXPENSE_LOADER_ID = 3
    }
}
