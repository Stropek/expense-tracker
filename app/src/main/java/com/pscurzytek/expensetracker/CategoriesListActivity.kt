package com.pscurzytek.expensetracker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class CategoriesListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }
}
