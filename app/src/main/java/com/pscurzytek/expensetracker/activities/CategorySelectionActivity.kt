package com.pscurzytek.expensetracker.activities

import android.content.Context
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
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

        mType = intent.extras.getSerializable(Constants.CategoryProperties.Type) as CategoryTypes

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

    // TODO: move to utility class; take grid width as parameter
    fun calculateNoOfColumns(context: Context): Int {
        val displayMetrics = context.resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        return (dpWidth / 170).toInt()
    }

    companion object {
        private val CATEGORY_LOADER_ID = 2
    }
}
