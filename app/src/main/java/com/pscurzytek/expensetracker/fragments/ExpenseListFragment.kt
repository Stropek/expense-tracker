package com.pscurzytek.expensetracker.fragments

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.FloatingActionButton
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
import com.pscurzytek.expensetracker.CategoryTypes
import com.pscurzytek.expensetracker.Constants

import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.activities.CategorySelectionActivity
import com.pscurzytek.expensetracker.adapters.ExpenseListAdapter
import com.pscurzytek.expensetracker.data.loaders.ExpenseLoader
import com.pscurzytek.expensetracker.interfaces.RecyclerItemTouchHelperListener

class ExpenseListFragment : Fragment(),
        LoaderManager.LoaderCallbacks<Cursor>,
        RecyclerItemTouchHelperListener {

    private lateinit var mMainLayout: FrameLayout
    private lateinit var mEmptyImageView: ImageView
    private lateinit var mExpenseListAdapter: ExpenseListAdapter
    private lateinit var mExpenseRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mExpenseListAdapter = ExpenseListAdapter(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_expense_list, container, false)

        // TODO: FABs need to be disabled if there is no category of their type
        val addExpenseButton = view.findViewById<FloatingActionButton>(R.id.btn_add_expense)
        addExpenseButton.setOnClickListener {
            val categoryIntent = Intent(context, CategorySelectionActivity::class.java)

            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
            sharedPreferences.putString(Constants.CategoryProperties.Type, CategoryTypes.EXPENSE.name)
            sharedPreferences.apply()

            startActivity(categoryIntent)
        }
        val addIncomeButton = view.findViewById<FloatingActionButton>(R.id.btn_add_income)
        addIncomeButton.setOnClickListener {
            val categoryIntent = Intent(context, CategorySelectionActivity::class.java)

            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
            sharedPreferences.putString(Constants.CategoryProperties.Type, CategoryTypes.INCOME.name)
            sharedPreferences.apply()

            startActivity(categoryIntent)
        }

        mMainLayout = view.findViewById(R.id.lt_main)
        mEmptyImageView = view.findViewById(R.id.iv_empty)

        mExpenseRecyclerView = view.findViewById(R.id.rv_expenses)
        mExpenseRecyclerView.adapter = mExpenseListAdapter
        mExpenseRecyclerView.layoutManager = LinearLayoutManager(context)
        mExpenseRecyclerView.itemAnimator = DefaultItemAnimator()
        mExpenseRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        //TODO: touch helper
//        val touchHelper = RecyclerItemWithBackgroundTouchHelper(0, ItemTouchHelper.LEFT, this@ExpenseListFragment)
//        ItemTouchHelper(touchHelper).attachToRecyclerView(mCategoriesRecyclerView)

        loaderManager.initLoader(EXPENSE_LOADER_ID, null, this)

        return view
    }

    override fun onResume() {
        super.onResume()
        loaderManager.restartLoader(EXPENSE_LOADER_ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return ExpenseLoader(context!!)
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        mExpenseListAdapter.swapCursor(data)

        toggleView(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        mExpenseListAdapter.swapCursor(null)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int) {
        when (direction) {
            ItemTouchHelper.LEFT -> {
//                val categoryUri = ExpenseContract.ExpenseCategory.CONTENT_URI
//                        .buildUpon().appendPath(id).build()
//
//                val item = context!!.contentResolver.query(categoryUri, null, null, null, null)
//                item.moveToFirst()
//                val deletedItem = item.getCategory()
//
//                context!!.contentResolver.delete(categoryUri, null, null)
//                loaderManager.restartLoader(EXPENSE_LOADER_ID, null, this@ExpenseListFragment)
//
//                val snackbar = Snackbar.make(mMainLayout, deletedItem.name + " removed from cart!", Snackbar.LENGTH_LONG)
//                snackbar.setAction("UNDO", {
//                    context!!.contentResolver.insert(ExpenseContract.ExpenseCategory.CONTENT_URI, deletedItem.getContentValues())
//                    loaderManager.restartLoader(EXPENSE_LOADER_ID, null, this@ExpenseListFragment)
//                })
//                snackbar.setActionTextColor(Color.YELLOW)
//                snackbar.show()
//
//                item.close()
            }
        }
    }

    private fun toggleView(data: Cursor?) {
        if (data?.count == 0) {
            mEmptyImageView.visibility = View.VISIBLE
            mExpenseRecyclerView.visibility = View.GONE
        } else {
            mEmptyImageView.visibility = View.GONE
            mExpenseRecyclerView.visibility = View.VISIBLE
        }
    }

    companion object {
        private val EXPENSE_LOADER_ID = 1
    }
}
