package com.pscurzytek.expensetracker.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.pscurzytek.expensetracker.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val categoriesButton = findViewById<TextView>(R.id.btn_categories)
//        categoriesButton.setOnClickListener {
//            val categoriesIntent = Intent(this@MainActivity, CategoryListActivity::class.java)
//            startActivity(categoriesIntent)
//        }
    }
}
