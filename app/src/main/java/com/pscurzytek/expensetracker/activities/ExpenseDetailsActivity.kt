package com.pscurzytek.expensetracker.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.pscurzytek.expensetracker.CategoryTypes
import com.pscurzytek.expensetracker.Constants
import com.pscurzytek.expensetracker.R

class ExpenseDetailsActivity : AppCompatActivity() {

    private lateinit var mType: CategoryTypes
    private lateinit var mCategory: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mType = intent.extras.getSerializable(Constants.CategoryProperties.Type) as CategoryTypes
        mCategory = intent.extras.getString(Constants.CategoryProperties.Name)
    }
}
