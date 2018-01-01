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
import android.widget.LinearLayout
import com.pscurzytek.expensetracker.data.ExpenseContract
import com.pscurzytek.expensetracker.data.extensions.getStringByColumn
import com.pscurzytek.expensetracker.data.loaders.CategoryLoader

class CategoryListActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {
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

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                when(direction) {
                    ItemTouchHelper.LEFT -> {

                    }
                    ItemTouchHelper.RIGHT -> {
                        val id = viewHolder!!.itemView.tag.toString()
                        openCategoryDetails(id)
                    }
                }
            }
        }).attachToRecyclerView(mCategoriesRecyclerView)

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

    fun onCategoryClicked(view: View) {
        val id = (view.parent as LinearLayout).tag.toString()
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
        intent.putExtra("category_name", details.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_NAME))
        intent.putExtra("category_desc", details.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_DESCRIPTION))

        details.close()

        startActivity(intent)
    }

    companion object {
        val CATEGORY_LOADER_ID = 1
    }
}
