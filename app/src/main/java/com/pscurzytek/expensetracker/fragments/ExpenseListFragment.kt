package com.pscurzytek.expensetracker.fragments

import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
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
import com.pscurzytek.expensetracker.CategoryTypes
import com.pscurzytek.expensetracker.Constants

import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.activities.CategorySelectionActivity
import com.pscurzytek.expensetracker.adapters.ExpenseListAdapter
import com.pscurzytek.expensetracker.data.ExpenseContract
import com.pscurzytek.expensetracker.data.extensions.getExpense
import com.pscurzytek.expensetracker.data.loaders.ExpenseLoader
import com.pscurzytek.expensetracker.helpers.RecyclerItemWithBackgroundTouchHelper
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

        val addIncomeButton = view.findViewById<FloatingActionButton>(R.id.btn_add_income)
        setupAddButton(addIncomeButton, CategoryTypes.INCOME)

        val addExpenseButton = view.findViewById<FloatingActionButton>(R.id.btn_add_expense)
        setupAddButton(addExpenseButton, CategoryTypes.EXPENSE)

        mMainLayout = view.findViewById(R.id.lt_main)
        mEmptyImageView = view.findViewById(R.id.iv_empty)

        mExpenseRecyclerView = view.findViewById(R.id.rv_expenses)
        mExpenseRecyclerView.adapter = mExpenseListAdapter
        mExpenseRecyclerView.layoutManager = LinearLayoutManager(context)
        mExpenseRecyclerView.itemAnimator = DefaultItemAnimator()
        mExpenseRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        val touchHelper = RecyclerItemWithBackgroundTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(touchHelper).attachToRecyclerView(mExpenseRecyclerView)

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
        val id = viewHolder.itemView.tag.toString()

        when (direction) {
            ItemTouchHelper.LEFT -> {
                val expenseUri = ExpenseContract.ExpenseEntry.CONTENT_URI
                        .buildUpon().appendPath(id).build()

                val item = context!!.contentResolver.query(expenseUri, null, null, null, null)
                item.moveToFirst()
                val deletedItem = item.getExpense()

                context!!.contentResolver.delete(expenseUri, null, null)
                loaderManager.restartLoader(EXPENSE_LOADER_ID, null, this)

                val snackbar = Snackbar.make(mMainLayout, deletedItem.name + " removed from cart!", Snackbar.LENGTH_LONG)
                snackbar.setAction("UNDO", {
                    context!!.contentResolver.insert(ExpenseContract.ExpenseEntry.CONTENT_URI, deletedItem.getContentValues())
                    loaderManager.restartLoader(EXPENSE_LOADER_ID, null, this)
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
            mExpenseRecyclerView.visibility = View.GONE
        } else {
            mEmptyImageView.visibility = View.GONE
            mExpenseRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun setupAddButton(button: FloatingActionButton, categoryType: CategoryTypes) {
        val categories = context?.contentResolver?.query(ExpenseContract.ExpenseCategory.CONTENT_URI,
                null,
                "${ExpenseContract.ExpenseCategory.COLUMN_TYPE}=?",
                arrayOf(categoryType.name),
                null)?.count ?: 0

        if (categories > 0) {
            button.setOnClickListener {
                val categoryIntent = Intent(context, CategorySelectionActivity::class.java)

                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
                sharedPreferences.putString(Constants.CategoryProperties.Type, categoryType.name)
                sharedPreferences.apply()

                startActivity(categoryIntent)
            }
        }
        else {
            button.isEnabled = false
            button.alpha = 0.5f
        }
    }

    companion object {
        private val EXPENSE_LOADER_ID = 1
    }
}
