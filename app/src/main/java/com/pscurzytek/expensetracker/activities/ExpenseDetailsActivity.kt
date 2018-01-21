package com.pscurzytek.expensetracker.activities

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.pscurzytek.expensetracker.CategoryTypes
import com.pscurzytek.expensetracker.Constants
import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.databinding.ActivityExpenseDetailsBinding
import com.pscurzytek.expensetracker.fragments.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.*

class ExpenseDetailsActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityExpenseDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_expense_details)

        mBinding.tvName.text = intent.getStringExtra(Constants.ExpenseProperties.Name)
        mBinding.tvCategory.text = intent.extras.getString(Constants.CategoryProperties.Name)
        mBinding.tvType.text = (intent.extras.getSerializable(Constants.CategoryProperties.Type) as CategoryTypes).name

        setCurrentDate()
    }

    fun showDatePickerDialog(view: View) {
        val datePicker = DatePickerFragment()

        datePicker.arguments = Bundle()
        datePicker.arguments.putString(Constants.ExpenseProperties.Date, mBinding.etDate.text.toString())

        datePicker.show(fragmentManager, "datePicker")
    }

    fun onExpenseSave(view: View) {
        //TODO: save
    }

    private fun setCurrentDate() {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        mBinding.etDate.setText("$day / ${month + 1} / $year")
    }
}
