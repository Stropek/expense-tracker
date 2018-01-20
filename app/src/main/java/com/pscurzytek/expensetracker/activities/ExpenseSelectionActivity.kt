package com.pscurzytek.expensetracker.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.pscurzytek.expensetracker.R

class ExpenseSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_selection)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
