package com.pscurzytek.expensetracker.activities

import android.content.ContentValues
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.pscurzytek.expensetracker.CategoryTypes
import com.pscurzytek.expensetracker.Constants
import com.pscurzytek.expensetracker.R
import com.pscurzytek.expensetracker.data.ExpenseContract
import com.pscurzytek.expensetracker.databinding.ActivityExpenseDetailsBinding
import com.pscurzytek.expensetracker.fragments.DatePickerFragment
import com.pscurzytek.expensetracker.helpers.DecimalTextWatcher
import java.util.*

class ExpenseDetailsActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityExpenseDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_expense_details)

        mBinding.tvName.text = intent.getStringExtra(Constants.ExpenseProperties.Name)
        mBinding.tvCategory.text = intent.extras.getString(Constants.ExpenseProperties.Category)
        mBinding.tvType.text = (intent.extras.getSerializable(Constants.ExpenseProperties.Type) as CategoryTypes).name

        mBinding.etAmount.addTextChangedListener(DecimalTextWatcher(mBinding.etAmount))

        setCurrentDate()
    }

    fun showDatePickerDialog(view: View) {
        val datePicker = DatePickerFragment()

        datePicker.arguments = Bundle()
        datePicker.arguments.putString(Constants.ExpenseProperties.Date, mBinding.etDate.text.toString())

        datePicker.show(fragmentManager, "datePicker")
    }

    fun onExpenseSave(view: View) {
        val amount = mBinding.etAmount.text.toString()
        if (amount.isEmpty()) {
            Toast.makeText(this, "Amount is required", Toast.LENGTH_SHORT).show()
            return
        }
        if (amount.toDouble() == 0.0) {
            Toast.makeText(this, "Amount must be positive", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val values = ContentValues()
            values.put(ExpenseContract.ExpenseEntry.COLUMN_NAME, mBinding.tvName.text.toString())
            values.put(ExpenseContract.ExpenseEntry.COLUMN_CATEGORY, mBinding.tvCategory.text.toString())
            values.put(ExpenseContract.ExpenseEntry.COLUMN_TYPE, mBinding.tvType.text.toString())
            values.put(ExpenseContract.ExpenseEntry.COLUMN_CREATED, mBinding.etDate.text.toString())
            values.put(ExpenseContract.ExpenseEntry.COLUMN_AMOUNT, mBinding.etAmount.text.toString())

            contentResolver.insert(ExpenseContract.ExpenseEntry.CONTENT_URI, values)

            // finish activity and return all the way back to main activity
            finish()
        } catch (ex: Exception) {
            Log.e(TAG, "Failed to insert expenses")
            ex.printStackTrace()
        }
    }

    private fun setCurrentDate() {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        mBinding.etDate.setText("$day / ${month + 1} / $year")
    }

    companion object {
        val TAG = ExpenseDetailsActivity::class.java.simpleName
    }
}
