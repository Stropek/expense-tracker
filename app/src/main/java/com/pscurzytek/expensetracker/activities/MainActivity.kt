package com.pscurzytek.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.PopupMenu
import android.widget.Toast
import com.pscurzytek.expensetracker.CategoryTypes
import com.pscurzytek.expensetracker.Constants
import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.data.ExpenseContract
import com.pscurzytek.expensetracker.data.extensions.getExpense
import com.pscurzytek.expensetracker.data.extensions.getIntByColumn
import com.pscurzytek.expensetracker.data.extensions.getStringByColumn
import com.pscurzytek.expensetracker.fragments.CategoryListFragment
import com.pscurzytek.expensetracker.fragments.ExpenseListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mHandler: Handler
    private lateinit var mToolbar: Toolbar
    private lateinit var mDrawer: DrawerLayout
    private lateinit var mNavigation: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mHandler = Handler()

        mToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)

        mDrawer = findViewById(R.id.drawer_layout)
        mNavigation = findViewById(R.id.nav_view)

        setUpNavigationView()
        loadHomeFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actions, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    fun onCategoryClicked(view: View) {
        val id = (view.parent.parent as FrameLayout).tag.toString()
        openCategoryDetails(id)
    }

    fun onExpenseMenuClicked(view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.actions_expense)

        val fieldMenuHelper = PopupMenu::class.java.getDeclaredField("mPopup")
        fieldMenuHelper.isAccessible = true
        val menuHelper = fieldMenuHelper.get(popup)
        menuHelper.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java).invoke(menuHelper, true)

        popup.setOnMenuItemClickListener {
            val id = (view.parent.parent.parent as FrameLayout).tag.toString()

            when(it.itemId) {
                R.id.action_quick_copy -> {
                    quickCopyExpense(id)
                }
                R.id.action_copy-> {
                    copyExpense()
                }
            }
            true
        }
        popup.show()
    }

    fun onExpenseClicked(view: View) {
        val id = (view.parent.parent as FrameLayout).tag.toString()
        openExpenseDetails(id)
    }

    private fun setUpNavigationView() {
        mNavigation.setNavigationItemSelectedListener { item ->
            CURRENT_TAG = when(item.itemId) {
                R.id.nav_expenses -> TAG_EXPENSES
                R.id.nav_categories -> TAG_CATEGORIES
                else -> throw UnsupportedOperationException("Unknown item selected: ${item.itemId}")
            }

            loadHomeFragment()
            true
        }

        val actionBarDrawerToggle = ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.open_drawer, R.string.close_drawer)
        mDrawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    private fun loadHomeFragment(force: Boolean = false) {
        if (supportFragmentManager.findFragmentByTag(CURRENT_TAG) == null || force) {
            val mPendingRunnable = Runnable {
                // update the main content by replacing fragments
                val fragment = getHomeFragment()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG)
                fragmentTransaction.commitAllowingStateLoss()
            }

            mHandler.post(mPendingRunnable)
        }

        mDrawer.closeDrawers()
        invalidateOptionsMenu()
    }

    private fun getHomeFragment(): Fragment {
        return when(CURRENT_TAG) {
            TAG_EXPENSES -> ExpenseListFragment()
            TAG_CATEGORIES -> CategoryListFragment()
            else -> throw UnsupportedOperationException("Unsupported fragment: $CURRENT_TAG")
        }
    }

    private fun openCategoryDetails(id: String) {
        val details = contentResolver.query(ExpenseContract.ExpenseCategory.CONTENT_URI.buildUpon().appendPath(id).build(),
                null,
                null,
                null,
                null)

        val intent = Intent(this, CategoryDetailsActivity::class.java)

        intent.putExtra(Constants.ID, id)
        intent.putExtra(Constants.CategoryProperties.Name, details.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_NAME))
        intent.putExtra(Constants.CategoryProperties.Description, details.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_DESCRIPTION))
        intent.putExtra(Constants.CategoryProperties.Type, details.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_TYPE))

        details.close()

        startActivity(intent)
    }

    private fun openExpenseDetails(id: String) {
        val details = contentResolver.query(ExpenseContract.ExpenseEntry.CONTENT_URI.buildUpon().appendPath(id).build(),
                null,
                null,
                null,
                null)

        val intent = Intent(this, ExpenseDetailsActivity::class.java)

        intent.putExtra(Constants.ID, id)
        intent.putExtra(Constants.ExpenseProperties.Name, details.getStringByColumn(ExpenseContract.ExpenseEntry.COLUMN_NAME))
        intent.putExtra(Constants.ExpenseProperties.Category, details.getStringByColumn(ExpenseContract.ExpenseEntry.COLUMN_CATEGORY))
        intent.putExtra(Constants.ExpenseProperties.Type, CategoryTypes.valueOf(details.getStringByColumn(ExpenseContract.ExpenseEntry.COLUMN_TYPE)))
        intent.putExtra(Constants.ExpenseProperties.Amount, details.getIntByColumn(ExpenseContract.ExpenseEntry.COLUMN_AMOUNT) / 100.0)
        intent.putExtra(Constants.ExpenseProperties.Date, details.getStringByColumn(ExpenseContract.ExpenseEntry.COLUMN_DATE))

        details.close()

        startActivity(intent)
    }

    private fun copyExpense() {
        Toast.makeText(this@MainActivity, "Copy...", Toast.LENGTH_LONG).show()
    }

    private fun quickCopyExpense(id: String) {
        // TODO: consider using DI and putting all contentResolver interactions in a repo or service
        try {
            val expenseUri = ExpenseContract.ExpenseEntry.CONTENT_URI
                    .buildUpon().appendPath(id).build()

            val item = contentResolver.query(expenseUri, null, null, null, null)
            item.moveToFirst()
            val toCopy = item.getExpense()
            val values = toCopy.getContentValues()

            values.remove(ExpenseContract.ExpenseEntry.ID)

            contentResolver.insert(ExpenseContract.ExpenseEntry.CONTENT_URI, values)

            item.close()
            loadHomeFragment(true)
        }
        catch (ex: Exception) {
            Log.e(TAG, "Failed to quick-copy expenses")
            ex.printStackTrace()
        }
    }

    companion object {
        private val TAG_EXPENSES = "expenses"
        private val TAG_CATEGORIES = "categories"

        var CURRENT_TAG = TAG_EXPENSES

        val TAG = MainActivity::class.java.name
    }
}
