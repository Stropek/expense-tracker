package com.pscurzytek.expensetracker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton

class CategoryDetailsActivity : AppCompatActivity() {
    private var mType = "Expense"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun onTypeSelected(view: View) {
        if (findViewById<RadioButton>(R.id.rbtn_category_type_income).isChecked) {
            mType = getString(R.string.label_category_type_income)
        } else if (findViewById<RadioButton>(R.id.rbtn_category_type_expense).isChecked) {
            mType = getString(R.string.label_category_type_expense)
        }
    }
}
