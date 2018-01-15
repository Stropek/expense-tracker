package com.pscurzytek.expensetracker.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.pscurzytek.expensetracker.Constants
import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.data.ExpenseContract
import com.pscurzytek.expensetracker.data.extensions.getStringByColumn
import com.pscurzytek.expensetracker.fragments.CategoryListFragment

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
    }

    fun onCategoryClicked(view: View) {
        val id = (view.parent.parent as FrameLayout).tag.toString()
        openCategoryDetails(id)
    }

    private fun setUpNavigationView() {
        mNavigation.setNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_expenses -> {
                    CURRENT_TAG = TAG_EXPENSES
                    Toast.makeText(this@MainActivity, "Expenses", Toast.LENGTH_LONG).show()
                }
                R.id.nav_categories -> {
                    CURRENT_TAG = TAG_CATEGORIES
                }
                else -> throw UnsupportedOperationException("Unknown item selected: ${item.itemId}")
            }

            loadHomeFragment()
            true
        }

        val actionBarDrawerToggle = ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.open_drawer, R.string.close_drawer)
        mDrawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    private fun loadHomeFragment() {
        if (supportFragmentManager.findFragmentByTag(CURRENT_TAG) == null) {
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
            TAG_EXPENSES -> {
                Toast.makeText(this@MainActivity, "getHomeFragment: $TAG_EXPENSES", Toast.LENGTH_LONG).show()
                Fragment()
            }
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

        intent.putExtra("id", id)
        intent.putExtra(Constants.CategoryProperties.Name, details.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_NAME))
        intent.putExtra(Constants.CategoryProperties.Description, details.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_DESCRIPTION))
        intent.putExtra(Constants.CategoryProperties.Type, details.getStringByColumn(ExpenseContract.ExpenseCategory.COLUMN_TYPE))

        details.close()

        startActivity(intent)
    }

    companion object {
        private val TAG_EXPENSES = "expenses"
        private val TAG_CATEGORIES = "categories"

        var CURRENT_TAG = TAG_EXPENSES
    }
}
