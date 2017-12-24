package com.pscurzytek.expensetracker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton

class CategoryListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val addCategoryButton = findViewById<FloatingActionButton>(R.id.btn_add_category)
        addCategoryButton.setOnClickListener {
            val categoryIntent = Intent(this@CategoryListActivity, CategoryDetailsActivity::class.java)
            startActivity(categoryIntent)
        }
    }
}
