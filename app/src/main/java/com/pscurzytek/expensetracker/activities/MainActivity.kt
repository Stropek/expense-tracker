package com.pscurzytek.expensetracker.activities

import android.os.Bundle
import android.content.Intent
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.widget.TextView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import com.pscurzytek.expensetracker.R

class MainActivity : AppCompatActivity() {
    private lateinit var mToolbar: Toolbar

    private lateinit var mDrawer: DrawerLayout
    private lateinit var mNavigation: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)

        mDrawer = findViewById(R.id.drawer_layout)
        mNavigation = findViewById(R.id.nav_view)

//        val categoriesButton = findViewById<TextView>(R.id.btn_categories)
//        categoriesButton.setOnClickListener {
//            val categoriesIntent = Intent(this@MainActivity, CategoryListActivity::class.java)
//            startActivity(categoriesIntent)
//        }

        setUpNavigationView()
    }

    private fun setUpNavigationView() {
        mNavigation.setNavigationItemSelectedListener(object: NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId) {
                    R.id.nav_expenses -> Toast.makeText(this@MainActivity, "Expenses", Toast.LENGTH_LONG).show()
                    R.id.nav_categories -> Toast.makeText(this@MainActivity, "Categories", Toast.LENGTH_LONG).show()
                    else -> throw UnsupportedOperationException("Unknown item selected: ${item.itemId}")
                }

                loadHomeFragment()
                return true
            }
        })

        val actionBarDrawerToggle = ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.open_drawer, R.string.close_drawer)
        mDrawer.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    private fun loadHomeFragment() {
        
    }
}
