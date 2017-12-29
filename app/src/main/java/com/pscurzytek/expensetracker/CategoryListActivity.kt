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
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.pscurzytek.expensetracker.data.ExpenseContract
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
        val id = (view.parent as LinearLayout).tag

        val details = contentResolver.query(ExpenseContract.ExpenseCategory.CONTENT_URI.buildUpon().appendPath(id.toString()).build(),
                null,
                null,
                null,
                null)

        val intent = Intent(this@CategoryListActivity, CategoryDetailsActivity::class.java)

        intent.putExtra("category_name", id.toString())
        intent.putExtra("category_desc", details.getString(details.getColumnIndex(ExpenseContract.ExpenseCategory.COLUMN_DESCRIPTION)))

        startActivity(intent)
    }

    companion object {
        val CATEGORY_LOADER_ID = 1
    }
}
