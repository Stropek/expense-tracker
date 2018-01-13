package com.pscurzytek.expensetracker.activities

import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.FrameLayout
import com.pscurzytek.expensetracker.CategoryAdapter
import com.pscurzytek.expensetracker.Constants
import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.data.ExpenseContract
import com.pscurzytek.expensetracker.data.extensions.getCategory
import com.pscurzytek.expensetracker.data.extensions.getStringByColumn
import com.pscurzytek.expensetracker.data.loaders.CategoryLoader
import com.pscurzytek.expensetracker.interfaces.RecyclerItemTouchHelperListener
import com.pscurzytek.expensetracker.helpers.RecyclerItemWithBackgroundTouchHelper

class CategoryListActivity: AppCompatActivity(),
        LoaderManager.LoaderCallbacks<Cursor>,
        RecyclerItemTouchHelperListener
{
    private var mCategoriesAdapter: CategoryAdapter? = null
    private lateinit var mCategoriesRecyclerView: RecyclerView
    private lateinit var mMainLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val addCategoryButton = findViewById<FloatingActionButton>(R.id.btn_add_category)
        addCategoryButton.setOnClickListener {
            val categoryIntent = Intent(this@CategoryListActivity, CategoryDetailsActivity::class.java)
            startActivity(categoryIntent)
        }

        mMainLayout = findViewById(R.id.lt_main)

        mCategoriesAdapter = CategoryAdapter(this)

        mCategoriesRecyclerView = findViewById(R.id.rv_categories)
        mCategoriesRecyclerView.adapter = mCategoriesAdapter
        mCategoriesRecyclerView.layoutManager = LinearLayoutManager(this)
        mCategoriesRecyclerView.itemAnimator = DefaultItemAnimator()
        mCategoriesRecyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val touchHelper = RecyclerItemWithBackgroundTouchHelper(0, ItemTouchHelper.LEFT, this@CategoryListActivity)
        ItemTouchHelper(touchHelper).attachToRecyclerView(mCategoriesRecyclerView)

        supportLoaderManager.initLoader(CATEGORY_LOADER_ID, null, this)
    }

    override fun onResume() {
        super.onResume()
        supportLoaderManager.restartLoader(CATEGORY_LOADER_ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CategoryLoader(this)
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        mCategoriesAdapter!!.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        mCategoriesAdapter!!.swapCursor(null)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        val id = viewHolder.itemView.tag.toString()

        when (direction) {
            ItemTouchHelper.LEFT -> {
                val categoryUri = ExpenseContract.ExpenseCategory.CONTENT_URI
                        .buildUpon().appendPath(id).build()

                val item = contentResolver.query(categoryUri, null, null, null, null)
                item.moveToFirst()
                val deletedItem = item.getCategory()

                contentResolver.delete(categoryUri, null, null)
                supportLoaderManager.restartLoader(CATEGORY_LOADER_ID, null, this@CategoryListActivity)

                val snackbar = Snackbar.make(mMainLayout, deletedItem.name + " removed from cart!", Snackbar.LENGTH_LONG)
                snackbar.setAction("UNDO", {
                    contentResolver.insert(ExpenseContract.ExpenseCategory.CONTENT_URI, deletedItem.getContentValues())
                    supportLoaderManager.restartLoader(CATEGORY_LOADER_ID, null, this@CategoryListActivity)
                })
                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()

                item.close()
            }
        }
    }

    fun onCategoryClicked(view: View) {
        val id = (view.parent.parent as FrameLayout).tag.toString()
        openCategoryDetails(id)
    }

    private fun openCategoryDetails(id: String) {
        val details = contentResolver.query(ExpenseContract.ExpenseCategory.CONTENT_URI.buildUpon().appendPath(id).build(),
                null,
                null,
                null,
                null)

        val intent = Intent(this@CategoryListActivity, CategoryDetailsActivity::class.java)

        intent.putExtra("id", id)
        intent.putExtra(Constants.CategoryProperties.Name, details.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_NAME))
        intent.putExtra(Constants.CategoryProperties.Description, details.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_DESCRIPTION))
        intent.putExtra(Constants.CategoryProperties.Type, details.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_TYPE))

        details.close()

        startActivity(intent)
    }

    companion object {
        val CATEGORY_LOADER_ID = 1
    }
}
