package com.pscurzytek.expensetracker.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.pscurzytek.expensetracker.R

class CategorySelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_selection)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // TODO: display list of categories - either income or expense, depending on passed Constants.CategoryProperties.Type extra value
    }
}
