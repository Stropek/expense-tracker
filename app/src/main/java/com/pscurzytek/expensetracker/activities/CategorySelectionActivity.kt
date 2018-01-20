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
import android.widget.TextView
import com.pscurzytek.expensetracker.CategoryTypes
import com.pscurzytek.expensetracker.Constants
import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.adapters.CategorySelectionAdapter
import com.pscurzytek.expensetracker.data.ExpenseContract
import com.pscurzytek.expensetracker.data.loaders.CategoryLoader
import com.pscurzytek.expensetracker.utils.LayoutUtils

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
        mCategoriesRecyclerView.layoutManager = GridLayoutManager(this, LayoutUtils.calculateNoOfColumns(this))

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

    fun onItemClicked(view: View) {
        val category = view.findViewById<TextView>(R.id.tv_item_name).text.toString()

        val intent = Intent(this, ExpenseSelectionActivity::class.java)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this).edit()
        sharedPreferences.putString(Constants.CategoryProperties.Name, category)
        sharedPreferences.apply()

        startActivity(intent)
    }

    companion object {
        private val CATEGORY_LOADER_ID = 2
    }
}
