package com.pscurzytek.expensetracker.fragments

import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.pscurzytek.expensetracker.adapters.CategoryListAdapter

import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.activities.CategoryDetailsActivity
import com.pscurzytek.expensetracker.data.ExpenseContract
import com.pscurzytek.expensetracker.data.extensions.getCategory
import com.pscurzytek.expensetracker.data.loaders.CategoryLoader
import com.pscurzytek.expensetracker.helpers.RecyclerItemWithBackgroundTouchHelper
import com.pscurzytek.expensetracker.interfaces.RecyclerItemTouchHelperListener

class CategoryListFragment : Fragment(),
        LoaderManager.LoaderCallbacks<Cursor>,
        RecyclerItemTouchHelperListener {

    private lateinit var mMainLayout: FrameLayout
    private lateinit var mEmptyImageView: ImageView
    private lateinit var mCategoriesListAdapter: CategoryListAdapter
    private lateinit var mCategoriesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mCategoriesListAdapter = CategoryListAdapter(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_category_list, container, false)

        val addCategoryButton = view.findViewById<FloatingActionButton>(R.id.btn_add_category)
        addCategoryButton.setOnClickListener {
            val categoryIntent = Intent(context, CategoryDetailsActivity::class.java)
            startActivity(categoryIntent)
        }

        mMainLayout = view.findViewById(R.id.lt_main)
        mEmptyImageView = view.findViewById(R.id.iv_empty)

        mCategoriesRecyclerView = view.findViewById(R.id.rv_categories)
        mCategoriesRecyclerView.adapter = mCategoriesListAdapter
        mCategoriesRecyclerView.layoutManager = LinearLayoutManager(context)
        mCategoriesRecyclerView.itemAnimator = DefaultItemAnimator()
        mCategoriesRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        val touchHelper = RecyclerItemWithBackgroundTouchHelper(0, ItemTouchHelper.LEFT, this@CategoryListFragment)
        ItemTouchHelper(touchHelper).attachToRecyclerView(mCategoriesRecyclerView)

        loaderManager.initLoader(CATEGORY_LOADER_ID, null, this)

        return view
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(CATEGORY_LOADER_ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CategoryLoader(context!!)
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        mCategoriesListAdapter.swapCursor(data)

        toggleView(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        mCategoriesListAdapter.swapCursor(null)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        val id = viewHolder.itemView.tag.toString()

        when (direction) {
            ItemTouchHelper.LEFT -> {
                val categoryUri = ExpenseContract.ExpenseCategory.CONTENT_URI
                        .buildUpon().appendPath(id).build()

                val item = context!!.contentResolver.query(categoryUri, null, null, null, null)
                item.moveToFirst()
                val deletedItem = item.getCategory()

                context!!.contentResolver.delete(categoryUri, null, null)
                loaderManager.restartLoader(CATEGORY_LOADER_ID, null, this@CategoryListFragment)

                val snackbar = Snackbar.make(mMainLayout, deletedItem.name + " removed from cart!", Snackbar.LENGTH_LONG)
                snackbar.setAction("UNDO", {
                    context!!.contentResolver.insert(ExpenseContract.ExpenseCategory.CONTENT_URI, deletedItem.getContentValues())
                    loaderManager.restartLoader(CATEGORY_LOADER_ID, null, this@CategoryListFragment)
                })
                snackbar.setActionTextColor(Color.YELLOW)
                snackbar.show()

                item.close()
            }
        }
    }

    private fun toggleView(data: Cursor?) {
        if (data?.count == 0) {
            mEmptyImageView.visibility = View.VISIBLE
            mCategoriesRecyclerView.visibility = View.GONE
        } else {
            mEmptyImageView.visibility = View.GONE
            mCategoriesRecyclerView.visibility = View.VISIBLE
        }
    }

    companion object {
        private val CATEGORY_LOADER_ID = 1
    }
}
