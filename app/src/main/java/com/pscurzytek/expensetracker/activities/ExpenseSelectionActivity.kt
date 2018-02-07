package com.pscurzytek.expensetracker.activities

import android.content.Intent
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.pscurzytek.expensetracker.CategoryTypes
import com.pscurzytek.expensetracker.Constants
import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.adapters.ExpenseSelectionAdapter
import com.pscurzytek.expensetracker.data.ExpenseContract
import com.pscurzytek.expensetracker.data.loaders.ExpenseLoader
import com.pscurzytek.expensetracker.data.loaders.ExpenseSelectionLoader
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

    override fun onResume() {
        super.onResume()
        supportLoaderManager.restartLoader(EXPENSE_LOADER_ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return ExpenseSelectionLoader(this, mCategory)
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        mExpenseSelectionAdapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        mExpenseSelectionAdapter.swapCursor(null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ACTIVITY_EXPENSE_DETAILS_ID) {
            finish()
        }
    }

    fun onItemClicked(view: View) {
        val name = view.findViewById<TextView>(R.id.tv_item_name).text.toString()

        openExpenseDetails(name)
    }

    fun onNextClicked(view: View) {
        val name = findViewById<EditText>(R.id.et_expense_name).text.toString()

        if (name.isBlank())
            Toast.makeText(this, "Expense name is required!", Toast.LENGTH_LONG).show()
        else
            openExpenseDetails(name)
    }

    private fun openExpenseDetails(name: String) {
        val intent = Intent(this, ExpenseDetailsActivity::class.java)

        intent.putExtra(Constants.ExpenseProperties.Type, mType)
        intent.putExtra(Constants.ExpenseProperties.Category, mCategory)
        intent.putExtra(Constants.ExpenseProperties.Name, name)

        startActivityForResult(intent, ACTIVITY_EXPENSE_DETAILS_ID)
    }

    companion object {
        private val EXPENSE_LOADER_ID = 3

        private val ACTIVITY_EXPENSE_DETAILS_ID = 1
    }
}
