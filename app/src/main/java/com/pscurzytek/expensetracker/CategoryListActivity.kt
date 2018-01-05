package com.pscurzytek.expensetracker

import android.content.Intent
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.pscurzytek.expensetracker.data.ExpenseContract
import com.pscurzytek.expensetracker.data.extensions.getStringByColumn
import com.pscurzytek.expensetracker.data.loaders.CategoryLoader
import com.pscurzytek.expensetracker.helpers.RecyclerItemTouchHelperListener
import com.pscurzytek.expensetracker.helpers.RecyclerItemWithBackgroundTouchHelper

class CategoryListActivity: AppCompatActivity(),
        LoaderManager.LoaderCallbacks<Cursor>,
        RecyclerItemTouchHelperListener
{
    private var mCategoriesAdapter: CategoryAdapter? = null
    private lateinit var mCategoriesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val addCategoryButton = findViewById<FloatingActionButton>(R.id.btn_add_category)
        addCategoryButton.setOnClickListener {
            val categoryIntent = Intent(this@CategoryListActivity, CategoryDetailsActivity::class.java)
            startActivity(categoryIntent)
        }

        mCategoriesRecyclerView = findViewById(R.id.rv_categories)
        mCategoriesRecyclerView.layoutManager = LinearLayoutManager(this)
        mCategoriesAdapter = CategoryAdapter(this)
        mCategoriesRecyclerView.adapter = mCategoriesAdapter

        val touchHelper = RecyclerItemWithBackgroundTouchHelper(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, this@CategoryListActivity)
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

//                        val item = contentResolver.query(categoryUri, null, null, null, null)
//                        val deletedItem = item.getCategory()
//                        val deletedPosition = viewHolder.adapterPosition

                contentResolver.delete(categoryUri, null, null)
                supportLoaderManager.restartLoader(CATEGORY_LOADER_ID, null, this@CategoryListActivity)

//                        item.close()
            }
            ItemTouchHelper.RIGHT -> {
                openCategoryDetails(id)
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
