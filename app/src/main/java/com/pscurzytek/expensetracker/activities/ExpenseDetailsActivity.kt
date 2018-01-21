package com.pscurzytek.expensetracker.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.pscurzytek.expensetracker.CategoryTypes
import com.pscurzytek.expensetracker.Constants
import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.fragments.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.*

class ExpenseDetailsActivity : AppCompatActivity() {

    private lateinit var mType: CategoryTypes
    private lateinit var mCategory: String
    private lateinit var mExpenseNameTextView: TextView
    private lateinit var mDateEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mType = intent.extras.getSerializable(Constants.CategoryProperties.Type) as CategoryTypes
        mCategory = intent.extras.getString(Constants.CategoryProperties.Name)

        // TODO: data binding
        mExpenseNameTextView = findViewById(R.id.tv_name)
        mDateEditText = findViewById(R.id.et_date)

        val name = intent.getStringExtra(Constants.ExpenseProperties.Name)
        mExpenseNameTextView.text = name

        setCurrentDate()
    }

    private fun setCurrentDate() {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        mDateEditText.setText("$day / ${month + 1} / $year")
    }

    fun showDatePickerDialog(view: View) {
        val datePicker = DatePickerFragment()

        datePicker.arguments = Bundle()
        datePicker.arguments.putString(Constants.ExpenseProperties.Date, mDateEditText.text.toString())

        datePicker.show(fragmentManager, "datePicker")
    }
}
