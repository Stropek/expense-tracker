package com.pscurzytek.expensetracker.activities

import android.content.Context
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
import android.widget.FrameLayout
import android.widget.TextView
import com.pscurzytek.expensetracker.CategoryTypes
import com.pscurzytek.expensetracker.Constants
import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.adapters.CategorySelectionAdapter
import com.pscurzytek.expensetracker.data.ExpenseContract
import com.pscurzytek.expensetracker.data.loaders.CategoryLoader


class CategorySelectionActivity : AppCompatActivity(),
        LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var mType: CategoryTypes
    private lateinit var mCategorySelectionAdapter: CategorySelectionAdapter
    private lateinit var mCategoriesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_selection)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val type = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(Constants.CategoryProperties.Type, "")
        mType = CategoryTypes.valueOf(type)

        mCategorySelectionAdapter = CategorySelectionAdapter(this)
        mCategoriesRecyclerView = findViewById(R.id.rv_categories)
        mCategoriesRecyclerView.adapter = mCategorySelectionAdapter
        mCategoriesRecyclerView.layoutManager = GridLayoutManager(this, calculateNoOfColumns(this))

        supportLoaderManager.initLoader(CATEGORY_LOADER_ID, null, this)
    }

    override fun onResume() {
        super.onResume()
        supportLoaderManager.restartLoader(CATEGORY_LOADER_ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val selection = "${ExpenseContract.ExpenseCategory.COLUMN_TYPE}=?"
        val selectionArgs = arrayOf(mType.name)

        return CategoryLoader(this, selection, selectionArgs)
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        mCategorySelectionAdapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        mCategorySelectionAdapter.swapCursor(null)
    }

    fun onCategoryClicked(view: View) {
        val category = view.findViewById<TextView>(R.id.tv_category_name).text
        newExpenseEntry(mType, category)
    }

    // TODO: move to utility class; take grid width as parameter
    fun calculateNoOfColumns(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        return (dpWidth / 170).toInt()
    }

    private fun newExpenseEntry(type: CategoryTypes, category: CharSequence) {
        // TODO: open expense details from expenseSelectionActivity
//        val intent = Intent(this, ExpenseDetailsActivity::class.java)
//
//        intent.putExtra(Constants.CategoryProperties.Type, type)
//        intent.putExtra(Constants.CategoryProperties.Name, category)
//
//        startActivity(intent)
        val intent = Intent(this, ExpenseSelectionActivity::class.java)

        intent.putExtra(Constants.CategoryProperties.Type, type)
        intent.putExtra(Constants.CategoryProperties.Name, category)

        startActivity(intent)
    }

    companion object {
        private val CATEGORY_LOADER_ID = 2
    }
}
