package com.pscurzytek.expensetracker.activities

import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.pscurzytek.expensetracker.CategoryTypes
import com.pscurzytek.expensetracker.Constants
import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.adapters.CategoryAdapter
import com.pscurzytek.expensetracker.data.ExpenseContract
import com.pscurzytek.expensetracker.data.loaders.CategoryLoader

class CategorySelectionActivity : AppCompatActivity(),
        LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var mType: CategoryTypes
    private lateinit var mCategoriesAdapter: CategoryAdapter
    private lateinit var mCategoriesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_selection)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mType = intent.extras.getSerializable(Constants.CategoryProperties.Type) as CategoryTypes

        mCategoriesAdapter = CategoryAdapter(this)
        mCategoriesRecyclerView = findViewById(R.id.rv_categories)
        mCategoriesRecyclerView.adapter = mCategoriesAdapter
        mCategoriesRecyclerView.layoutManager = LinearLayoutManager(this)

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
        mCategoriesAdapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        mCategoriesAdapter.swapCursor(null)
    }

    companion object {
        private val CATEGORY_LOADER_ID = 2
    }
}
